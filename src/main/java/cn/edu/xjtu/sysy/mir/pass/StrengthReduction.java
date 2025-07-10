package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;

// 运算强度削减
public final class StrengthReduction extends ModuleVisitor {
    public StrengthReduction(ErrManager errManager) {
        super(errManager);
    }
}
