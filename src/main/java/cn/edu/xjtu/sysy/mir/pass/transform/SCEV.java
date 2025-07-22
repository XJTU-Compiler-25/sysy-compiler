package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// Scalar Evolution 循环变量归纳
public final class SCEV extends AbstractTransform {
    public SCEV(Pipeline<Module> pipeline) { super(pipeline); }

}
