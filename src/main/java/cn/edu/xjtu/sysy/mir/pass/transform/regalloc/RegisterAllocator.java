package cn.edu.xjtu.sysy.mir.pass.transform.regalloc;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.riscv.ValuePosition;

import java.util.HashMap;

public final class RegisterAllocator extends ModulePass<AllocatedResult> {

    private CFG cfg;
    private DomInfo domInfo;
    private FrequencyInfo frequencyInfo;
    private HashMap<Value, ValuePosition> allocated;

    @Override
    public AllocatedResult process(Module module) {
        cfg = getResult(CFGAnalysis.class);
        domInfo = getResult(DominanceAnalysis.class);
        frequencyInfo = getResult(FrequencyAnalysis.class);
        allocated = new HashMap<>();

        return new AllocatedResult(allocated);
    }

    public void visit(Function function) {

    }


}
