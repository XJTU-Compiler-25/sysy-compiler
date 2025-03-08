package cn.edu.xjtu.sysy.ast;

public final class BlockStmt extends Stmt {
    public final Block block;

    public BlockStmt(Block block) {
        this.block = block;
    }
}
