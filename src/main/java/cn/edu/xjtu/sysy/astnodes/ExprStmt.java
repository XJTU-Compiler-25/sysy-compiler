package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class ExprStmt extends Stmt {

    public Expr expr;

    public ExprStmt(Token start, Token end, Expr expr) {
        super(start, end);
        this.expr = expr;
    }
}
