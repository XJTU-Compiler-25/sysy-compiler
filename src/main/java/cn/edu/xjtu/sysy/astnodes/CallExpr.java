package cn.edu.xjtu.sysy.astnodes;

import java.util.List;

import org.antlr.v4.runtime.Token;

/** Call Expressions */
public class CallExpr extends Expr {

    public Ident function;
    public List<Expr> args;

    public CallExpr(Token start, Token end, Ident function, List<Expr> args) {
        super(start, end);
        this.function = function;
        this.args = args;
    }

    @Override
    public String toString() {
        return "CallExpr [function=" + function + ", args=" + args + "]";
    }
}
