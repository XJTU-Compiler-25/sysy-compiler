package cn.edu.xjtu.sysy.mir.pass;

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
        function.blocks.forEach(this::visit);
    }

    @Override
    public void visit(BasicBlock block) {
        for (var instr : block.instructions) {
            try {
                var in = analysis.getFlowBefore(instr);
                var other = in.stream().filter(o -> instr.value.equalRVal(o.value)).findFirst().get();
                instr.value.replaceAllUsesWithIf(other.value, use -> use.user instanceof Instruction);
            } catch (NoSuchElementException e) { 
                // 不存在相同的表达式的情况，跳过
            }
        }
    }
}
