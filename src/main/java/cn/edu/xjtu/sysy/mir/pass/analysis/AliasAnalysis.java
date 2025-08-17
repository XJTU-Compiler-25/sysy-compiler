package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

import static cn.edu.xjtu.sysy.mir.pass.analysis.AliasState.*;

public final class AliasAnalysis extends ModulePass<AliasInfo> {

    @Override
    public AliasInfo process(Module obj) {
        return null;
    }

    public AliasState analyze() {
        return MAY_ALIAS;
    }

}
