package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class GlobalVar extends Value {
    public String name;
    public Type varType;

    GlobalVar(String name, Type type) {
        // 注意：“变量类型的值” 的类型是指针，需要 load/store 访问其值
        super(Types.ptrOf(type));
        this.name = name;
        this.varType = type;
    }

    @Override
    public String shortName() {
        return "%Global[" + name + "]";
    }

}
