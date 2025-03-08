package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class WhileStmt extends Stmt {

    public Expr cond;
    public Stmt bodyStmt;

    public WhileStmt(Token start, Token end, Expr cond, Stmt bodyStmt) {
        super(start, end);
        this.bodyStmt = bodyStmt;
        this.cond = cond;
    }
}
