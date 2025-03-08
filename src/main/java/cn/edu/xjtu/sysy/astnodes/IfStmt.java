package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class IfStmt extends Stmt {

    public Expr cond;
    public Stmt thenStmt;
    public Stmt elseStmt;

    public IfStmt(Token start, Token end, Expr cond, Stmt thenStmt, Stmt elseStmt) {
        super(start, end);
        this.cond = cond;
        this.elseStmt = elseStmt;
        this.thenStmt = thenStmt;
    }
    
}