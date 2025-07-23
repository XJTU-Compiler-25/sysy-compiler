package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.CallExternal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;

/** 可用表达式分析 */
public class AvailableExpression
        extends AbstractAnalysis<Map<AvailableExpression.Expr, Set<Instruction>>> {

    /** 表达式类 */
    public record Expr(Instruction data) {
        @Override
        public int hashCode() {
            var usedList = data.usedList.stream().map(u -> u.value).collect(Collectors.toList());
            return Objects.hash(data.getClass(), usedList);
        }

        /** 只要Instruction类相同，且运算元顺序和内容都相同，就算做是一个表达式 */
        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Expr otherExpr)) return false;
            if (!data.getClass().equals(otherExpr.data.getClass())) return false;
            var thisUsed = this.data.usedList.stream().map(u -> u.value).toList();
            var otherUsed = otherExpr.data.usedList.stream().map(u -> u.value).toList();
            return thisUsed.equals(otherUsed);
        }

        @Override
        public String toString() {
            return data.toString().replace(data.shortName() + " = ", "").replaceAll(" ", "_");
        }

        public Instruction getInstr() {
            return data;
        }
    }

    public AvailableExpression(Pipeline<Module> pipeline) {
        super(pipeline, FORWARD);
    }

    @Override
    protected Map<AvailableExpression.Expr, Set<Instruction>> initial() {
        return new HashMap<>();
    }

    @Override
    protected void copy(
            Map<AvailableExpression.Expr, Set<Instruction>> dst,
            Map<AvailableExpression.Expr, Set<Instruction>> src) {
        dst.clear();
        for (var key : src.keySet()) {
            dst.put(key, new HashSet<>(src.get(key)));
        }
    }

    @Override
    protected void merge(
            Map<AvailableExpression.Expr, Set<Instruction>> dst,
            Map<AvailableExpression.Expr, Set<Instruction>> src1,
            Map<AvailableExpression.Expr, Set<Instruction>> src2) {
        dst.clear();
        for (var key : src1.keySet()) {
            var set = src2.get(key);
            if (set == null) continue;
            var union = new HashSet<>(src1.get(key));
            union.addAll(set);
            dst.put(key, union);
        }
    }

    @Override
    protected void flowThrough(
            BasicBlock block, Map<AvailableExpression.Expr, Set<Instruction>> in) {
        var instrs = direction.getOrderedInstrs(block);
        var inFlow = in;
        var outFlow = initial();
        copy(outFlow, inFlow);
        // 按顺序遍历每条指令，进行分析
        for (var instr : instrs) {
            // System.out.println(instr);
            flowThrough(instr, outFlow, inFlow);
        }
        flowAfterBlock.put(block, outFlow);
    }

    @Override
    protected void flowThrough(
            Instruction instr,
            Map<AvailableExpression.Expr, Set<Instruction>> in,
            Map<AvailableExpression.Expr, Set<Instruction>> out) {
        gen(in, instr)
                .collect(Collectors.toList())
                .forEach(
                        e -> {
                            if (in.containsKey(e)) in.get(e).add(e.data);
                            else {
                                var singleton = new HashSet<Instruction>();
                                singleton.add(e.data);
                                in.put(e, singleton);
                            }
                        });
        kill(in, instr).collect(Collectors.toList()).forEach(in::remove);
    }

    private Stream<Expr> gen(
            Map<AvailableExpression.Expr, Set<Instruction>> in, Instruction instr) {
        if (instr.hasNoDef()) return Stream.empty();
        if (instr instanceof CallExternal) return Stream.empty();
        if (instr instanceof Instruction.Alloca) return Stream.empty();
        if (instr instanceof Instruction.Call it && !it.function.isPure) return Stream.empty();
        if (in.containsKey(new Expr(instr))) return Stream.empty();
        return Stream.of(new Expr(instr));
    }

    private Stream<Expr> kill(
            Map<AvailableExpression.Expr, Set<Instruction>> in, Instruction instr) {
        if (instr instanceof Instruction.Store store) {
            return in.keySet().stream()
                    .filter(
                            x ->
                                    x.data instanceof Instruction.Load load
                                            && load.address.value.equals(store.address.value));
        }
        return Stream.empty();
    }

    @Override
    public void printResult(Function function) {
        for (var block : function.blocks) {
            System.out.printf("%s:%n", block.label);
            System.out.println(getFlowBefore(block));
            System.out.println(getFlowAfter(block));
            System.out.println();
        }
    }
}
