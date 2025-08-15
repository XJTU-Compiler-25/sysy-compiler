package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFG;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraph;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;

import java.util.HashMap;

// 函数内联
// 指令数少于阈值 且 非递归 的函数，调用会被内联
public final class Inline extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    // 被内联的函数的指令条数上限
    private static final int INLINE_THRESHOLD = 600;

    private CFG cfg;
    private CallGraph callGraph;
    @Override
    public void visit(Module module) {
        cfg = getResult(CFGAnalysis.class);
        callGraph = getResult(CallGraphAnalysis.class);

    }

    private void selectCandidates() {

    }


    private final HashMap<Value, Value> valueCopy = new HashMap<>();
    private final HashMap<BasicBlock, BasicBlock> blockCopy = new HashMap<>();

}
