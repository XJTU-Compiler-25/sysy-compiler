package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** ConstDecl */
public abstract class InitVal extends Node {

    public InitVal(Token start, Token end) {
        super(start, end);
    }
}