package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;

/**
 * 识别出例如算术表达式中包含 ! 运算符等语义错误
 */
public final class ExprValidator extends AstVisitor {
    private boolean isInCondition = false;

    @Override
    public void visit(Stmt.While node) {
        isInCondition = true;
        visit(node.cond);
        isInCondition = false;
        visit(node.body);
    }

    @Override
    public void visit(Stmt.If node) {
        isInCondition = true;
        visit(node.cond);
        isInCondition = false;
        visit(node.thenStmt);
        visit(node.elseStmt);
    }

    @Override
    public void visit(Expr.Unary node) {
        if (!isInCondition && node.op == Expr.Operator.NOT)
            throw new IllegalArgumentException("! operator must in condition");
        super.visit(node);
    }
}
