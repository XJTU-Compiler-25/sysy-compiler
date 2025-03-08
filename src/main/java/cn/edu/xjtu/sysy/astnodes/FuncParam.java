package cn.edu.xjtu.sysy.astnodes;

import java.util.List;

import org.antlr.v4.runtime.Token;

/** Function Definition */
public final class FuncParam extends Node {

    public BType type;
    public Ident id;
    public List<Expr> dimensions;

    public FuncParam(Token start, Token end, Ident id, BType type, List<Expr> dimensions) {
        super(start, end);
        this.dimensions = dimensions;
        this.id = id;
        this.type = type;
    }
}
