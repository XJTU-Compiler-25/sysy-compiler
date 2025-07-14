package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;

import java.util.ArrayDeque;
import java.util.HashSet;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public class DeadCodeElimination extends ModuleVisitor {

    public DeadCodeElimination(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        super.visit(function);

        // 删除无用的局部变量
        function.localVars.removeIf(it -> !it.isParam && it.usedBy.isEmpty());

        // 删除空基本块
        // 删除没有前驱的基本块
        removeEmptyBlocks(function);
        removeUnreachableBlocks(function);
    }

    @Override
    public void visit(BasicBlock block) {
        var instrs = block.instructions;
        var reachable = new HashSet<Instruction>();
        // 终结指令是可达的
        reachable.add(block.terminator);
        // 有副作用的指令是可达的
        for (var it : instrs) {
            if (it instanceof Store || it instanceof Call || it instanceof CallExternal) reachable.add(it);
        }

        var worklist = new ArrayDeque<>(reachable);

        while (!worklist.isEmpty()) {
            var inst = worklist.poll();
            for (var use : inst.used) {
                if (use.value instanceof Instruction instr && reachable.add(instr))
                    worklist.add(instr);
            }
        }

        for (var iter = instrs.iterator(); iter.hasNext(); ) {
            var inst = iter.next();
            if (!reachable.contains(inst)) {
                iter.remove();
                inst.dispose();
            }
        }

        // 删除无用的 block argument
        for (var iter = block.args.entrySet().iterator(); iter.hasNext(); ) {
            var entry = iter.next();
            var var = entry.getKey();
            var blockArg = entry.getValue();
            if (!blockArg.usedBy.isEmpty()) continue;

            iter.remove();
            for (var use : block.usedBy) {
                var term = (Instruction.Terminator) use.user;
                switch (term) {
                    case Br it -> {
                        var tp = it.trueParams.remove(var);
                        if (tp != null) tp.dispose();
                        var fp = it.falseParams.remove(var);
                        if (fp != null) fp.dispose();
                    }
                    case Jmp it -> it.params.remove(var).dispose();
                    default -> unreachable();
                }
            }
        }
    }

    private void removeEmptyBlocks(Function function) {
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
                var predTerms = block.usedBy.stream()
                        .map(it -> it.user)
                        .filter(it -> it instanceof Instruction.Terminator)
                        .map(it -> (Instruction.Terminator) it)
                        .toList();
                if (args.isEmpty() || predTerms.size() == 1) {
                    iterator.remove();
                    var succ = jmp.target.value;
                    var params = jmp.params;
                    predTerms.forEach(term -> {
                        switch (term) {
                            case Br it -> {
                                it.overwriteParams(block, params);
                                it.replaceTarget(block, succ);
                            }
                            case Jmp it -> {
                                it.overwriteParams(params);
                                it.replaceTarget(succ);
                            }
                            default -> unreachable();
                        }
                    });
                    // 如果被删的是入口块，需要更新入口块
                    if (block == function.entry) function.entry = succ;
                    // 清空 block 的所有 use
                    block.dispose();
                }
            }
        }
    }

    private void removeUnreachableBlocks(Function function) {
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        function.blocks.retainAll(reachable);
    }

    private void dfs(BasicBlock entry, HashSet<BasicBlock> visited) {
        for (BasicBlock succ : entry.getSuccBlocks()) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }

}
