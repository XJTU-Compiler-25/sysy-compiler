package cn.edu.xjtu.sysy.mir.pass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.mir.node.Var;

public class LivenessVariable extends AbstractAnalysis<Set<Value>> {

    public LivenessVariable(ErrManager errManager) {
        super(errManager, AnalysisDirection.BACKWARD);
    }

    @Override
    protected Set<Value> initial() {
        return new HashSet<>();
    }

    @Override
    protected void copy(Set<Value> dst, Set<Value> src) {
        dst.clear();
        dst.addAll(src);
    }

    @Override
    protected void merge(Set<Value> dst, Set<Value> src1, Set<Value> src2) {
        dst.clear();
        dst.addAll(src1);
        dst.addAll(src2);
    }

    @Override
    protected void flowThrough(Instruction instr, Set<Value> in, Set<Value> out) {
        out.clear();
        out.addAll(in);
        gen(instr).forEach(out::add);
        kill(instr).forEach(out::remove);
    }

    private Stream<Value> gen(Instruction instr) {
        return instr.used.stream().map(it -> it.value).filter(it ->
            it instanceof Instruction || it instanceof Var
        );
    }

    private Stream<Value> kill(Instruction instr) {
        if (instr instanceof Instruction.Store store) {
            return Stream.of(store.address.value);
        }
        if (instr.hasNoDef()) return Stream.empty();
        return Stream.of(instr);
    }

}
