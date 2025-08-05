package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.transform.AbstractTransform;

public final class LoopInterchange extends AbstractTransform {
    public LoopInterchange(Pipeline<Module> pipeline) {
        super(pipeline);
    }

}
