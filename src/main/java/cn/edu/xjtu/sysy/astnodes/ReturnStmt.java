package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class ReturnStmt extends Stmt {

    public Expr value;

    public ReturnStmt(Token start, Token end, Expr value) {
        super(start, end);
        this.value = value;
    }
}
