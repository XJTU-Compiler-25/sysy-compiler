package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** Stmts */
public final class IfStmt extends Stmt {

    public Expr cond;
    public Stmt thenStmt;
    public Stmt elseStmt;

    public IfStmt(Token start, Token end, Expr cond, Stmt thenStmt, Stmt elseStmt) {
        super(start, end);
        this.cond = cond;
        this.elseStmt = elseStmt;
        this.thenStmt = thenStmt;
    }

    @Override
    public String toString() {
        return "IfStmt [cond=" + cond + ", thenStmt=" + thenStmt + ", elseStmt=" + elseStmt + ", getLocation()="
                + Arrays.toString(getLocation()) + "]";
    }
}
