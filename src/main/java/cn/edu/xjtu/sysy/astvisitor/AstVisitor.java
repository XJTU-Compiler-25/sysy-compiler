package cn.edu.xjtu.sysy.astvisitor;

import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.AssignStmt;
import cn.edu.xjtu.sysy.astnodes.AssignableExpr;
import cn.edu.xjtu.sysy.astnodes.BinaryExpr;
import cn.edu.xjtu.sysy.astnodes.Block;
import cn.edu.xjtu.sysy.astnodes.BlockStmt;
import cn.edu.xjtu.sysy.astnodes.BreakStmt;
import cn.edu.xjtu.sysy.astnodes.CallExpr;
import cn.edu.xjtu.sysy.astnodes.CompUnit;
import cn.edu.xjtu.sysy.astnodes.ContinueStmt;
import cn.edu.xjtu.sysy.astnodes.Decl;
import cn.edu.xjtu.sysy.astnodes.Expr;
import cn.edu.xjtu.sysy.astnodes.ExprStmt;
import cn.edu.xjtu.sysy.astnodes.FloatLiteral;
import cn.edu.xjtu.sysy.astnodes.FuncDef;
import cn.edu.xjtu.sysy.astnodes.Ident;
import cn.edu.xjtu.sysy.astnodes.IfStmt;
import cn.edu.xjtu.sysy.astnodes.IndexExpr;
import cn.edu.xjtu.sysy.astnodes.IntLiteral;
import cn.edu.xjtu.sysy.astnodes.Literal;
import cn.edu.xjtu.sysy.astnodes.Param;
import cn.edu.xjtu.sysy.astnodes.ReturnStmt;
import cn.edu.xjtu.sysy.astnodes.Stmt;
import cn.edu.xjtu.sysy.astnodes.TypeAnnotation;
import cn.edu.xjtu.sysy.astnodes.UnaryExpr;
import cn.edu.xjtu.sysy.astnodes.VarDef;
import cn.edu.xjtu.sysy.astnodes.VarDefStmt;
import cn.edu.xjtu.sysy.astnodes.VarDefs;
import cn.edu.xjtu.sysy.astnodes.WhileStmt;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

/** 抽象类，用于遍历AST。
 *  派生类应该重写所有需要使用的方法。
 */
public abstract class AstVisitor<T> {
    public void visit(CompUnit node) {
        unreachable();
    }

    public void visit(AssignStmt node) {
        unreachable();
    }

    public void visit(Block node) {
        unreachable();
    }

    public void visit(BlockStmt node) {
        unreachable();
    }

    public void visit(BreakStmt node) {
        unreachable();
    }

    public void visit(ContinueStmt node) {
        unreachable();
    }

    public void visit(Decl node) {
        if (node instanceof VarDefs it) {
            visit(it);
        } else if (node instanceof FuncDef it) {
            visit(it);
        }
        unreachable();
    }

    public void visit(ExprStmt node) {
        unreachable();
    }

    public T visit(FloatLiteral node) {
        return unreachable();
    }

    public void visit(FuncDef node) {
        unreachable();
    }

    public T visit(Ident node) {
        return unreachable();
    }

    public void visit(IfStmt node) {
        unreachable();
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

    public void visit(Param node) {
        unreachable();
    }

    public void visit(ReturnStmt node) {
        unreachable();
    }

    public void visit(Stmt node) {
        if (node instanceof AssignStmt it) {
            visit(it);
        } else if (node instanceof BlockStmt it) {
            visit(it);
        } else if (node instanceof BreakStmt it) {
            visit(it);
        } else if (node instanceof ContinueStmt it) {
            visit(it);
        } else if (node instanceof ExprStmt it) {
            visit(it);
        } else if (node instanceof IfStmt it) {
            visit(it);
        } else if (node instanceof ReturnStmt it) {
            visit(it);
        } else if (node instanceof VarDefStmt it) {
            visit(it);
        } else if (node instanceof WhileStmt it) {
            visit(it);
        }
        unreachable();
    }

    public void visit(TypeAnnotation node) {
        unreachable();
    }

    public T visit(UnaryExpr node) {
        return unreachable();
    }

    public void visit(VarDef node) {
        unreachable();
    }

    public void visit(VarDefs node) {
        unreachable();
    }

    public void visit(VarDefStmt node) {
        unreachable();
    }

    public void visit(WhileStmt node) {
        unreachable();
    }

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
}
