package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** 声明基类 */
public abstract sealed class Decl extends Node permits VarDefs, FuncDef {
    
    private boolean isDead = false;

    public Decl(Token start, Token end) {
        super(start, end);
    }

    public void dead() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }
}
