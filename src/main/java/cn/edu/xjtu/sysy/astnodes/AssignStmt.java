package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

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
        return "AssignStmt [Location="
                + Arrays.toString(getLocation())
                + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(target);
        visitor.visit(value);
    }
}
