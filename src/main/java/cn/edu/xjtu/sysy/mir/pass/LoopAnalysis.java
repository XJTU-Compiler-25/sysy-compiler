package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;

public final class LoopAnalysis extends ModuleVisitor {
    public LoopAnalysis(ErrManager errManager) {
        super(errManager);
    }

}
