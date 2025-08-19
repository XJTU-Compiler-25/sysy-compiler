package cn.edu.xjtu.sysy.riscv.node;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.symbol.Type;

public class MachineFunc {

    public String name;
    public Type.Function funcType;
    public ArrayList<MachineBasicBlock> blocks = new ArrayList<>();

    // 是否没有副作用
    public boolean isPure;

    public MachineFunc(String name, Type.Function type) {
        this.name = name;
        this.funcType = type;
    }

    public void addBlock(MachineBasicBlock block) {
        blocks.add(block);
    } 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb  .append("  .align 1\n")
            .append("  .globl ").append(name).append("\n")
            .append("  .text\n")
            .append("  .type ").append(name).append(", @function\n")
            .append(name).append(":\n")
            .append(blocks.stream().map(MachineBasicBlock::toString).collect(Collectors.joining()))
            .append("  .size ").append(name).append(", .-").append(name).append("\n");
        return sb.toString();
    }
}
