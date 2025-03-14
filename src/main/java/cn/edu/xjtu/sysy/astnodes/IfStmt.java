package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

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

    public void accept(AstVisitor visitor) {
        visitor.visit(cond);
        visitor.visit(thenStmt);
        if (elseStmt != null) {
            visitor.visit(elseStmt);
        }
    }
}
