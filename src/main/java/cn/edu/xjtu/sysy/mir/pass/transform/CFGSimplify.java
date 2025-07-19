package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.HashSet;

public final class CFGSimplify extends ModuleVisitor {
    public CFGSimplify(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        // 删除空基本块
        removeEmptyBlocks(function);
        // 删除没有前驱的基本块
        removeUnreachableBlocks(function);
    }

    private static void removeEmptyBlocks(Function function) {
        // 由于删除一个块可能会使别的块也变得可删除，可能需要多次运行
        boolean changed = true;
        while(changed) {
            changed = false;
            for (var iterator = function.blocks.iterator(); iterator.hasNext(); ) {
                var block = iterator.next();

                // 不删入口块
                if (block == function.entry) continue;
                // 单后继才能删除
                if (!(block.terminator instanceof Instruction.Jmp jmp)) continue;
                if (!block.instructions.isEmpty()) continue;

                var params = jmp.params;
                var predTerms = block.getPredTerminators();
                var succ = jmp.getTarget();
                // 删除自环可能会改变程序语义，不删
                if (succ != block
                        // 没有 phi，一定可以删除，有 phi 而只有单个前驱，也可以删除
                        && (block.args.isEmpty()
                            || predTerms.size() == 1)) {
                    iterator.remove();
                    for (var term : predTerms) {
                        term.replaceTarget(block, succ);
                        term.overwriteParams(succ, params);
                    }

                    // 将本块的参数都换成后继块的参数
                    block.args.forEach((var, arg) -> {
                        var succArg = succ.getBlockArgument(var);
                        if (succArg == null) succArg = succ.addBlockArgument(var);
                        arg.replaceAllUsesWith(succArg);
                    });
                    // 清空 block 的所有 use
                    block.dispose();
                }
            }
        }
    }

    private static void removeUnreachableBlocks(Function function) {
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        function.blocks.retainAll(reachable);
    }

    private static void dfs(BasicBlock entry, HashSet<BasicBlock> visited) {
        for (BasicBlock succ : entry.getSuccBlocks()) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }


}
