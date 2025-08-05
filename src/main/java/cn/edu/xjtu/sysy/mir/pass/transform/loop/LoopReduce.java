package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.transform.AbstractTransform;

public final class LoopReduce extends AbstractTransform {
    public LoopReduce(Pipeline<Module> pipeline) {
        super(pipeline);
    }
}
