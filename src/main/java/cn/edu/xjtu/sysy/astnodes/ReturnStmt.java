package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** Return Stmts */
public final class ReturnStmt extends Stmt {

    public Expr value;

    public ReturnStmt(Token start, Token end, Expr value) {
        super(start, end);
        this.value = value;
    }

    @Override
    public String toString() {
        return "ReturnStmt [value=" + value + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }
}
