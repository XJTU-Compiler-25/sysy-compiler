package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class LoopAnalysis extends ModuleVisitor {
    public LoopAnalysis(ErrManager errManager) {
        super(errManager);
    }

}
