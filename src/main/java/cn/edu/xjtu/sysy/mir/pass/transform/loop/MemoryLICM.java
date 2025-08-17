package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.LoopAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.LoopInfo;

// GCM 实现了除了 load 和 store 以外的 code motion
// MemoryLICM 专门提取循环中的 load store，其他的影响倒不一定大
public class MemoryLICM extends ModulePass<Void> {

    private LoopInfo loopInfo;

    @Override
    public void visit(Module module) {
        loopInfo = getResult(LoopAnalysis.class);

        super.visit(module);
    }

    @Override
    public void visit(Function function) {
        // 先处理内层循环
        var loops = loopInfo.getLoopsInToOut(function);
        for (var loop : loops) {

        }
    }
}
