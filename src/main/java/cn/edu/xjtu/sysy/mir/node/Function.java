package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class Function extends User {
    public Module module;

    public String name;
    public Type.Function funcType;
    public ArrayList<BasicBlock> blocks = new ArrayList<>();
    public BasicBlock entry;
    private int tempValueCounter = 0;
    public ArrayList<Var> localVars = new ArrayList<>();

    public Function(Module module, String name, Type.Function funcType) {
        super(Types.Void);
        this.module = module;
        this.name = name;
        this.funcType = funcType;
    }

    public Module getModule() {
        return module;
    }

    public int incTempValueCounter() {
        return tempValueCounter++;
    }

    public BasicBlock addNewBlock() {
        var block = new BasicBlock(this);
        addBlock(block);
        return block;
    }

    public String newBlockLabel() {
        return "bb" + blocks.size();
    }

    public void addBlock(BasicBlock block) {
        blocks.add(block);
        block.label = newBlockLabel();
    }

    public void removeBlock(BasicBlock block) {
        blocks.remove(block);
    }

    public Var addNewParam(String name, Type type) {
        var param = new Var(name, type, false, true);
        localVars.add(param);
        return param;
    }

    public Var addNewLocalVar(String name, Type type) {
        var localVar = new Var(name, type, false, false);
        localVars.add(localVar);
        return localVar;
    }

    @Override
    public String shortName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function ").append(name)
                .append(" (")
                .append(localVars.stream().filter(it -> it.isParam)
                        .map(v -> v.name + ": " + v.varType)
                        .collect(Collectors.joining(", ")))
                .append(") -> ").append(funcType.returnType)
                .append(" (entry = ").append(entry.label).append(", locals = {")
                .append(localVars.stream().map(it -> it.name + ": " + it.varType)
                        .collect(Collectors.joining(", ")))
                .append("}) \n")
                .append(blocks.stream().map(BasicBlock::toString)
                        .collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
