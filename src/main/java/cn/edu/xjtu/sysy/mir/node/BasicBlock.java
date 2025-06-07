package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xjtu.sysy.util.Assertions.todo;

public final class BasicBlock implements User {
    public String label;

    public Function owner;
    public ArrayList<Use<BlockArgument>> arguments;
    public ArrayList<Use<Instruction>> instructions;
    public Instruction.Terminator terminator;

    // 以下都为分析用的字段

    // 前导块
    public HashSet<BasicBlock> pred;
    // 后继块
    public HashSet<BasicBlock> succ;

    // 该块被哪些块直接支配
    public HashSet<BasicBlock> idom;
    // 该块被哪些块支配
    public HashSet<BasicBlock> doms;
    // 支配边界
    public HashSet<BasicBlock> df;

    public BasicBlock(Function owner, String label) {
        this.owner = owner;
        this.label = label;
        this.arguments = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public BlockArgument newBlockArgument(Type type) {
        var arg = new BlockArgument(type, this, arguments.size());
        arguments.add(use(arg));
        return arg;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(use(instruction));
    }

    public void setTerminator(Instruction.Terminator terminator) {
        this.terminator = terminator;
    }

    public List<BasicBlock> getPredBlocks() {
        return todo();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('$').append(label).append('(')
                .append(arguments.stream().map(it -> it.value.toString())
                        .collect(Collectors.joining(", "))).append("):\n")
                .append(instructions.stream().map(it -> it.value.toString() + "\n")
                        .collect(Collectors.joining()));
        if (terminator != null) sb.append(terminator.toString());
        return sb.toString();
    }

}
