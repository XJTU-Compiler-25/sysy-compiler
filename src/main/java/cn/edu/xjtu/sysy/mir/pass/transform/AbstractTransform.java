package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public abstract class AbstractTransform extends ModuleVisitor<Void> {
    public AbstractTransform(Pipeline<Module> pipeline) { super(pipeline); }

    public final Void process(Module module) {
        super.process(module);
        return null;
    }
}
