package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** AssignStmt assignableExp '=' exp ';' */
public final class AssignStmt extends Stmt {
    /** 赋值表达式的左值 */
    public final AssignableExpr target;

    /** 右值 */
    public Expr value;

    public AssignStmt(Token start, Token end, AssignableExpr target, Expr value) {
        super(start, end);
        this.target = target;
        this.value = value;
    }

    @Override
    public String toString() {
        return "AssignStmt [target="
                + target
                + ", value="
                + value
                + ", getLocation()="
                + Arrays.toString(getLocation())
                + "]";
    }
}
