package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

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

    public void accept(AstVisitor visitor) {
        visitor.visit(cond);
        visitor.visit(bodyStmt);
    }
}
