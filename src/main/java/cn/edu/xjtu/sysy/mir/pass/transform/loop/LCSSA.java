package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.transform.AbstractTransform;

// loop closed ssa 变换，使循环中定义的变量在循环外不被直接使用
public final class LCSSA extends AbstractTransform {
    public LCSSA(Pipeline<Module> pipeline) {
        super(pipeline);
    }

}
