package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class Function implements User {
    public String name;
    public Type returnType;
    public ArrayList<BasicBlock> blocks = new ArrayList<>();
    public BasicBlock entry;

    // 下面都是用于分析的信息
    public ArrayList<Use<Var>> localVars = new ArrayList<>();

    public Function(String name, Type returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public BasicBlock addNewBlock(String label) {
        var block = new BasicBlock(this, label);
        blocks.add(block);
        return block;
    }

    public BasicBlock newBlock(String label) {
        return new BasicBlock(this, label);
    }

    public void addBlock(BasicBlock block) {
        blocks.add(block);
    }

    public void removeBlock(BasicBlock block) {
        blocks.remove(block);
    }

    public Var addNewLocalVar(String name, Type type) {
        var localVar = new Var(name, type, false);
        localVars.add(use(localVar));
        return localVar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function ").append(name).append("(type = ").append(returnType)
                .append(", entryBlock = ").append(entry.label).append("):\nLocal Vars:\n")
                .append(localVars.stream().map(it -> it.value.shortName())
                        .collect(Collectors.joining(", ")))
                .append("\nBlocks:\n")
                .append(blocks.stream().map(BasicBlock::toString)
                        .collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
