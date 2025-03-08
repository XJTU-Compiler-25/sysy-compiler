package cn.edu.xjtu.sysy.astnodes;

import java.util.List;

import org.antlr.v4.runtime.Token;

/** ConstDecl */
public final class VarDef extends Node {

    public Ident id;
    public List<Expr> dimensions;
    public InitVal initVal;

    public VarDef(
            Token start,
            Token end,
            Ident id,
            List<Expr> dimensions,
            InitVal initVal) {
        super(start, end);
        this.dimensions = dimensions;
        this.id = id;
        this.initVal = initVal;
    }
}
