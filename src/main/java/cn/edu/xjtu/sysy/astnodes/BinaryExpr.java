package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** BinaryExpr
 * 二元表达式
 *  | cond op=('<' | '>' | '<=' | '>=') cond # relCond
    | cond op=('==' | '!=') cond             # eqCond
    | cond '&&' cond                         # andCond
    | cond '||' cond                         # orCond
    | exp op=('*' | '/' | '%') exp         # mulExp
    | exp op=('+' | '-') exp               # addExp

    1. Exp 在 SysY 中代表 int/float 型表达式，故它定义为 AddExp；Cond 代表条件
表达式，故它定义为 LOrExp。前者的单目运算符中不出现'!'，后者可以出现。
此外，当 Exp 作为数组维度时，必须是非负整数。
    4. SysY 中算符的优先级与结合性与 C 语言一致，在上面的 SysY 文法中已体现
出优先级与结合性的定义。
 
    */
public final class BinaryExpr extends Expr {
    /** 左运算元 */
    public Expr left;
    /** 右运算元 */
    public Expr right;
    /** 运算符 */
    public String operator;

    private boolean isArithExpr = false;
    private boolean isRelExpr = false;
    private boolean isLogicalExpr = false;

    public BinaryExpr(Token start, Token end, Expr left, String operator, Expr right) {
        super(start, end);
        this.left = left;
        this.operator = operator;
        this.right = right;
        switch (operator) {
            case "+", "-", "*", "/", "%" -> this.isArithExpr = true;
            case "&&", "||" -> this.isLogicalExpr = true;
            case "==", "!=", ">=", "<=", ">", "<" -> this.isRelExpr = true;
        }
    }

    @Override
    public String toString() {
        return "BinaryExpr [left=" + left + ", right=" + right + ", operator=" + operator + ", getLocation()="
                + Arrays.toString(getLocation()) + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(left);
        visitor.visit(right);
    }

    public boolean isArithExpr() {
        return isArithExpr;
    }

    public boolean isRelExpr() {
        return isRelExpr;
    }

    public boolean isLogicalExpr() {
        return isLogicalExpr;
    }
}
