package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** Type Annotation */
public final class TypeAnnotation extends Node {
    
    public String name;

    public TypeAnnotation(Token start, Token end, String name) {
        super(start, end);
        this.name = name;
    }

    public boolean isVoid() {
        return name.equals("void");
    }

    @Override
    public String toString() {
        return "TypeAnnotation [Location=" + Arrays.toString(getLocation()) + "]";
    }
}
