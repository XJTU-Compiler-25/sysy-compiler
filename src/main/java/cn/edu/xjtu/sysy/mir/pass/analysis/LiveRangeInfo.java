package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.Map;
import java.util.Set;

import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Value;

public record LiveRangeInfo(
        // 指令执行前时哪些值是活跃的
        Map<Instruction, Set<Value>> liveIn,
        // 指令执行后哪些值是活跃的
        Map<Instruction, Set<Value>> liveOut
) {
    public Set<Value> getLiveIn(Instruction instruction) {
        return liveIn.get(instruction);
    }

    public Set<Value> getLiveOut(Instruction instruction) {
        return liveOut.get(instruction);
    }
}
