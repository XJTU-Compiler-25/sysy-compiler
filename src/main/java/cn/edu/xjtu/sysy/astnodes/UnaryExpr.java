package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** Unary Expressions */
public final class UnaryExpr extends Expr {
    /** 运算元 */
    public Expr operand;
    /** 运算符 */
    public String operator;

    public UnaryExpr(Token start, Token end, String operator, Expr operand) {
        super(start, end);
        this.operand = operand;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "UnaryExpr [operand=" + operand + ", operator=" + operator + ", getLocation()="
                + Arrays.toString(getLocation()) + "]";
    }
}
