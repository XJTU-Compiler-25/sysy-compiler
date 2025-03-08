package cn.edu.xjtu.sysy.ast;

public final class BinaryExpr extends Expr {
    public final Operator op;
    public final Expr lhs, rhs;

    public BinaryExpr(Operator op, Expr lhs, Expr rhs) {
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", lhs, op, rhs);
    }
}
