package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;

public abstract class ModuleVisitor<R> extends Pass<Module, R> {
    public ModuleVisitor(Pipeline<Module> pipeline) {
        super(pipeline);
    }

    @Override
    public R process(Module module) {
        visit(module);
        return null;
    }

    public void visit(Module module) {
        for (var function : module.getFunctions()) visit(function);
    }

    public void visit(Function function) {
        for (var basicBlock : getCFG().getRPOBlocks(function)) visit(basicBlock);
    }

    public void visit(BasicBlock block) {
        for (var instruction : block.instructions) visit(instruction);
        visit(block.terminator);
    }

    public void visit(Instruction instruction) { }

    private void visit(Instruction.Terminator terminator) { }

    protected final CFGAnalysis.Result getCFG() {
        return pipeline.getResult(CFGAnalysis.class);
    }

    protected final CallGraphAnalysis.Result getCallGraph() {
        return pipeline.getResult(CallGraphAnalysis.class);
    }

}
