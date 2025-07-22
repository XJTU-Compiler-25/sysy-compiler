package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;

// Sparse Conditional Constant Propagation 稀有条件常量传播
public final class SCCP extends AbstractTransform {
    public SCCP(Pipeline<Module> pipeline) { super(pipeline); }

    private static final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(Module module) {
        super.visit(module);
        invalidateResult(CFGAnalysis.class);
    }

    @Override
    public void visit(BasicBlock block) {
        foldBranch(block);
    }

    // 如果一个块参数在所有情况下都一样，则
    private void removeConstPhi(BasicBlock block) {

    }

    // 尝试把 br 转为 jmp
    private void foldBranch(BasicBlock block) {
        if (block.terminator instanceof Instruction.Br br) {
            if (br.getCondition() instanceof ImmediateValue.IntConst ic) {
                if (!ic.equals(ImmediateValues.iZero)) {
                    helper.changeBlock(block);
                    var target = br.getTrueTarget();
                    var jmp = helper.jmp(target);
                    br.trueParams.forEach((var, use) -> jmp.putParam(var, use.value));
                    helper.changeBlock(null);
                    br.dispose();
                } else {
                    helper.changeBlock(block);
                    var target = br.getFalseTarget();
                    var jmp = helper.jmp(target);
                    br.falseParams.forEach((var, use) -> jmp.putParam(var, use.value));
                    helper.changeBlock(null);
                    br.dispose();
                }
            }
        }
    }

}
