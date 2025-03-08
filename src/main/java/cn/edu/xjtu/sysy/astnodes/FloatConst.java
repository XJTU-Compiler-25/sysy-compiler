package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Expressions */
public class FloatConst extends Number {

    public float value;

    public FloatConst(Token start, Token end, float value) {
        super(start, end);
        this.value = value;
    }
}
