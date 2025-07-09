package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

public sealed abstract class User extends Value permits BasicBlock, Function, Instruction {

    public User(Type type) {
        super(type);
    }

    public <V extends Value> Use<V> use(V value) {
        var use = new Use<>(this, value);
        value.addUse(use);
        return use;
    }

}