package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Expressions */
public class BinaryExpr extends Expr {

    public Expr left;
    public Expr right;
    public String operator;

    public BinaryExpr(Token start, Token end, Expr left, String operator, Expr right) {
        super(start, end);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "BinaryExpr [left=" + left + ", right=" + right + ", operator=" + operator + "]";
    }
}
