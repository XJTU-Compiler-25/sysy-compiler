package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Expressions */
public abstract class Expr extends Node {

    public Expr(Token start, Token end) {
        super(start, end);
    }
}
