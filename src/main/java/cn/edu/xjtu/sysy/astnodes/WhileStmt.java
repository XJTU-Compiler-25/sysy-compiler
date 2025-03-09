package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** WhileStmt */
public final class WhileStmt extends Stmt {

    public Expr cond;
    public Stmt bodyStmt;

    public WhileStmt(Token start, Token end, Expr cond, Stmt bodyStmt) {
        super(start, end);
        this.bodyStmt = bodyStmt;
        this.cond = cond;
    }

    @Override
    public String toString() {
        return "WhileStmt [cond=" + cond + ", bodyStmt=" + bodyStmt + ", getLocation()="
                + Arrays.toString(getLocation()) + "]";
    }
}
