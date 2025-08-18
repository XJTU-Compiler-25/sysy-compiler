package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

import java.util.*;

// 求出函数互相的调用关系
public final class CallGraphAnalysis extends ModulePass<CallGraph> {

    @Override
    public CallGraph process(Module module) {
        var funcCallToMap = new HashMap<Function, Set<Function>>();
        var funcCallByMap = new HashMap<Function, Set<Function>>();
        var callSiteToMap = new HashMap<Function, Set<Instruction.Call>>();
        var callSiteByMap = new HashMap<Function, Set<Instruction.Call>>();

        var functions = module.getFunctions();

        for (var function : functions) {
            var funcCallTo = funcCallToMap.computeIfAbsent(function, _ -> new HashSet<>());
            var callSiteTo = callSiteToMap.computeIfAbsent(function, _ -> new HashSet<>());
            
            for (var use : function.usedBy) {
                if (use.user instanceof Instruction.Call it) {
                    var callerFunction = it.getBlock().getFunction();
                    callSiteTo.add(it);
                    funcCallTo.add(callerFunction);

                    var funcCallBy = funcCallByMap.computeIfAbsent(callerFunction, _ -> new HashSet<>());
                    var callSiteBy = callSiteByMap.computeIfAbsent(callerFunction, _ -> new HashSet<>());
                    callSiteBy.add(it);
                    funcCallBy.add(function);
                }
            }
        }

        return new CallGraph(funcCallToMap, funcCallByMap, callSiteToMap, callSiteByMap);
    }

}
