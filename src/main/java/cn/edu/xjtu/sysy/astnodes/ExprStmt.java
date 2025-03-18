package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** ExprStmt */
public final class ExprStmt extends Stmt {

    public Expr expr;

    public ExprStmt(Token start, Token end, Expr expr) {
        super(start, end);
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "ExprStmt [Location=" + Arrays.toString(getLocation()) + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(expr);
    }
}
