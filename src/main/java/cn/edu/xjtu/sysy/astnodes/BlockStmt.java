package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

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

    public void accept(AstVisitor visitor) {
        visitor.visit(block);
    }
}
