package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashMap;
import java.util.stream.Collectors;

public final class Module {
    public final HashMap<String, Var> globalVars = new HashMap<>();
    public final HashMap<String, Function> functions = new HashMap<>();

    public Function main;

    public Function newFunction(String name, Type retType) {
        Function function = new Function(this, name, retType);
        functions.put(name, function);
        return function;
    }

    public Var newGlobalVar(String name, Type type) {
        var globalVar = new Var(name, type, true);
        globalVars.put(name, globalVar);
        return globalVar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global Variables:\n")
                .append(globalVars.values().stream().map(Var::shortName).collect(Collectors.joining(", ")))
                .append("\nFunctions:\n")
                .append(functions.values().stream().map(Function::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
