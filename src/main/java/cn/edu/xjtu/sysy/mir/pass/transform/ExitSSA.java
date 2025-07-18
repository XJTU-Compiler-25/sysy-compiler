package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class ExitSSA extends ModuleVisitor {
    public ExitSSA(ErrManager errManager) {
        super(errManager);
    }

}
