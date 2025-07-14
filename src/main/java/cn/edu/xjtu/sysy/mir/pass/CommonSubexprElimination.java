package cn.edu.xjtu.sysy.mir.pass;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;

public class CommonSubexprElimination extends ModuleVisitor {
    
    public final AvailableExpression analysis;

    public CommonSubexprElimination(ErrManager errManager) {
        super(errManager);
        analysis = new AvailableExpression(errManager);
    }

    @Override
    public void visit(Function function) {
        analysis.visit(function);
        analysis.printResult(function);
        function.blocks.forEach(this::visit);
    }

    @Override
    public void visit(BasicBlock block) {
        List<Instruction> elim = new ArrayList<>();
        for (var instr : block.instructions) {
            var in = analysis.getFlowBefore(instr);
            var other = in.get(new AvailableExpression.Expr(instr));
            if (other == null) continue;
            // TODO: 对于set size > 1的情况，需要添加phi结点
            instr.replaceAllUsesWith(other.iterator().next());
            elim.add(instr);
        }
        elim.forEach(block.instructions::remove);
    }
}
