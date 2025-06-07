package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class Var extends Value {
    public String name;
    public boolean isGlobal;

    Var(String name, Type type, boolean isGlobal) {
        // 注意：“变量类型的值” 的类型是指针，需要 load/store 访问其值
        super(Types.ptrOf(type));
        this.name = name;
        this.isGlobal = isGlobal;
    }

    @Override
    public String shortName() {
        return isGlobal ? "@" + name : "%" + name + ".addr";
    }

    @Override
    public String toString() {
        return shortName() + " : " + type.toString();
    }
}
