package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Value;

import java.util.Map;
import java.util.Set;

public record LiveRangeInfo(
        Map<Instruction, Set<Value>> valuesLiveBeforeInst,
        Map<Instruction, Set<Value>> valuesLiveAfterInst,
        Map<Value, Set<Instruction>> instsValueLiveBefore
) {
    public Set<Value> getLiveBefore(Instruction instruction) {
        return valuesLiveBeforeInst.get(instruction);
    }

    public Set<Value> getLiveAfter(Instruction instruction) {
        return valuesLiveAfterInst.get(instruction);
    }

    public Set<Instruction> getInstsLiveBefore(Instruction instruction) {
        return instsValueLiveBefore.get(instruction);
    }

}
