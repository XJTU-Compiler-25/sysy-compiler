package cn.edu.xjtu.sysy.mir.node;

public final class BlockArgument extends User {
    // 属于哪个变量
    public final BasicBlock block;
    private final Use<Var> var;

    public BlockArgument(BasicBlock block, Var var) {
        super(var.type);
        this.block = block;
        this.var = use(var);
    }

    @Override
    public String shortName() {
        return "%" + getVar().name + "@" + block.order;
    }

    public Var getVar() {
        return var.value;
    }
}
