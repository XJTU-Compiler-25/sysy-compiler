package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashMap;
import java.util.stream.Collectors;

public final class Module {
    public final HashMap<String, Var> globalVars = new HashMap<>();
    public final HashMap<Var, ImmediateValue> globalVarInitValues = new HashMap<>();
    public final HashMap<String, Function> functions = new HashMap<>();

    public Function main;

    public Function newFunction(String name, Type.Function funcType) {
        Function function = new Function(this, name, funcType);
        functions.put(name, function);
        return function;
    }

    // 全局变量的初值必须是常量表达式，所以直接取 compTimeValue 即可
    public Var newGlobalVar(Symbol.VarSymbol symbol, ImmediateValue init) {
        var globalVar = new Var(symbol.name, symbol.type, true, false);
        globalVars.put(symbol.name, globalVar);
        globalVarInitValues.put(globalVar, init);
        return globalVar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global Variables:\n")
                .append(globalVars.values().stream().map(it -> it.shortName() + " = " + globalVarInitValues.get(it))
                        .collect(Collectors.joining(", ")))
                .append("\nFunctions:\n")
                .append(functions.values().stream().map(Function::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
