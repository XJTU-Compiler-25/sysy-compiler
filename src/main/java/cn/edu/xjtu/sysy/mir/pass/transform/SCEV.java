package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// Scalar Evolution 循环变量归纳
public final class SCEV extends ModuleVisitor {
    public SCEV() { }
    public SCEV(ErrManager errManager) {
        super(errManager);
    }


}
