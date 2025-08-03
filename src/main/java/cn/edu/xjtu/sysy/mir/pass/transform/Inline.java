package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFG;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraph;

// 函数内联，在 SSA 之前做是因为改 Phi 比较麻烦
// 指令数少于阈值 且 非递归 的函数，调用会被内联
public final class Inline extends AbstractTransform {
    public Inline(Pipeline<Module> pipeline) { super(pipeline); }

    // 被内联的函数的指令条数上限
    private static final int INLINE_THRESHOLD = 128;

    private CFG cfg;
    private CallGraph callGraph;
    @Override
    public void visit(Module module) {
        cfg = getCFG();
        callGraph = getCallGraph();
    }
}
