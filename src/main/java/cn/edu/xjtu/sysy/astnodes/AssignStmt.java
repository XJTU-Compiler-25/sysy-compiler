package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class AssignStmt extends Stmt {

    @Override
    public String toString() {
        return "AssignStmt [lVal=" + lVal + ", value=" + value + "]";
    }

    public Expr lVal;
    public Expr value;

    public AssignStmt(Token start, Token end, Expr lVal, Expr value) {
        super(start, end);
        this.lVal = lVal;
        this.value = value;
    }
}
