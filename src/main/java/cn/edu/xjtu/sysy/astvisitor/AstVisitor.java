package cn.edu.xjtu.sysy.astvisitor;

import java.util.ArrayList;
import java.util.List;

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
import cn.edu.xjtu.sysy.astnodes.Node;
import cn.edu.xjtu.sysy.astnodes.Param;
import cn.edu.xjtu.sysy.astnodes.ReturnStmt;
import cn.edu.xjtu.sysy.astnodes.SemanticError;
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
public abstract class AstVisitor {

    private List<SemanticError> errors = new ArrayList<>();

    public void err(Node errNode, String errForm, Object... args) {
        errors.add(new SemanticError(errNode, String.format(errForm, args)));
    }

    public void err(List<SemanticError> errors) {
        this.errors.addAll(errors);
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

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
            return;
        } else if (node instanceof FuncDef it) {
            visit(it);
            return;
        }
        unreachable();
    }

    public void visit(ExprStmt node) {
        unreachable();
    }

    public void visit(FuncDef node) {
        unreachable();
    }

    public void visit(IfStmt node) {
        unreachable();
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
            return;
        } else if (node instanceof BlockStmt it) {
            visit(it);
            return;
        } else if (node instanceof BreakStmt it) {
            visit(it);
            return;
        } else if (node instanceof ContinueStmt it) {
            visit(it);
            return;
        } else if (node instanceof ExprStmt it) {
            visit(it);
            return;
        } else if (node instanceof IfStmt it) {
            visit(it);
            return;
        } else if (node instanceof ReturnStmt it) {
            visit(it);
            return;
        } else if (node instanceof VarDefStmt it) {
            visit(it);
            return;
        } else if (node instanceof WhileStmt it) {
            visit(it);
            return;
        }
        unreachable();
    }

    public void visit(TypeAnnotation node) {
        unreachable();
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

    public void visit(Expr node) {
        if (node instanceof AssignableExpr it) {
            visit(it);
            return;
        } else if (node instanceof ArrayExpr it) {
            visit(it);
            return;
        } else if (node instanceof BinaryExpr it) {
            visit(it);
            return;
        } else if (node instanceof CallExpr it) {
            visit(it);
            return;
        } else if (node instanceof Literal it) {
            visit(it);
            return;
        } else if (node instanceof UnaryExpr it) {
            visit(it);
            return;
        }
        unreachable();
    }

    public void visit(CallExpr node) {
        unreachable();
    }

    public void visit(BinaryExpr node) {
        unreachable();
    }

    public void visit(AssignableExpr node) {
        if (node instanceof Ident it) {
            visit(it);
            return;
        } else if (node instanceof IndexExpr it) {
            visit(it);
            return;
        }
        unreachable();
    }

    public void visit(ArrayExpr node) {
        unreachable();
    }

    public void visit(UnaryExpr node) {
        unreachable();
    }

    public void visit(IndexExpr node) {
        unreachable();
    }
    
    public void visit(IntLiteral node) {
        unreachable();
    }

    public void visit(Literal node) {
        if (node instanceof IntLiteral it) {
            visit(it);
            return;
        } else if (node instanceof FloatLiteral it) {
            visit(it);
            return;
        }
        unreachable();
    }

    public void visit(FloatLiteral node) {
        unreachable();
    }

    public void visit(Ident node) {
        unreachable();
    }
}
