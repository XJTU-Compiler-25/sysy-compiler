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
    public String shortName() {
        return "%" + id;
    }

    public boolean isParam() {
        return block == block.getFunction().entry;
    }
}
