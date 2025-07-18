package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 运算强度削减
public final class StrengthReduction extends ModuleVisitor {
    public StrengthReduction(ErrManager errManager) {
        super(errManager);
    }
}
