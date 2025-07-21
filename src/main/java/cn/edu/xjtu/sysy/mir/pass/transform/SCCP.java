package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class SCCP extends ModuleVisitor {
    public SCCP() { }
    public SCCP(ErrManager errManager) { super(errManager); }

    private static final InstructionHelper helper = new InstructionHelper();

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
            if (br.getCondition() instanceof ImmediateValue iv) {
                if (iv != ImmediateValues.iZero) {
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
