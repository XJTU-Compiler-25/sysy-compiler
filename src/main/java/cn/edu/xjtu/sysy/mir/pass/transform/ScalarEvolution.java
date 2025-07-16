package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// SCEV 分析循环中的标量如何变化
public final class ScalarEvolution extends ModuleVisitor {
    public ScalarEvolution(ErrManager errManager) {
        super(errManager);
    }


}
