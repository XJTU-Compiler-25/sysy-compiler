package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashMap;
import java.util.stream.Collectors;

public final class Module {
    public final HashMap<String, GlobalVar> globalVars = new HashMap<>();
    public final HashMap<String, Function> functions = new HashMap<>();

    public Function main;

    public Function newFunction(String name, Type retType) {
        Function function = new Function(name, retType);
        functions.put(name, function);
        return function;
    }

    public GlobalVar newGlobalVar(String name, Type type) {
        var globalVar = new GlobalVar(name, type);
        globalVars.put(name, globalVar);
        return globalVar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global Variables:\n")
                .append(globalVars.values().stream().map(it -> "@" + it.name + " : " + it.type)
                        .collect(Collectors.joining(", ")))
                .append("\nFunctions:\n")
                .append(functions.values().stream().map(Function::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
