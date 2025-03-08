package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** ConstDecl */
public class ExprInitVal extends InitVal {

    public Expr value;

    public ExprInitVal(Token start, Token end, Expr value) {
        super(start, end);
        this.value = value;
    }
}
