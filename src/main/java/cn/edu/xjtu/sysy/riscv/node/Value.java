package cn.edu.xjtu.sysy.riscv.node;

import cn.edu.xjtu.sysy.symbol.Type;


public abstract sealed class Value permits Register, Global {

    public Type type;

    public Value(Type type) {
        this.type = type;
    }
}
