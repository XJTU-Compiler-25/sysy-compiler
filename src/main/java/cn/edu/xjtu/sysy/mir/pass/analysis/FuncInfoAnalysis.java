package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BlockArgument;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.GlobalVar;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// 分析函数的纯性、是否最多被调用一次、是否是叶函数等特性
public final class FuncInfoAnalysis extends ModulePass<FuncInfo> {

    private CallGraph callGraph;
    private Collection<Function> functions;

    @Override
    public FuncInfo process(Module module) {
        callGraph = getResult(CallGraphAnalysis.class);
        functions = module.getFunctions();

        var pureFunctions = pureAnalysis();
        var onceFunctions = onceAnalysis();
        var leafFunctions = leafAnalysis();
        var recursiveFunctions = recursiveAnalysis();

        return new FuncInfo(pureFunctions, onceFunctions, leafFunctions, recursiveFunctions);
    }

    private Set<Function> pureAnalysis() {
        // 先默认所有函数是纯的
        var pureFunctions = new HashSet<>(functions);
        // 需要遍历 caller 的函数
        var worklist = new Worklist<Function>();

        // 第一趟遍历，仅通过函数本身的指令是否有副作用来判断纯性
        outer: for (var function : functions) {
            for (var block : function.blocks) {
                for (var instruction : block.instructions) {
                    switch (instruction) {
                        // 外部函数都有副作用
                        case CallExternal _ -> {
                            pureFunctions.remove(function);
                            worklist.add(function);
                            continue outer;
                        }
                        case GetElemPtr it -> {
                            // GetElemPtr 指令本身没有副作用，但如果取的是全局变量或者函数参数的地址，则函数不纯
                            var addr = it.basePtr.value;
                            if (addr instanceof GlobalVar || (addr instanceof BlockArgument arg && arg.isParam())) {
                                pureFunctions.remove(function);
                                worklist.add(function);
                                continue outer;
                            }
                        }
                        case Load it -> {
                            // Load 指令本身没有副作用，但如果加载的是全局变量或者函数参数，则函数不纯
                            var addr = it.getAddress();
                            if (addr instanceof GlobalVar || (addr instanceof BlockArgument arg && arg.isParam())) {
                                pureFunctions.remove(function);
                                worklist.add(function);
                                continue outer;
                            }
                        }
                        case Store it -> {
                            // 修改全局变量或者修改外部传入的指针，都具有副作用
                            var addr = it.getAddress();
                            if (addr instanceof GlobalVar || (addr instanceof BlockArgument arg && arg.isParam())) {
                                pureFunctions.remove(function);
                                worklist.add(function);
                                continue outer;
                            }

                        }
                        default -> { }
                    }
                }
            }
        }

        // 第二趟遍历，根据调用图，让不纯函数感染调用者为不纯函数
        while (!worklist.isEmpty()) {
            var function = worklist.poll();
            // 遍历函数的调用者
            for (var caller : callGraph.getFunctionsCalled(function)) {
                if (pureFunctions.contains(caller)) {
                    pureFunctions.remove(caller);
                    worklist.add(caller);
                } // 如果调用者已经被标记为不纯，则不需要处理并遍历其调用者，因为肯定已经做过了
            }
        }

        return pureFunctions;
    }

    private Set<Function> onceAnalysis() {
        var onceFunctions = new HashSet<Function>();

        for (var function : functions) {
            var callSites = callGraph.getCallSitesTo(function);
            var callerCount = callSites.size();

            // 在程序中没有被调用，只可能被外部调用，比如 main，则是最多被调用一次的
            if (callerCount == 0) onceFunctions.add(function);
            else if (callerCount > 1) { } // 多于一处调用点
            else { // 只有一处调用点
                var callSite = callSites.iterator().next();

                // 不标记递归函数
                if (callSite.getCallee() == function) { }
                // 调用点在循环中，则函数可能被多次调用
                // else if (callSite.getBlock().loopDepth != 0) { }
                // 否则，函数最多被调用一次
                else onceFunctions.add(function);
            }
        }

        return onceFunctions;
    }

    private Set<Function> leafAnalysis() {
        var leafFunctions = new HashSet<Function>();

        for (var function : functions) {
            // 如果函数没有调用其他函数，则是叶函数
            if (callGraph.getFunctionsCalledBy(function).isEmpty()) leafFunctions.add(function);
        }

        return leafFunctions;
    }


    private Set<Function> recursiveAnalysis() {
        var recursiveFunctions = new HashSet<Function>();

        for (var function : functions) {
            if (callGraph.getFunctionsCalled(function).contains(function))
                recursiveFunctions.add(function);
        }

        return recursiveFunctions;
    }

}
