package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Literal */
public abstract sealed class Literal extends Expr permits IntLiteral, FloatLiteral {

    public Literal(Token start, Token end) {
        super(start, end);
    }
}