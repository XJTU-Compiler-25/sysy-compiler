package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.mir.util.ModulePrinter;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.util.Assertions;

import java.util.Collection;
import java.util.HashMap;

public final class Module {
    public final HashMap<String, GlobalVar> globalVars = new HashMap<>();
    public final HashMap<GlobalVar, ImmediateValue> globalVarInitValues = new HashMap<>();
    public final HashMap<String, Function> functions = new HashMap<>();

    public Function main;

    public Function newFunction(String name, Type.Function funcType) {
        Function function = new Function(this, name, funcType);
        functions.put(name, function);
        return function;
    }

    // 全局变量的初值必须是常量表达式，所以直接取 compTimeValue 即可
    public GlobalVar newGlobalVar(Symbol.VarSymbol symbol, ImmediateValue init) {
        var name = symbol.name;
        var globalVar = new GlobalVar(name, symbol.type);
        globalVars.put(name, globalVar);
        globalVarInitValues.put(globalVar, init);
        return globalVar;
    }

    public void removeGlobalVar(GlobalVar var) {
        globalVars.remove(var.name);
        globalVarInitValues.remove(var);
    }

    @Override
    public String toString() {
        return ModulePrinter.toString(this);
    }

    public Collection<Function> getFunctions() {
        return functions.values();
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public Collection<GlobalVar> getGlobalVars() {
        return globalVars.values();
    }



}
