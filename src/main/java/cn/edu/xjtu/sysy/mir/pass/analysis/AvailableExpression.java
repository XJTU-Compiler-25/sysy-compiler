package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
            return data.getClass().hashCode() << 8 + data.used.size();
        }

        /** 只要Instruction类相同，且运算元顺序和内容都相同，就算做是一个表达式 */
        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (getClass() != other.getClass()) return false;
            Expr otherExpr = (Expr) other;
            var thisUsed =
                    this.data.usedList.stream().map(use -> use.value).collect(Collectors.toList());
            var otherUsed =
                    otherExpr.data.usedList.stream()
                            .map(use -> use.value)
                            .collect(Collectors.toList());
            return thisUsed.equals(otherUsed);
        }

        @Override
        public String toString() {
            return data.toString().replace(data.shortName() + " = ", "");
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
            Instruction instr,
            Map<AvailableExpression.Expr, Set<Instruction>> in,
            Map<AvailableExpression.Expr, Set<Instruction>> out) {
        out.clear();
        for (var key : in.keySet()) {
            out.put(key, new HashSet<>(in.get(key)));
        }
        gen(in, instr).forEach(e -> {
            if (out.containsKey(e)) 
                out.get(e).add(e.data);
            else 
                out.put(e, new HashSet<>(Collections.singleton(e.data)));
        });
        kill(out, instr).collect(Collectors.toList()).forEach(out::remove);
    }

    private Stream<Expr> gen(
            Map<AvailableExpression.Expr, Set<Instruction>> in, Instruction instr) {
        if (instr.hasNoDef()) return Stream.empty();
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
}
