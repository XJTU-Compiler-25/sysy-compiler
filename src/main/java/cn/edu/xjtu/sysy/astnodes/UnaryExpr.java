package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** Unary Expressions */
public final class UnaryExpr extends Expr {
    /** 运算元 */
    public Expr operand;
    /** 运算符 */
    public String operator;

    private boolean isLogicalExpr = false;

    private boolean isArithExpr = false;

    public UnaryExpr(Token start, Token end, String operator, Expr operand) {
        super(start, end);
        this.operand = operand;
        this.operator = operator;
        switch (operator) {
            case "!" -> isLogicalExpr = true;
            case "+", "0" -> isArithExpr = true;
        }
    }

    @Override
    public String toString() {
        return "UnaryExpr [operand=" + operand + ", operator=" + operator + ", getLocation()="
                + Arrays.toString(getLocation()) + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(operand);
    }

    public boolean isLogicalExpr() {
        return isLogicalExpr;
    }
    
    public boolean isArithExpr() {
        return isArithExpr;
    }
}
