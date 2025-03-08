package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class BlockStmt extends Stmt {

    public Block block;

    public BlockStmt(Token start, Token end, Block block) {
        super(start, end);
        this.block = block;
    }

    @Override
    public String toString() {
        return "BlockStmt [block=" + block + "]";
    }
}
