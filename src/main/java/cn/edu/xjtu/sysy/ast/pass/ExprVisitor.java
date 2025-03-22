package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.Expr;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public abstract class ExprVisitor extends AstVisitor {
    public T visit(Expr node) {
        if (node instanceof Expr.Assignable it) {
            return visit(it);
        } else if (node instanceof Expr.Array it) {
            return visit(it);
        } else if (node instanceof Expr.Binary it) {
            return visit(it);
        } else if (node instanceof Expr.Call it) {
            return visit(it);
        } else if (node instanceof Expr.Literal it) {
            return visit(it);
        } else if (node instanceof Expr.Unary it) {
            return visit(it);
        }
        return unreachable();
    }

    public T visit(Expr.Assignable node) {
        if (node instanceof Expr.VarAccess it) {
            return visit(it);
        } else if (node instanceof Expr.IndexAccess it) {
            return visit(it);
        }
        return unreachable();
    }

    public T visit(Expr.Literal node) {
        if (node instanceof Expr.IntLiteral it) {
            return visit(it);
        } else if (node instanceof Expr.FloatLiteral it) {
            return visit(it);
        }
        return unreachable();
    }
}
