package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Value;

import java.util.Map;
import java.util.Set;

public record LiveRangeInfo(
        // 指令执行前时哪些值是活跃的
        Map<Instruction, Set<Value>> liveIn,
        // 指令执行后哪些值是活跃的
        Map<Instruction, Set<Value>> liveOut,
        Map<Value, Set<Instruction>> instsValueLiveBefore
) {
    public Set<Value> getLiveIn(Instruction instruction) {
        return liveIn.get(instruction);
    }

    public Set<Value> getLiveOut(Instruction instruction) {
        return liveOut.get(instruction);
    }

    public Set<Instruction> getInstsLiveBefore(Value value) {
        return instsValueLiveBefore.get(value);
    }
}
