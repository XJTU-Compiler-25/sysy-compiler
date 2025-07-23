package cn.edu.xjtu.sysy.riscv.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.mir.node.BlockArgument;
import cn.edu.xjtu.sysy.mir.node.Var;
import cn.edu.xjtu.sysy.symbol.Types;

public class MachineBasicBlock {
    public String label;
    public ArrayList<Instr> instructions;

    public MachineBasicBlock() {
        this(null);
    }

    public MachineBasicBlock(String label) {
        this.label = label;
    }

    public MachineBasicBlock(int label) {
        this.label = Integer.toString(label);
    }

    public void add(Instr instruction) {
        instructions.add(instruction);
    }

    public void addAll(Collection<Instr> instructions) {
        instructions.addAll(instructions);
    }

    public String shortName() {
        return ".L" + label;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(shortName())
                .append(instructions.stream().map(it -> it.toString() + "\n")
                        .collect(Collectors.joining()));
        return sb.toString();
    }
}
