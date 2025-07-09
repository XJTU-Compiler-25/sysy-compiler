package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;

// EnterSSA 假设 DominanceAnalysis 已经完成
public final class EnterSSA extends ModuleVisitor {
    public EnterSSA(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Module module) {
        super.visit(module);
    }

    @Override
    public void visit(Function function) {
        super.visit(function);
    }

    @Override
    public void visit(BasicBlock block) {
        super.visit(block);
    }
}
