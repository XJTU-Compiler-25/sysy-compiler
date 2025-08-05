package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;

// Tail Call Optimization
public final class TCO extends AbstractTransform {
    public TCO(Pipeline<Module> pipeline) { super(pipeline); }

}
