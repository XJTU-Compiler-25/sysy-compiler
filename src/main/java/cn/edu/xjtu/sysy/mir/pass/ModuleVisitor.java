package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.util.Placeholder;

public abstract class ModuleVisitor extends Pass<Module> {
    public ModuleVisitor(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void process(Module module) {
        visit(module);
    }

    public void visit(Module module) {
        module.functions.values().forEach(this::visit);
    }

    public void visit(Function function) {
        function.blocks.forEach(this::visit);
    }

    public void visit(BasicBlock block) {
        block.instructions.forEach(it -> visit(it.value));
        visit(block.terminator);
    }

    public void visit(Instruction instruction) {
        Placeholder.pass();
    }

    private void visit(Instruction.Terminator terminator) {
        Placeholder.pass();
    }

}
