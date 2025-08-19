package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.InstructionHelper;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFG;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;

import java.util.HashSet;

public final class SplitCriticalEdges extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();

    private Function currentFunction;
    public void visit(Function function) {
        var cfg = getResult(CFGAnalysis.class);
        currentFunction = function;

        for (var block : new HashSet<>(function.blocks)) {
            if (block.terminator instanceof Instruction.AbstractBr br) {
                var trueTarget = br.getTrueTarget();
                var falseTarget = br.getFalseTarget();

                if (trueTarget == falseTarget) {
                    splitEdge(br, true);
                    splitEdge(br, false);
                } else {
                    if (cfg.getPredBlocksOf(trueTarget).size() > 1) splitEdge(br, true);
                    if (cfg.getPredBlocksOf(falseTarget).size() > 1) splitEdge(br, false);
                }
            }
        }
    }

    private void splitEdge(Instruction.AbstractBr br, boolean isTrueBranch) {
        var originalTarget = isTrueBranch ? br.getTrueTarget() : br.getFalseTarget();

        var midBlock = new BasicBlock(currentFunction);
        currentFunction.addBlock(midBlock);

        helper.changeBlock(midBlock);
        var jmpToTarget = helper.insertJmp(originalTarget);
        helper.changeBlockToNull();

        if (isTrueBranch) {
            br.trueParams.forEach((arg, use) -> jmpToTarget.putParam(arg, use.value));
            br.trueParams.clear();
            br.setTrueTarget(midBlock);
        } else {
            br.falseParams.forEach((arg, use) -> jmpToTarget.putParam(arg, use.value));
            br.falseParams.clear();
            br.setFalseTarget(midBlock);
        }
    }

}

