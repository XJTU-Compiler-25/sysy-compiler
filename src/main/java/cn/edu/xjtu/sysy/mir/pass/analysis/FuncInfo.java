package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.Function;

import java.util.Set;

// 函数的各项元信息
public record FuncInfo(
        Set<Function> pureFunctions,
        Set<Function> onceFunctions,
        Set<Function> leafFunctions,
        Set<Function> recursiveFunctions
) {
    public boolean isPure(Function function) {
        return pureFunctions.contains(function);
    }

    public boolean isOnce(Function function) {
        return onceFunctions.contains(function);
    }

    public boolean isLeaf(Function function) {
        return leafFunctions.contains(function);
    }

    public boolean isRecursive(Function function) {
        return recursiveFunctions.contains(function);
    }
}
