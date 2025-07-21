package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

public final class OnceAnalysis extends ModuleVisitor {
    public OnceAnalysis(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        var callSites = function.callSites;
        var callerCount = callSites.size();

        // 在程序中没有被调用，只可能被外部调用，比如 main，则是最多被调用一次的
        if (callerCount == 0) function.isAtMostCalledOnce = true;
        else if (callerCount > 1) function.isAtMostCalledOnce = false; // 多于一处调用点
        else { // 只有一处调用点
            var callSite = callSites.getFirst();

            // 我们不把递归函数纳入考虑
            if (callSite.getFunction() == function) function.isAtMostCalledOnce = false;
            // 调用点在循环中，则函数可能被多次调用
            else if (callSite.getBlock().loopDepth != 0) function.isAtMostCalledOnce = false;
            // 否则，函数最多被调用一次
            else function.isAtMostCalledOnce = true;
        }
    }
}
