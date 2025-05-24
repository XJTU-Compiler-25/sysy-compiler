package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashSet;

public abstract sealed class Value permits BlockArgument, ImmediateValue, GlobalVar, Instruction {
    public Type type;
    public final HashSet<Use> usedBy = new HashSet<>();

    public Value(Type type) {
        this.type = type;
    }

    /**
     * 浅的字符串表示，如指令只返回其 label，只有数字字面量完全返回其表示
     */
    public abstract String shallowToString();

    @Override
    public String toString() {
        return shallowToString();
    }
}
