package cn.edu.xjtu.sysy.riscv.regalloc;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleAnalysis;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.riscv.ValuePosition;

import java.util.HashMap;
import java.util.Map;

public final class RegisterAllocator extends ModuleAnalysis<AllocatedResult> {

    private CFG cfg;
    private DomInfo domInfo;
    private FrequencyInfo frequencyInfo;
    private Map<Value, ValuePosition> allocated;
    @Override
    public AllocatedResult process(Module module) {
        cfg = CFGAnalysis.run(module);
        domInfo = DominanceAnalysis.run(module);
        frequencyInfo = FrequencyAnalysis.run(module);
        allocated = new HashMap<>();

        return new AllocatedResult(allocated);
    }

    public void visit(Function function) {

    }


}
