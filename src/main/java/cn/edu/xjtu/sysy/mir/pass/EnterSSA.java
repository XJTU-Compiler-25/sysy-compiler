package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;

// 调用 EnterSSA 之前必须调用 DominanceAnalysis
public final class EnterSSA extends ModuleVisitor {
    public EnterSSA(ErrManager errManager) {
        super(errManager);
    }

}
