package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class Var extends Value {
    public String name;
    public boolean isGlobal;
    public boolean isParam;
    public Type varType;

    public Var(String name, Type type, boolean isGlobal, boolean isParam) {
        // 注意：“变量类型的值” 的类型是指针，需要 load/store 访问其值
        super(Types.ptrOf(type));
        this.name = name;
        this.varType = type;
        this.isGlobal = isGlobal;
        this.isParam = isParam;
    }

    @Override
    public String shortName() {
        if (isGlobal) return "%Global[" + name + "]";
        if (isParam) return "%Param[" + name + "]";
        return "%Local[" + name + "]";
    }

    @Override
    public String toString() {
        return shortName() + " : " + type.toString();
    }
}
