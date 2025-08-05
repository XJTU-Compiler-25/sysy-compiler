/*
package cn.edu.xjtu.sysy.mir.pass.transform;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.AvailableExpression;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public class CommonSubexprElimination extends AbstractTransform {
    
    public final AvailableExpression analysis;

    public CommonSubexprElimination(Pipeline<Module> pipeline) {
        super(pipeline);
        analysis = new AvailableExpression(pipeline);
    }

    @Override
    public void visit(Function function) {
        analysis.visit(function);
        analysis.printResult(function);
        getCFG().getRPOBlocks(function).forEach(this::visit);
    }

    @Override
    public void visit(BasicBlock block) {
        List<Instruction> elim = new ArrayList<>();
        for (var instr : block.instructions) {
            var in = analysis.getFlowBefore(instr);
            var other = in.get(new AvailableExpression.Expr(instr));
            if (other == null) continue;
            // TODO: 对于set size > 1的情况，则存在多条控制流计算了这个expression，可以插入基本块参数
            instr.replaceAllUsesWith(other.iterator().next());
            elim.add(instr);
        }
        elim.forEach(block.instructions::remove);
    }
}
*/
