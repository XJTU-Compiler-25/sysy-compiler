package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.InstructionHelper;

// 运算强度削减
public final class StrengthReduction extends ModuleVisitor {
    public StrengthReduction(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Instruction i) {
        if(true) return;

        if (i instanceof Instruction.ILt l)
            l.replaceAllUsesWith(InstructionHelper.gt(l.label, l.lhs.value, l.rhs.value));

        super.visit(i);
    }
}
