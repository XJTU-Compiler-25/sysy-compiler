package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 如果安全，将全局变量降低到局部变量
public final class VarLocalize extends AbstractTransform {
    public VarLocalize(Pipeline<Module> pipeline) { super(pipeline); }

}
