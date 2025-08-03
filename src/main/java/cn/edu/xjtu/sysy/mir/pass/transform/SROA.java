package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;

// Scalar Replacement of Aggregates 聚合量的标量替代
public final class SROA extends AbstractTransform {
    public SROA(Pipeline<Module> pipeline) {
        super(pipeline);
    }

}
