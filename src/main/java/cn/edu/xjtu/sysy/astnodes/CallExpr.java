package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

/** Call Expressions
 * 函数调用形式是 Ident ‘(’ FuncRParams ‘)’，其中的 FuncRParams 表示实际参
数。实际参数的类型和个数必须与 Ident 对应的函数定义的形参完全匹配。
 */
public final class CallExpr extends Expr {

    public Ident function;
    public List<Expr> args;

    public CallExpr(Token start, Token end, Ident function, List<Expr> args) {
        super(start, end);
        this.function = function;
        this.args = args;
    }

    @Override
    public String toString() {
        return "CallExpr [function=" + function + ", args=" + args + ", getLocation()=" + Arrays.toString(getLocation())
                + "]";
    }

}
