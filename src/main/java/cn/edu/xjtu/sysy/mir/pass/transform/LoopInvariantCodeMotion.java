package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// LICM 循环不变量外提
public final class LoopInvariantCodeMotion extends ModuleVisitor {
    public LoopInvariantCodeMotion(ErrManager errManager) {
        super(errManager);
    }

}
