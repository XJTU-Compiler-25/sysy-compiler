package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

/** AssignableExpr
1. LVal 表示具有左值的表达式，可以为变量或者某个数组元素。
2. 当 LVal 表示数组时，方括号个数必须和数组变量的维数相同（即定位到元
素）。
3. 当 LVal 表示单个变量时，不能出现后面的方括号。
LVal 必须是当前作用域内、该 Exp 语句之前有定义的变量或常量；对于赋值
号左边的 LVal 必须是变量。 
*/
public sealed abstract class AssignableExpr extends Expr permits Ident, IndexExpr {

    public AssignableExpr(Token start, Token end) {
        super(start, end);
    }

    @Override
    public boolean isAssignable() {
        return true;
    }
}
