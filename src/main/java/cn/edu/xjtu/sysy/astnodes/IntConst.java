package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Expressions */
public class IntConst extends Number {

    public int value;

    public IntConst(Token start, Token end, int value) {
        super(start, end);
        this.value = value;
    }
}
