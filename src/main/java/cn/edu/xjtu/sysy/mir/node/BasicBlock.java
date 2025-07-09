package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic Block 继承 Value 可以借助 Value 的 Def-Use 去便利地收集 pred 和 succ blocks
 */
public final class BasicBlock extends Value {
    public String label;

    private final InstructionHelper helper = new InstructionHelper(this);
    private final Function function;
    public ArrayList<Instruction> instructions;
    public Instruction.Terminator terminator;

    // 以下都为分析用的字段

    /** 这个基本块是否属于一个size > 1的强连通分量 */
    public boolean isStronglyConnected;

    // 该块的直接支配者（支配者树上的父节点）
    public BasicBlock idom;
    // 支配边界
    public HashSet<BasicBlock> df;

    public BasicBlock(Function function, String label) {
        super(Types.Void);
        this.function = function;
        this.label = label;
        this.instructions = new ArrayList<>();
        this.isStronglyConnected = false;
    }

    public InstructionHelper getHelper() {
        return helper;
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

    @Override
    public String shortName() {
        return label;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('$').append(label).append(':')
                .append(instructions.stream().map(it -> it.toString() + "\n")
                        .collect(Collectors.joining()));
        if (terminator != null) sb.append(terminator.toString());
        return sb.toString();
    }

    public List<BasicBlock> getPredBlocks() {
        return usedBy.stream()
                .map(it -> it.user)
                .filter(it -> it instanceof Instruction.Terminator)
                .map(it -> ((Instruction.Terminator) it).getBlock())
                .toList();
    }

    public List<BasicBlock> getSuccBlocks() {
        return switch (terminator) {
            case Instruction.Jmp jmp -> List.of(jmp.target);
            case Instruction.Br br -> List.of(br.trueTarget, br.falseTarget);
            case Instruction.Ret _,  Instruction.RetV _ -> List.of();
        };
    }

}
