package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 如果安全，将局部数组提升到全局变量
public final class VarGlobalize extends ModuleVisitor {
    public VarGlobalize() { }
    public VarGlobalize(ErrManager errManager) { super(errManager); }

}
