package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 求出基本块的 pred, succ 列表
public final class CFGAnalysis extends ModuleVisitor {
    public CFGAnalysis() { }
    public CFGAnalysis(ErrManager errManager) {
        super(errManager);
    }

    /*
    public record Result(Map<BasicBlock, Set<BasicBlock>> predMap, Map<BasicBlock, Set<BasicBlock>> succMap) { }
    */

    public static void run(Function function) {
        new CFGAnalysis().visit(function);
    }

    @Override
    public void visit(Function function) {
        for (var block : function.blocks) visit(block);
    }

    @Override
    public void visit(BasicBlock block) {
        block.preds.clear();
        block.predTerms.clear();
        block.succs.clear();

        for (var use : block.usedBy) {
            if (use.user instanceof Instruction.Terminator term) {
                block.predTerms.add(term);
                block.preds.add(term.getBlock());
            }
        }

        switch(block.terminator) {
            case Instruction.Jmp jmp -> block.succs.add(jmp.getTarget());
            case Instruction.Br br -> block.succs.addAll(br.getTargets());
            default -> { }
        }
    }

}
