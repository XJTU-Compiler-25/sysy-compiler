package cn.edu.xjtu.sysy.ast;

public final class EmptyStmt extends Stmt {
    private EmptyStmt() {}

    public static final EmptyStmt INSTANCE = new EmptyStmt();
}
