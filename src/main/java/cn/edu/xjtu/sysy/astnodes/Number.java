package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Expressions */
public abstract class Number extends Expr {

    public Number(Token start, Token end) {
        super(start, end);
    }
}