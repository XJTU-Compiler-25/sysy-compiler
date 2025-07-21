package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// Global Value Numbering
public final class GVN extends ModuleVisitor {
    public GVN(ErrManager errManager) {
        super(errManager);
    }

}
