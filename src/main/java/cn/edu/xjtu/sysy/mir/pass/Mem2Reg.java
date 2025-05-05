package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;

public final class Mem2Reg extends ModuleVisitor {
    public Mem2Reg(ErrManager errManager) {
        super(errManager);
    }

}
