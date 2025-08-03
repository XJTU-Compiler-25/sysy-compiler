package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.LoopInfo;

import java.util.HashMap;

// Global Code Motion
// 不处理 load store
public final class GCM extends AbstractTransform {
    public GCM(Pipeline<Module> pipeline) {
        super(pipeline);
    }

    private FuncInfo funcInfo;
    private LoopInfo loopInfo;
    private final HashMap<Instruction, BasicBlock> bbEarly = new HashMap<>();
    @Override
    public void visit(Module module) {
        funcInfo = getFuncInfo();
        loopInfo = getLoopInfo();

        for (var function : module.getFunctions()) run(function);
    }

    private void run(Function function) {

    }

    private boolean isPinned(Instruction inst) {
        return switch (inst) {
            case Instruction.Load it -> true;
            case Instruction.Store it -> true;
            case Instruction.Alloca it -> true;
            case Instruction.Call it -> !funcInfo.isPure(it.getFunction());
            case Instruction.CallExternal it -> true;
            default -> false;
        };
    }


}
