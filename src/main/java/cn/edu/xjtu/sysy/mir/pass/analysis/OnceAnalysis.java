package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Placeholder;

import java.util.HashSet;
import java.util.Set;

// 找出最多被调用一次的函数
public final class OnceAnalysis extends ModuleVisitor<OnceAnalysis.Result> {
    public OnceAnalysis(Pipeline<Module> pipeline) { super(pipeline); }

    public record Result(Set<Function> onceFunctions) {
        public boolean isAtMostCalledOnce(Function function) {
            return onceFunctions.contains(function);
        }
    }

    @Override
    public Result process(Module module) {
        var callGraph = getCallGraph();
        var onceFunctions = new HashSet<Function>();

        for (var function : module.getFunctions()) {
            var callSites = callGraph.getCallSites(function);
            var callerCount = callSites.size();

            // 在程序中没有被调用，只可能被外部调用，比如 main，则是最多被调用一次的
            if (callerCount == 0) onceFunctions.add(function);
            else if (callerCount > 1) Placeholder.pass(); // 多于一处调用点
            else { // 只有一处调用点
                var callSite = callSites.iterator().next();

                // 我们不把递归函数纳入考虑
                if (callSite.getFunction() == function) Placeholder.pass();
                    // 调用点在循环中，则函数可能被多次调用
                else if (callSite.getBlock().loopDepth != 0) Placeholder.pass();
                    // 否则，函数最多被调用一次
                else onceFunctions.add(function);
            }
        }

        return new Result(onceFunctions);
    }
}
