package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Use;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvailableExpression extends AbstractAnalysis<Set<Use<Instruction>>> {

    public AvailableExpression(ErrManager errManager) {
        super(errManager, AnalysisDirection.FORWARD);
    }

    @Override
    protected Set<Use<Instruction>> initial() {
        return new HashSet<>();
    }

    @Override
    protected void copy(Set<Use<Instruction>> dst, Set<Use<Instruction>> src) {
        dst.clear();
        dst.addAll(src);
    }

    @Override
    protected void merge(
            Set<Use<Instruction>> dst, Set<Use<Instruction>> src1, Set<Use<Instruction>> src2) {
        dst.clear();
        dst.addAll(src1);
        dst.retainAll(src2);
    }

    @Override
    protected void flowThrough(
            Use<Instruction> instr, Set<Use<Instruction>> in, Set<Use<Instruction>> out) {
        out.clear();
        out.addAll(in);
        gen(in, instr).forEach(out::add);
        kill(out, instr).collect(Collectors.toList()).forEach(out::remove);
    }

    private Stream<Use<Instruction>> gen(Set<Use<Instruction>> in, Use<Instruction> instr) {
        if (instr.value.hasNoDef()) return Stream.empty();
        if (!in.stream().filter(o -> instr.value.equalRVal(o.value)).findAny().isEmpty())
            return Stream.empty();
        return Stream.of(instr);
    }

    private Stream<Use<Instruction>> kill(Set<Use<Instruction>> in, Use<Instruction> instr) {
        if (instr.value instanceof Instruction.Store store) {
            return in.stream()
                    .filter(
                            x -> x.value instanceof Instruction.Load load
                                        && load.address.value.equals(store.address.value));
        }
        return Stream.empty();
    }
}
