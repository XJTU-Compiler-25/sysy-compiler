package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

public final class BlockArgument extends Value {
    public BasicBlock owner;
    public int index;

    BlockArgument(Type type, BasicBlock owner, int index) {
        super(type);
        this.owner = owner;
        this.index = index;
    }


    @Override
    public String shallowToString() {
        return "%arg" + index;
    }
}
