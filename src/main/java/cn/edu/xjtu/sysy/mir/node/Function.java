package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class Function {
    public String name;
    public Type returnType;
    public ArrayList<Var> localVars = new ArrayList<>();
    public ArrayList<BasicBlock> blocks = new ArrayList<>();
    public BasicBlock entry;

    public Function(String name, Type returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public BasicBlock addNewBlock(String label) {
        var block = new BasicBlock(label);
        blocks.add(block);
        return block;
    }

    public void addBlock(BasicBlock block) {
        blocks.add(block);
    }

    public Var addNewLocalVar(String name, Type type) {
        var localVar = new Var(name, type, false);
        localVars.add(localVar);
        return localVar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function ").append(name).append("(type = ").append(returnType)
                .append(", entryBlock = ").append(entry.label).append("):\nLocal Vars:\n")
                .append(localVars.stream().map(Var::shortName).collect(Collectors.joining(", ")))
                .append("\nBlocks:\n")
                .append(blocks.stream().map(BasicBlock::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
