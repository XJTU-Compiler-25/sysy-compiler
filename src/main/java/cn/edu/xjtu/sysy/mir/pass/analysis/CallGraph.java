package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;

import java.util.Map;
import java.util.Set;

public record CallGraph(
        // 函数被哪些函数调用
        Map<Function, Set<Function>> funcsCallTo,
        Map<Function, Set<Function>> funcsCallBy,
        // 函数被别的函数调用的调用点
        Map<Function, Set<Instruction.Call>> callSitesTo,
        // 函数调用别的函数的调用点
        Map<Function, Set<Instruction.Call>> callSitesBy
) {
    public Set<Function> getFunctionsCalled(Function function) {
        return funcsCallTo.getOrDefault(function, Set.of());
    }

    public Set<Function> getFunctionsCalledBy(Function function) {
        return funcsCallBy.getOrDefault(function, Set.of());
    }

    public Set<Instruction.Call> getCallSitesTo(Function function) {
        return callSitesTo.getOrDefault(function, Set.of());
    }

    public Set<Instruction.Call> getCallSitesBy(Function function) {
        return callSitesBy.getOrDefault(function, Set.of());
    }
}
