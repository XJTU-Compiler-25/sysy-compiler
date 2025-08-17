package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;

import java.util.*;

// 函数内联
// 指令数少于阈值 且 非递归 的函数，调用会被内联
public final class Inline extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    // 被内联的函数的指令条数上限
    private static final int INLINE_THRESHOLD = 600;

    private FuncInfo funcInfo;
    private final HashSet<Function> inlineCandidate = new HashSet<>();
    @Override
    public void visit(Module module) {
        funcInfo = getResult(FuncInfoAnalysis.class);

        var modified = false;
        do {
            modified = false;
            selectCandidate(module.getFunctions());
            for (var function : module.getFunctions()) modified |= processFunction(function);
        } while (modified);
    }

    private void selectCandidate(Collection<Function> functions) {
        inlineCandidate.clear();
        for (var function : functions) {
            if (funcInfo.isRecursive(function)) continue;
            if (function.getAllInstructions().size() > INLINE_THRESHOLD) continue;
            inlineCandidate.add(function);
        }
    }

    private boolean processFunction(Function function) {
        var modified = false;
        do {
            modified = false;
            for (var block : function.blocks) modified |= processBlock(block);
        } while (modified);
        return modified;
    }

    private boolean processBlock(BasicBlock block) {
        var modified = false;
        for (var inst : new ArrayList<>(block.instructions)) {
            if (inst instanceof Instruction.Call call) {
                var callee = call.getCallee();
                if (!inlineCandidate.contains(callee)) continue; // 没有被选择为内联对象
                if (callee == block.getFunction()) continue; // 递归调用不内联

                splitBlock(block, call);
                cloneFunction(call);
                modified = true;
            }
        }
        return modified;
    }

    // 以 call 指令为界，将剩余指令移动到新块，并转接控制流关系
    private void splitBlock(BasicBlock block, Instruction.Call call) {
        var function = block.getFunction();
        var newBlock = new BasicBlock(function);
        function.addBlock(newBlock);

        block.instructions.remove(call);
        newBlock.instructions.add(call);
    }

    private void cloneFunction(Instruction.Call call) {
        var valueCopy = new HashMap<Value, Value>();
        var blockCopy = new HashMap<BasicBlock, BasicBlock>();

    }

}
