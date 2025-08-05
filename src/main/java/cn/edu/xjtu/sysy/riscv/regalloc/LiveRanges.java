package cn.edu.xjtu.sysy.riscv.regalloc;

import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Value;

import java.util.Map;

public record LiveRanges(
        Map<Instruction, Value> liveBefore
) {
    public Value getLiveBefore(Instruction instruction) {
        return liveBefore.get(instruction);
    }
}
