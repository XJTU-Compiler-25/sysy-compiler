package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** Expressions */
public final class IndexExpr extends AssignableExpr {

    public Ident id;
    public List<Expr> indexes;

    public IndexExpr(Token start, Token end, Ident id, List<Expr> indexes) {
        super(start, end);
        this.id = id;
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        return "IndexExpr [id=" + id + ", indexes=" + indexes + ", getLocation()=" + Arrays.toString(getLocation())
                + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(id);
        for (Expr index : indexes) {
            visitor.visit(index);
        }
    }
}
