package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Identifiers */
public final class Ident extends Expr {

    public String name;

    public Ident(Token start, Token end, String name) {
        super(start, end);
        this.name = name;
    }
    
}