package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.transform.AbstractTransform;

public final class LoopFusion extends AbstractTransform {
    public LoopFusion(Pipeline<Module> pipeline) {
        super(pipeline);
    }

}
