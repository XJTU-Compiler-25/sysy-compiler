package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public abstract class Stmt extends Node {

    public Stmt(Token start, Token end) {
        super(start, end);
    }
}
