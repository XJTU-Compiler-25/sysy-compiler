package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** BlockStmt */
public final class BlockStmt extends Stmt {
    /** 语句块 */
    public Block block;

    public BlockStmt(Token start, Token end, Block block) {
        super(start, end);
        this.block = block;
    }

    @Override
    public String toString() {
        return "BlockStmt [block=" + block + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }
}
