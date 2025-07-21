package cn.edu.xjtu.sysy.mir.node;

public final class BlockArgument extends Value {
    // 属于哪个变量
    public final BasicBlock block;
    public final Var var;

    public BlockArgument(BasicBlock block, Var var) {
        super(var.type);
        this.block = block;
        this.var = var;
    }

    @Override
    public String shortName() {
        return "%" + var.name + "@bb" + block.order;
    }
}
