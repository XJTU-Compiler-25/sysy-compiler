package cn.edu.xjtu.sysy.mir.pass.transform.regalloc;

import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.riscv.ValuePosition;

import java.util.Map;

public record AllocatedResult(
        Map<Value, ValuePosition> allocated
) {
    public ValuePosition getPosition(Value value) {
        return allocated.get(value);
    }


}
