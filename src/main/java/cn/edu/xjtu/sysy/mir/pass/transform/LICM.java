package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// Loop Invariant Code Motion 循环不变量外提
public final class LICM extends AbstractTransform {
    public LICM(Pipeline<Module> pipeline) { super(pipeline); }

}
