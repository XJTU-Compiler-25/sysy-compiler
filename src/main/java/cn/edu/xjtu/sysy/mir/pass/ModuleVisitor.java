package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Terminator;
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
        block.instructions.forEach(this::visit);
        visit(block.terminator);
    }

    public void visit(Instruction instruction) {
        Placeholder.pass();
    }

    private void visit(Terminator terminator) {
        Placeholder.pass();
    }

}
