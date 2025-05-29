package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class BasicBlock extends User {
    public String label;

    public List<Use<BasicBlock, BlockArgument>> arguments;
    public List<Use<BasicBlock, Instruction>> instructions;
    public Use<BasicBlock, Instruction.Terminator> terminator;

    public BasicBlock(String label) {
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
        this.terminator = use(terminator);
    }

    @Override
    public String shortName() {
        return label;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append('(')
                .append(arguments.stream().map(it -> it.value.toString())
                        .collect(Collectors.joining(", "))).append("):\n")
                .append(instructions.stream().map(it -> it.value.toString() + "\n")
                        .collect(Collectors.joining()));
        if (terminator != null) sb.append(terminator.value.toString());
        return sb.toString();
    }

}
