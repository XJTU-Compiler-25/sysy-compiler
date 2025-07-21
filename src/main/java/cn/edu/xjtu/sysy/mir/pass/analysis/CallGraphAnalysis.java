package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.*;

// 求出函数互相的调用关系
public final class CallGraphAnalysis extends ModuleVisitor {
    public CallGraphAnalysis(ErrManager errManager) {
        super(errManager);
    }

    public CallGraphAnalysis() { }

    /*
    public record Result(Map<Function, Set<Function>> callerMap, Map<Function, Set<Function>> calleeMap) {
        public Set<Function> getCallers(Function function) {
            return callerMap.getOrDefault(function, Set.of());
        }

        public Set<Function> getCallees(Function function) {
            return calleeMap.getOrDefault(function, Set.of());
        }
    }
    */

    @Override
    public void visit(Module module) {
        for (var function : module.getFunctions()) {
            for (var use : function.usedBy) {
                if (use.user instanceof Instruction.Call it) {
                    var callerFunction = it.getBlock().getFunction();
                    function.callSites.add(it);
                    function.callers.add(callerFunction);
                    callerFunction.callees.add(function);
                }
            }
        }
    }

}
