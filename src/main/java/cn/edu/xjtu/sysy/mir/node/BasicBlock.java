package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class BasicBlock {
    public String label;

    public List<BlockArgument> arguments;
    public List<Instruction> instructions;
    public Terminator terminator;

    public BasicBlock(String label) {
        this.label = label;
        this.arguments = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public void addArgument(BlockArgument argument) {
        arguments.add(argument);
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void setTerminator(Terminator terminator) {
        this.terminator = terminator;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append('(')
                .append(arguments.stream().map(BlockArgument::shallowToString).collect(Collectors.joining(", ")))
                .append("):\n")
                .append(instructions.stream().map(it -> it.toString() + "\n").collect(Collectors.joining()));
        if (terminator != null) sb.append(terminator);
        return sb.toString();
    }

}
