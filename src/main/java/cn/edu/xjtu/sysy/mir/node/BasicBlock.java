package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic Block 继承 Value 可以借助 Value 的 Def-Use 去便利地收集 pred 和 succ blocks
 */
public final class BasicBlock extends Value {
    public String label;

    private final Function function;
    public HashMap<Var, BlockArgument> args;
    public ArrayList<Instruction> instructions;
    public Instruction.Terminator terminator;

    // 以下都为分析用的字段

    /** 这个基本块是否属于一个size > 1的强连通分量 */
    public boolean isStronglyConnected;

    // 该块的直接支配者（支配者树上的父节点）
    public BasicBlock idom;
    // 支配边界
    public HashSet<BasicBlock> df;

    public BasicBlock(Function function) {
        this(function, null);
    }

    public BasicBlock(Function function, String label) {
        super(Types.Void);
        this.function = function;
        this.label = label;
        this.args = new HashMap<>();
        this.instructions = new ArrayList<>();
        this.isStronglyConnected = false;
    }

    public void dispose() {
        instructions.forEach(Instruction::dispose);
        terminator.dispose();
    }

    public Function getFunction() {
        return function;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void setTerminator(Instruction.Terminator terminator) {
        this.terminator = terminator;
    }

    public BlockArgument addBlockArgument(Var var) {
        var arg = new BlockArgument(this, var);
        args.put(var, arg);
        return arg;
    }

    @Override
    public String shortName() {
        return "^" + label;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(shortName()).append(" (")
                .append(args.values().stream().map(it -> it.var.name)
                        .collect(Collectors.joining(", ")))
                .append("):\n")
                .append(instructions.stream().map(it -> it.toString() + "\n")
                        .collect(Collectors.joining()));
        if (terminator != null) sb.append(terminator);
        return sb.toString();
    }

    public List<Instruction.Terminator> getPredTerminators() {
        var result = new ArrayList<Instruction.Terminator>(usedBy.size());
        for (var use : usedBy) if (use.user instanceof Instruction.Terminator term) result.add(term);
        return result;
    }

    public List<BasicBlock> getPredBlocks() {
        var result = new ArrayList<BasicBlock>(usedBy.size());
        for (var use : usedBy) if (use.user instanceof Instruction.Terminator term) result.add(term.getBlock());
        return result;
    }

    public List<BasicBlock> getSuccBlocks() {
        return switch (terminator) {
            case Instruction.Jmp jmp -> List.of(jmp.getTarget());
            case Instruction.Br br -> br.getTargets();
            case Instruction.Ret _,  Instruction.RetV _ -> List.of();
        };
    }

    public boolean dominates(BasicBlock other) {
        // 自己支配自己
        if (this == other) return true;
        // entry 不被任何块支配
        if (idom == null) return false;

        // 通过支配树判断
        while (other != null) {
            if (other == this) return true;
            other = other.idom;
        }
        return false;
    }

}
