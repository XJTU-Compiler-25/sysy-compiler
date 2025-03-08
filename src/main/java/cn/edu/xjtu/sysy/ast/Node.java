package cn.edu.xjtu.sysy.ast;

public sealed class Node permits Block, CompUnit, Expr, Stmt {
    public String toString() {
        return getClass().getSimpleName();
    }
}
