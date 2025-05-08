package cn.edu.xjtu.sysy.mir.node;

import java.util.HashMap;

public final class Module {
    public final HashMap<String, Void> globalVars = new HashMap<>();
    public final HashMap<String, Function> functions = new HashMap<>();

    public Function main;

    public Function newFunction(String name) {
        Function function = new Function();
        functions.put(name, function);
        return function;
    }
}
