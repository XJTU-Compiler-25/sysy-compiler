package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

public final class GlobalVar extends Value {
    public int label;

    public GlobalVar(int label, Type type) {
        super(type);
        this.label = label;
    }

    @Override
    public String shallowToString() {
        return "@" + label;
    }
}
