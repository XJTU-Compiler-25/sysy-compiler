package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** ContinueStmt */
public final class ContinueStmt extends Stmt {

    public ContinueStmt(Token start, Token end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return "ContinueStmt [Location=" + Arrays.toString(getLocation()) + "]";
    }
}
