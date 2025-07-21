package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.util.Placeholder;

public abstract class ModuleVisitor<R> extends Pass<Module, R> {
    public ModuleVisitor(ErrManager errManager) {
        super(errManager);
    }

    public ModuleVisitor() { super(); }

    @Override
    public R process(Module module) {
        visit(module);
        return null;
    }

    public void visit(Module module) {
        for (var function : module.getFunctions()) visit(function);
    }

    public void visit(Function function) {
        for (var basicBlock : function.getTopoSortedBlocks()) visit(basicBlock);
    }

    public void visit(BasicBlock block) {
        for (var instruction : block.instructions) visit(instruction);
        visit(block.terminator);
    }

    public void visit(Instruction instruction) { }

    private void visit(Instruction.Terminator terminator) { }

}
