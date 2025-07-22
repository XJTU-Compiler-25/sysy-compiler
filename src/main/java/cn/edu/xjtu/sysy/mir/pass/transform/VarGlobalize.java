package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 如果安全，将局部数组提升到全局变量
public final class VarGlobalize extends AbstractTransform {
    public VarGlobalize(Pipeline<Module> pipeline) { super(pipeline); }

}
