package cn.edu.xjtu.sysy.ast;

public final class UnaryExpr extends Expr {
    public final Operator op;
    public final Expr operand;

    public UnaryExpr(Operator op, Expr operand) {
        this.op = op;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", op, operand);
    }
}
