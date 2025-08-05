package cn.edu.xjtu.sysy.riscv.regalloc;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.riscv.ValuePosition;

import java.util.HashMap;
import java.util.Map;

public final class RegisterAllocator extends ModuleVisitor<AllocatedResult> {
    public RegisterAllocator(Pipeline<Module> pipeline) {
        super(pipeline);
    }

    private CFG cfg;
    private DomInfo domInfo;
    private FrequencyInfo frequencyInfo;
    private Map<Value, ValuePosition> allocated;
    @Override
    public AllocatedResult process(Module module) {
        cfg = getCFG();
        domInfo = getDomInfo();
        frequencyInfo = getResult(FrequencyAnalysis.class);
        allocated = new HashMap<>();

        return new AllocatedResult(allocated);
    }

    @Override
    public void visit(Function function) {
        super.visit(function);
    }


}
