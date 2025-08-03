package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

public final class BlockArgument extends User {
    // 属于哪个变量
    public BasicBlock block;
    public Type type;

    public BlockArgument(BasicBlock block, Type type) {
        super(type);
        this.block = block;
        this.type = type;
    }

    @Override
    public String shortName() {
        return "%" + id;
    }

    public boolean isParam() {
        return block == block.getFunction().entry;
    }
}
