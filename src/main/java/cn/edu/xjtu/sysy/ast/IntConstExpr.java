package cn.edu.xjtu.sysy.ast;

public final class IntConstExpr extends Expr {
    public final int value;

    public IntConstExpr(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntConstExprNode{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntConstExpr) {
            return ((IntConstExpr) obj).value == value;
        }
        return false;
    }
}
