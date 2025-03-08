package cn.edu.xjtu.sysy.astnodes;

import java.util.List;

import org.antlr.v4.runtime.Token;

/** Expressions */
public class IndexExpr extends Expr {

    public Ident id;
    public List<Expr> dimensions;

    public IndexExpr(Token start, Token end, Ident id, List<Expr> dimensions) {
        super(start, end);
        this.id = id;
        this.dimensions = dimensions;
    }
}
