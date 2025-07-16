package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class CallGraphAnalysis extends ModuleVisitor {
    public CallGraphAnalysis(ErrManager errManager) {
        super(errManager);
    }


}
