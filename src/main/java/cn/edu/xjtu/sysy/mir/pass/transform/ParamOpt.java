package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraph;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfoAnalysis;
import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;

// 函数参数优化
// 1. 死参数删除（未使用的）
// 2. 常量参数内联（在所有 call site 相同的）
public final class ParamOpt extends ModulePass<Void> {

    private CallGraph callGraph;
    private FuncInfo funcInfo;

    @Override
    public void visit(Module module) {
        callGraph = getResult(CallGraphAnalysis.class);
        funcInfo = getResult(FuncInfoAnalysis.class);

        // 收集需要处理的函数（非递归，非外部函数）
        for (var func : module.getFunctions()) {
            if (funcInfo.isRecursive(func)) continue;

            removeDeadParameters(func);
            inlineConstantParameters(func);
        }
    }

    private void removeDeadParameters(Function func) {
        var entryArgs = func.entry.args;
        var params = func.params;
        var callSites = callGraph.getCallSitesTo(func);
        for (int i = 0, size = params.size(); i < size; i++) {
            var pair = func.params.get(i);
            var param = pair.second();

            if (!param.notUsed()) continue;

            params.remove(i);
            entryArgs.remove(param);
            for (var callSite : callSites) callSite.removeArg(i);
            --i;
            --size;
        }
    }

    private void inlineConstantParameters(Function func) {
        var entryArgs = func.entry.args;
        var params = func.params;
        var callSites = callGraph.getCallSitesTo(func);
        if (callSites.isEmpty()) return;

        outer: for (int i = 0, size = params.size(); i < size; i++) {
            var pair = func.params.get(i);
            var param = pair.second();

            Value argVal = null;
            for (var callSite : callSites) {
                var arg = callSite.getArg(i);
                if (!(arg instanceof ImmediateValue)) continue outer;
                if (!(arg.type instanceof Type.Scalar)) continue outer;
                if (argVal == null) argVal = arg;
                else if (!argVal.equals(arg)) continue outer;
            }

            param.replaceAllUsesWith(argVal);
            params.remove(i);
            entryArgs.remove(param);
            for (var callSite : callSites) callSite.removeArg(i);
            --i;
            --size;
        }
    }
}
