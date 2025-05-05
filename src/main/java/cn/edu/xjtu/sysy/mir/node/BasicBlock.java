package cn.edu.xjtu.sysy.mir.node;

import java.util.List;

public final class BasicBlock {
    public String label;

    public List<Value> arguments;
    public List<Instruction> instructions;
    public Terminator terminator;
}
