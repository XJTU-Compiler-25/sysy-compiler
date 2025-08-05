package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class RangeAnalysis extends ModuleVisitor<RangeInfo> {
    public RangeAnalysis(Pipeline<Module> pipeline) {
        super(pipeline);
    }

}
