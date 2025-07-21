package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashSet;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public class DeadCodeElimination extends ModuleVisitor {

    public DeadCodeElimination(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        removeUnreachableBlocks(function);
        removeUnusedInstructions(function);
        removeEmptyBlocks(function);
        removeUnusedBlockArguments(function);
        removeUnusedLocalVars(function);
    }

    private static void removeUnusedInstructions(Function function) {
        var blocks = function.blocks;
        var reachable = new HashSet<Instruction>();
        for (var block : blocks) {
            var instrs = block.instructions;
            // 终结指令是可达的
            reachable.add(block.terminator);
            // 有副作用的指令是可达的
            for (var it : instrs) {
                if (it instanceof Store
                        || (it instanceof Call call && !call.getFunction().isPure)
                        || it instanceof CallExternal)
                    reachable.add(it);
            }

            var worklist = new Worklist<>(reachable);

            while (!worklist.isEmpty()) {
                var inst = worklist.poll();
                for (var use : inst.used) {
                    if (use.value instanceof Instruction instr) {
                        reachable.add(instr);
                        worklist.add(instr);
                    }
                }
            }
        }

        for (var block : blocks) {
            for (var iter = block.instructions.iterator(); iter.hasNext(); ) {
                var inst = iter.next();
                if (!reachable.contains(inst)) {
                    iter.remove();
                    inst.dispose();
                }
            }
        }
    }

    private static void removeUnusedBlockArguments(Function function) {
        function.blocks.forEach(DeadCodeElimination::removeUnusedBlockArguments);
    }

    private static void removeUnusedBlockArguments(BasicBlock block) {
        // 删除无用的 block argument
        for (var iter = block.args.entrySet().iterator(); iter.hasNext(); ) {
            var entry = iter.next();
            var var = entry.getKey();
            var blockArg = entry.getValue();
            if (!blockArg.notUsed()) continue;

            iter.remove();
            for (var term : block.getPredTerminators()) {
                switch (term) {
                    case Br it -> {
                        if (it.getTrueTarget() == block) {
                            var tp = it.trueParams.remove(var);
                            if (tp != null) tp.dispose();
                        }
                        if (it.getFalseTarget() == block) {
                            var fp = it.falseParams.remove(var);
                            if (fp != null) fp.dispose();
                        }
                    }
                    case Jmp it -> {
                        var p = it.params.remove(var);
                        if (p != null) p.dispose();
                    }
                    default -> unreachable();
                }
            }
        }
    }

    private static void removeUnusedLocalVars(Function function) {
        function.localVars.removeIf(Value::notUsed);
    }

    private static void removeUnreachableBlocks(Function function) {
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        for (var iter = function.blocks.iterator(); iter.hasNext(); ) {
            var block = iter.next();
            if (!reachable.contains(block)) {
                iter.remove();
                block.dispose();
            }
        }
    }

    private static void dfs(BasicBlock block, HashSet<BasicBlock> visited) {
        for (var succ : block.getSuccBlocks()) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }

    private static void removeEmptyBlocks(Function function) {
        // 由于删除一个块可能会使别的块也变得可删除，可能需要多次运行
        boolean changed = true;
        while(changed) {
            changed = false;
            for (var iterator = function.blocks.iterator(); iterator.hasNext(); ) {
                var blockToRemove = iterator.next();

                // 不删入口块
                if (blockToRemove == function.entry) continue;
                // 单后继才能删除
                if (!(blockToRemove.terminator instanceof Instruction.Jmp jmp)) continue;
                if (!blockToRemove.instructions.isEmpty()) continue;

                var predTerms = blockToRemove.getPredTerminators();
                var succ = jmp.getTarget();
                // 删除自环可能会改变程序语义，不删
                if (succ != blockToRemove
                        // 没有 phi，一定可以删除，有 phi 而只有单个前驱，也可以删除
                        && (blockToRemove.args.isEmpty()
                            || predTerms.size() == 1)) {
                    iterator.remove();
                    for (var term : predTerms) {
                        term.replaceTarget(blockToRemove, succ);
                        jmp.params.forEach((var, use) -> {
                            // 如果这个值是直接从参数里接到，直接传给后继，则不用 put
                            // 只需要将本块中产生的新值重写到前导跳转上
                            if (!(use.value instanceof BlockArgument arg && arg.block == blockToRemove))
                                term.putParam(succ, var, use.value);
                        });
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
    }

}
