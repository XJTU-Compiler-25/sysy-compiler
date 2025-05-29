package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public sealed abstract class User extends Value permits BasicBlock, Instruction {

    public User() {
        this(Types.Void);
    }

    public User(Type type) {
        super(type);
    }

    public final Use use(Value value) {
        var use = new Use<>(this, value);
        value.addUse(use);
        return use;
    }

}
