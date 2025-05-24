package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class GlobalVar extends Value {
    public String name;

    public GlobalVar(String name, Type type) {
        // 注意：为了在 SSA 形式中使用全局变量，是当作一块外部内存区域来看的，所以这个 “值” 的类型是一个指针
        super(Types.ptrOf(type));
        this.name = name;
    }

    @Override
    public String shallowToString() {
        return "@" + name;
    }

    public static final GlobalVar DUMMY = new GlobalVar("dummy", Types.Void);
}
