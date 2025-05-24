package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class Function {
    public String name;
    public Type returnType;
    public BasicBlock entry;
    public final ArrayList<BasicBlock> blocks = new ArrayList<>();

    public Function(String name, Type returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public BasicBlock newBlock() {
        var block = new BasicBlock("block" + blocks.size());
        blocks.add(block);
        return block;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function ").append(name).append("(type = ").append(returnType)
                .append(", entryBlock = ").append(entry.label).append("):\n")
                .append(blocks.stream().map(BasicBlock::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
