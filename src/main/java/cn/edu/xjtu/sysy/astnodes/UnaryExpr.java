package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Expressions */
public class UnaryExpr extends Expr {

    public Expr operand;
    public String operator;

    public UnaryExpr(Token start, Token end, String operator, Expr operand) {
        super(start, end);
        this.operand = operand;
        this.operator = operator;
    }
}
