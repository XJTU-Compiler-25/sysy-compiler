package cn.edu.xjtu.sysy.mir.node;

public sealed interface User permits BasicBlock, Function, Instruction {

    default Use use(Value value) {
        var use = new Use<>(this, value);
        value.addUse(use);
        return use;
    }

}
