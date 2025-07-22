package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class AliasAnalysis extends ModuleVisitor<AliasAnalysis.Result> {
    public AliasAnalysis(Pipeline<Module> pipeline) { super(pipeline); }

    public record Result() {}

}
