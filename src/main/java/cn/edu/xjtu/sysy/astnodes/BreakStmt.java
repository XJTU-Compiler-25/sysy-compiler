package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** BreakStmt */
public final class BreakStmt extends Stmt {

    public BreakStmt(Token start, Token end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return "BreakStmt [Location=" + Arrays.toString(getLocation()) + "]";
    }
}
