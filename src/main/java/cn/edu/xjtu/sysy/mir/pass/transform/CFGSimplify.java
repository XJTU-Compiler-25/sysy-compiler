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

                // 单后继才能删除
                if (!(block.terminator instanceof Instruction.Jmp jmp)) continue;
                if (!block.instructions.isEmpty()) continue;

                // 没有 phi，一定可以删除，有 phi 而只有单个前驱，也可以删除
                var args = block.args;
                var predTerms = block.getPredTerminators();
                if (args.isEmpty() || predTerms.size() == 1) {
                    iterator.remove();
                    var succ = jmp.getTarget();
                    var params = jmp.params;
                    for (var term : predTerms) {
                        term.overwriteParams(block, params);
                        term.replaceTarget(block, succ);
                    }
                    // 如果被删的是入口块，需要更新入口块
                    if (block == function.entry) function.entry = succ;
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
