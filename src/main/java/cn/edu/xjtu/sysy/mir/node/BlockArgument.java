package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class BlockArgument extends Value {
    public BasicBlock owner;
    public int index;

    BlockArgument(Type type, BasicBlock owner, int index) {
        super(Types.ptrOf(type));
        this.owner = owner;
        this.index = index;
    }

    @Override
    public String shortName() {
        return "%arg" + index;
    }
}
