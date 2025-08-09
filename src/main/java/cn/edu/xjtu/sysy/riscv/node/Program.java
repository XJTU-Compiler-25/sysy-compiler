package cn.edu.xjtu.sysy.riscv.node;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Program {
    private final HashMap<String, Global> globals = new HashMap<>();
    private final HashMap<String, MachineFunc> functions = new HashMap<>();

    @Override
    public String toString() {
        var vars =
                globals.values().stream()
                        .map(x -> x.toString())
                        .collect(Collectors.joining("\n"));
        var funcs =
                functions.values().stream()
                        .map(x -> x.toString())
                        .collect(Collectors.joining("\n"));
        return vars + "\n" + funcs;
    }

    public void addGlobl(Global global) {
        globals.put(global.name, global);
    }

    public void addFunc(MachineFunc func) {
        functions.put(func.name, func);
    }
}
