package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

public final class BlockArgument extends Value {
    // 属于哪个变量
    public BasicBlock block;

    public BlockArgument(BasicBlock block, Type type) {
        super(type);
        this.block = block;
    }

    @Override
    public final String shortName() {
        if (position == null) return "%" + id;
        else return "%" + id + "[" + position.toString() + "]";
    }

    public boolean isParam() {
        return block == block.getFunction().entry;
    }
}
