package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 如果安全，将全局变量降低到局部变量
public final class VarLocalize extends ModuleVisitor {
    public VarLocalize() { }
    public VarLocalize(ErrManager errManager) { super(errManager); }


}
