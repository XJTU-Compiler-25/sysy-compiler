package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashSet;

public final class CFGSimplify extends ModuleTransformer {

    private static final InstructionHelper helper = new InstructionHelper();
    @Override
    public void visit(Module module) {
        var modified = false;
        do {
            modified = false;
            modified |= foldConstBranch(module);
            modified |= removeUnreachableBlocks(module);
            modified |= removeEmptyBlocks(module);
        } while(modified);
    }

    public static boolean removeUnreachableBlocks(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) modified |= removeUnreachableBlocks(function);
        return modified;
    }

    public static boolean removeUnreachableBlocks(Function function) {
        var modified = false;
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        for (var iterator = function.blocks.iterator(); iterator.hasNext(); ) {
            var block = iterator.next();
            if (!reachable.contains(block)) {
                iterator.remove();
                block.dispose();
                modified = true;
            }
        }
        return modified;
    }

    private static void dfs(BasicBlock block, HashSet<BasicBlock> visited) {
        for (var succ : CFGAnalysis.getSuccBlocksOf(block)) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }


    public static boolean removeEmptyBlocks(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) modified |= removeEmptyBlocks(function);
        return modified;
    }

    // 由于删除一个块可能会使别的块也变得可删除，可能需要多次运行
    public static boolean removeEmptyBlocks(Function function) {
        var modified = false;
        var worklist = new Worklist<>(function.blocks);
        while(!worklist.isEmpty()) {
            var block = worklist.poll();
            // 不删入口块
            if (block == function.entry) continue;
            // 单后继才能删除
            if (!(block.terminator instanceof Instruction.Jmp jmp)) continue;
            if (!block.instructions.isEmpty()) continue;

            var succ = jmp.getTarget();
            // 删除自环可能会改变程序语义，不删
            if (succ == block) continue;

            var predTerms = CFGAnalysis.getPredTermsOf(block);
            if (block.args.isEmpty()) {
                for (var term : predTerms) {
                    jmp.params.forEach(pair -> term.putParam(block, pair.first().value, pair.second().value));
                    term.replaceTarget(block, succ);
                }

                modified = true;
                function.removeBlock(block);
                block.dispose();
            }
        }
        return modified;
    }

    public static boolean foldConstBranch(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) modified |= foldConstBranch(function);
        return modified;
    }

    // 尝试把 br 转为 jmp
    public static boolean foldConstBranch(Function function) {
        var modified = false;
        for (var block : function.blocks) {
            if (block.terminator instanceof Instruction.Br br) {
                if (br.getCondition() instanceof ImmediateValue.IntConst ic) {
                    modified = true;
                    if (!ic.equals(ImmediateValues.iZero)) {
                        helper.changeBlock(block);
                        var target = br.getTrueTarget();
                        var jmp = helper.jmp(target);
                        br.trueParams.forEach(pair ->
                                jmp.putParam(target, pair.first().value, pair.second().value));
                        helper.changeBlockToNull();
                        br.dispose();
                    } else {
                        helper.changeBlock(block);
                        var target = br.getFalseTarget();
                        var jmp = helper.jmp(target);
                        br.falseParams.forEach(pair ->
                                jmp.putParam(target, pair.first().value, pair.second().value));
                        helper.changeBlockToNull();
                        br.dispose();
                    }
                } else if (br.getTrueTarget() == br.getFalseTarget()) {
                    for (var i = 0; i < br.trueParams.size(); i++) {
                        var tp = br.trueParams.get(i);
                        var fp = br.falseParams.get(i);
                        if (tp.first().value != fp.first().value || tp.second().value != fp.second().value)
                            return modified;
                    }
                    modified = true;
                    helper.changeBlock(block);
                    var target = br.getTrueTarget();
                    var jmp = helper.jmp(target);
                    br.trueParams.forEach(pair ->
                            jmp.putParam(target, pair.first().value, pair.second().value));
                    helper.changeBlockToNull();
                    br.dispose();
                }
            }
        }
        return modified;
    }

}
