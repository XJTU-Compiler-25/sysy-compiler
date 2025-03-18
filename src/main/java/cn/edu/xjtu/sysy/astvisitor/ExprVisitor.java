package cn.edu.xjtu.sysy.astvisitor;

import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.AssignableExpr;
import cn.edu.xjtu.sysy.astnodes.BinaryExpr;
import cn.edu.xjtu.sysy.astnodes.CallExpr;
import cn.edu.xjtu.sysy.astnodes.Expr;
import cn.edu.xjtu.sysy.astnodes.FloatLiteral;
import cn.edu.xjtu.sysy.astnodes.Ident;
import cn.edu.xjtu.sysy.astnodes.IndexExpr;
import cn.edu.xjtu.sysy.astnodes.IntLiteral;
import cn.edu.xjtu.sysy.astnodes.Literal;
import cn.edu.xjtu.sysy.astnodes.UnaryExpr;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public abstract class ExprVisitor<T> {
    public T visit(Expr node) {
        if (node instanceof AssignableExpr it) {
            return visit(it);
        } else if (node instanceof ArrayExpr it) {
            return visit(it);
        } else if (node instanceof BinaryExpr it) {
            return visit(it);
        } else if (node instanceof CallExpr it) {
            return visit(it);
        } else if (node instanceof Literal it) {
            return visit(it);
        } else if (node instanceof UnaryExpr it) {
            return visit(it);
        }
        return unreachable();
    }

    public T visit(CallExpr node) {
        return unreachable();
    }

    public T visit(BinaryExpr node) {
        return unreachable();
    }

    public T visit(AssignableExpr node) {
        if (node instanceof Ident it) {
            return visit(it);
        } else if (node instanceof IndexExpr it) {
            return visit(it);
        }
        return unreachable();
    }

    public T visit(ArrayExpr node) {
        return unreachable();
    }
    
    public T visit(UnaryExpr node) {
        return unreachable();
    }

    public T visit(IndexExpr node) {
        return unreachable();
    }
    
    public T visit(IntLiteral node) {
        return unreachable();
    }

    public T visit(Literal node) {
        if (node instanceof IntLiteral it) {
            return visit(it);
        } else if (node instanceof FloatLiteral it) {
            return visit(it);
        }
        return unreachable();
    }

    public T visit(FloatLiteral node) {
        return unreachable();
    }

    public T visit(Ident node) {
        return unreachable();
    }
}
