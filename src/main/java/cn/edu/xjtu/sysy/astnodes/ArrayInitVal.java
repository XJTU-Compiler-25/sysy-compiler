package cn.edu.xjtu.sysy.astnodes;

import java.util.List;

import org.antlr.v4.runtime.Token;

/** ConstDecl */
public class ArrayInitVal extends InitVal {

    public List<InitVal> elements;

    public ArrayInitVal(Token start, Token end, List<InitVal> elements) {
        super(start, end);
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "ArrayInitVal [elements=" + elements + "]";
    }
}
