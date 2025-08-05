package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;

// Global Value Numbering
public final class GVN extends AbstractTransform {
    public GVN(Pipeline<Module> pipeline) { super(pipeline); }

}
