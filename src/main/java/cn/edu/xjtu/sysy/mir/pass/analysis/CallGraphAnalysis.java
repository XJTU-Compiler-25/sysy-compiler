package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.*;

// 求出函数互相的调用关系
public final class CallGraphAnalysis extends ModuleVisitor<CallGraphAnalysis.Result> {
    public CallGraphAnalysis(Pipeline<Module> pipeline) { super(pipeline); }

    public record Result(
            Map<Function, Set<Function>> callerMap,
            Map<Function, Set<Function>> calleeMap,
            Map<Function, Set<Instruction.Call>> callSiteMap
    ) {
        public Set<Function> getCallers(Function function) {
            return callerMap.getOrDefault(function, Set.of());
        }

        public Set<Function> getCallees(Function function) {
            return calleeMap.getOrDefault(function, Set.of());
        }

        public Set<Instruction.Call> getCallSites(Function function) {
            return callSiteMap.getOrDefault(function, Set.of());
        }
    }

    private final HashMap<Function, Set<Function>> callerMap = new HashMap<>();
    private final HashMap<Function, Set<Function>> calleeMap = new HashMap<>();
    private final HashMap<Function, Set<Instruction.Call>> callSiteMap = new HashMap<>();

    @Override
    public Result process(Module module) {
        var functions = module.getFunctions();
        for (var function : functions) {
            callerMap.put(function, new HashSet<>());
            calleeMap.put(function, new HashSet<>());
            callSiteMap.put(function, new HashSet<>());
        }

        for (var function : functions) {
            var callers = callerMap.get(function);
            var callSites = callSiteMap.get(function);
            for (var use : function.usedBy) {
                if (use.user instanceof Instruction.Call it) {
                    var callerFunction = it.getBlock().getFunction();
                    callSites.add(it);
                    callers.add(callerFunction);
                    calleeMap.get(callerFunction).add(function);
                }
            }
        }

        return new Result(callerMap, calleeMap, callSiteMap);
    }

}
