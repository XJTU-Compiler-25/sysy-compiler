package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.transform.AbstractTransform;

public final class LoopFission extends AbstractTransform {
    public LoopFission(Pipeline<Module> pipeline) {
        super(pipeline);
    }

}
