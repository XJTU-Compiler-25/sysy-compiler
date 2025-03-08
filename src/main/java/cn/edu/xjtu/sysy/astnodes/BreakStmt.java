package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public class BreakStmt extends Stmt {

    public BreakStmt(Token start, Token end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return "BreakStmt []";
    }
}
