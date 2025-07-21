package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;

import java.util.*;

public final class CFGSimplify extends ModuleVisitor {
    public CFGSimplify() { }
    public CFGSimplify(ErrManager em) { super(em); }

    @Override
    public void visit(Function function) {
        removeUnreachableBlocks(function);
        removeEmptyBlocks(function);
    }

    private static void removeUnreachableBlocks(Function function) {
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        for (var iter = function.getTopoSortedBlocks().iterator(); iter.hasNext(); ) {
            var block = iter.next();
            if (!reachable.contains(block)) {
                iter.remove();
                block.dispose();
            }
        }

        // 改变了 CFG 结构
        CFGAnalysis.run(function);
    }

    private static void dfs(BasicBlock block, HashSet<BasicBlock> visited) {
        for (var succ : block.succs) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }

    private static void removeEmptyBlocks(Function function) {
        // 由于删除一个块可能会使别的块也变得可删除，可能需要多次运行
        boolean changed = true;
        while(changed) {
            changed = false;
            for (var iterator = function.getTopoSortedBlocks().iterator(); iterator.hasNext(); ) {
                var blockToRemove = iterator.next();

                // 不删入口块
                if (blockToRemove == function.entry) continue;
                // 单后继才能删除
                if (!(blockToRemove.terminator instanceof Instruction.Jmp jmp)) continue;
                if (!blockToRemove.instructions.isEmpty()) continue;

                var predTerms = blockToRemove.predTerms;
                var succ = jmp.getTarget();
                // 删除自环可能会改变程序语义，不删
                if (succ != blockToRemove
                        // 没有 phi，一定可以删除，有 phi 而只有单个前驱，也可以删除
                        && (blockToRemove.args.isEmpty()
                        || predTerms.size() == 1)) {
                    iterator.remove();
                    for (var term : predTerms) {
                        jmp.params.forEach((var, use) -> {
                            // 如果这个值是直接从参数里接到，直接传给后继，则不用 put
                            // 只需要将本块中产生的新值重写到前导跳转上
                            if (!(use.value instanceof BlockArgument arg && arg.block == blockToRemove))
                                term.putParam(blockToRemove, var, use.value);
                        });
                        term.replaceTarget(blockToRemove, succ);
                    }

                    // 将本块的参数都换成后继块的参数
                    blockToRemove.args.forEach((var, arg) -> {
                        var succArg = succ.getBlockArgument(var);
                        if (succArg == null) succArg = succ.addBlockArgument(var);
                        arg.replaceAllUsesWith(succArg);
                    });
                    // 清空 blockToRemove 的所有 use
                    blockToRemove.dispose();
                }
            }
        }

        // 改变了 CFG 结构
        CFGAnalysis.run(function);
    }
}
