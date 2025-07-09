package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.symbol.Type;

public final class BasicBlock implements User {
    public String label;

    public Function owner;
    public ArrayList<Use<BlockArgument>> arguments;
    public ArrayList<Use<Instruction>> instructions;
    public Instruction.Terminator terminator;

    // 以下都为分析用的字段
    
    /** 这个基本块是否属于一个size > 1的强连通分量 */
    public boolean isStronglyConnected;

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
        this.pred = new HashSet<>();
        this.succ = new HashSet<>();
        this.isStronglyConnected = false;
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
        instructions.add(use(terminator));
        switch (terminator) {
            case Instruction.Jmp it -> {
                this.succ.add(it.target);
                it.target.pred.add(this);
            }
            case Instruction.Br it -> {
                this.succ.add(it.trueTarget);
                it.trueTarget.pred.add(this);
                this.succ.add(it.falseTarget);
                it.falseTarget.pred.add(this);
            }
            default -> {}
        }
    }

    public List<BasicBlock> getPredBlocks() {
        return new ArrayList<>(pred);
    }

    public List<BasicBlock> getSuccBlocks() {
        return new ArrayList<>(succ);
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
