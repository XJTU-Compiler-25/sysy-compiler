package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;

// 通常来说会被别的 Pass 多次调用，同时不是被 MirPassGroups 直接调用的
public final class EmptyBlockElimination extends ModuleVisitor {
    public EmptyBlockElimination(ErrManager errManager) {
        super(errManager);
    }

    public void visit(Module module) {

    }

}
