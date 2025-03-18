package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Stmts */
public abstract class Stmt extends Node {
    private boolean isDead = false;

    public Stmt(Token start, Token end) {
        super(start, end);
    }

    public void dead() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }
}
