package cn.edu.xjtu.sysy.ast;

public final class ReturnStmt extends Stmt {
    public final Expr retVal;

    public ReturnStmt(Expr retVal) {
        this.retVal = retVal;
    }
}
