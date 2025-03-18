package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** Return Stmts */
public final class ReturnStmt extends Stmt {

    public Expr value;

    public ReturnStmt(Token start, Token end, Expr value) {
        super(start, end);
        this.value = value;
    }

    @Override
    public String toString() {
        return "ReturnStmt [Location=" + Arrays.toString(getLocation()) + "]";
    }

    public void accept(AstVisitor visitor) {
        if (value != null) {
            visitor.visit(value);
        }
    }
}
