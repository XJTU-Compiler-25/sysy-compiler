package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** Basic Types */
public final class BType extends Node {
    
    public String className;

    public BType(Token start, Token end, String className) {
        super(start, end);
        this.className = className;
    }

    @Override
    public String toString() {
        return "BType [className=" + className + "]";
    }
}
