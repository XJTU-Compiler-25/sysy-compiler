package cn.edu.xjtu.sysy.mir.pass.transform;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.Undefined;
import cn.edu.xjtu.sysy.mir.node.ImmediateValues;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.CallExternal;
import cn.edu.xjtu.sysy.mir.node.Var;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.AvailableExpression;
import cn.edu.xjtu.sysy.mir.pass.AvailableExpression.Expr;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonSubexprElimination extends AbstractTransform {

    public final AvailableExpression analysis;

    public CommonSubexprElimination(Pipeline<Module> pipeline) {
        super(pipeline);
        analysis = new AvailableExpression(pipeline);
    }

    @Override
    public void visit(Function function) {
        function.blocks.forEach(this::optLocal);
        analysis.visit(function);
        function.blocks.forEach(this::optGlobal);
    }

    public void optGlobal(BasicBlock block) {
        var map = analysis.getFlowBefore(block);
        for (var instr : block.instructions) {
            var expr = new Expr(instr);
            if (!map.containsKey(expr)) {
                continue;
            }
            var calculatedSet = map.get(expr);
            if (calculatedSet.size() == 1) {
                instr.replaceAllUsesWith(calculatedSet.iterator().next());
                //continue;
            }
            /* TODO: phi结点
            Set<BasicBlock> dfs = new HashSet<>();
            Worklist<BasicBlock> worklist =
                    new Worklist<>(
                            calculatedSet.stream()
                                    .map(x -> x.getBlock())
                                    .collect(Collectors.toList()));
            while (!worklist.isEmpty()) {
                var cur = worklist.poll();
                dfs.addAll(cur.df);
                for (var df : cur.df) worklist.add(df);
            }
                */
        }
    }

    public void optLocal(BasicBlock block) {
        List<Instruction> elim = new ArrayList<>();
        Map<AvailableExpression.Expr, Instruction> map = new HashMap<>();
        for (var instr : block.instructions) {
            var expr = new Expr(instr);
            if (map.containsKey(expr)) {
                var other = map.get(expr);
                instr.replaceAllUsesWith(other);
                elim.add(instr);
                continue;
            }
            gen(instr).forEach(e -> map.put(e, instr));
            kill(map, instr).collect(Collectors.toList()).forEach(map::remove);
        }
        elim.forEach(block.instructions::remove);
    }

    private Stream<AvailableExpression.Expr> gen(Instruction instr) {
        if (instr.hasNoDef()) return Stream.empty();
        if (instr instanceof Instruction.CallExternal) return Stream.empty();
        if (instr instanceof Instruction.Alloca) return Stream.empty();
        if (instr instanceof Instruction.Call it && !it.function.isPure) return Stream.empty();
        return Stream.of(new Expr(instr));
    }

    private Stream<AvailableExpression.Expr> kill(
            Map<AvailableExpression.Expr, Instruction> in, Instruction instr) {
        if (instr instanceof Instruction.Store store) {
            return in.keySet().stream()
                    .filter(
                            x ->
                                    x.getInstr() instanceof Instruction.Load load
                                            && load.address.value.equals(store.address.value));
        }
        return Stream.empty();
    }
}
