package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;

// SCEV 分析循环中的标量如何变化
public final class ScalarEvolution extends ModuleVisitor {
    public ScalarEvolution(ErrManager errManager) {
        super(errManager);
    }


}
