package cn.edu.xjtu.sysy.ast;

import cn.edu.xjtu.sysy.parse.SysYBaseVisitor;
import cn.edu.xjtu.sysy.parse.SysYParser.*;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import static cn.edu.xjtu.sysy.util.Todo.todo;
import static cn.edu.xjtu.sysy.ast.Operator.*;

/**
 * 用 Visitor 模式实现的 解析树（Concrete Syntax Tree）转抽象语法树（Abstract Syntax Tree）功能
 */
public final class Cst2AstVisitor extends SysYBaseVisitor<Node> {
    @Override
    public CompUnit visitCompUnit(CompUnitContext ctx) {
        todo();

        var varDefs = ctx.varDefs().stream().map(this::visitVarDefs).flatMap(defs -> {
            // var type = defs.type
            // var vars = defs.varDefs.map(it -> new VarDef(type, it));
            // return vars
            return todo();
        });
        CompUnit result = new CompUnit();
        return result;
    }

    @Override
    public Node visitVarDefs(VarDefsContext ctx) {
        return todo();
    }

    @Override
    public Node visitScalarVarDef(ScalarVarDefContext ctx) {
        return todo();
    }

    @Override
    public Node visitArrayVarDef(ArrayVarDefContext ctx) {
        return todo();
    }

    @Override
    public Node visitFuncDef(FuncDefContext ctx) {
        return todo();
    }

    @Override
    public Node visitParam(ParamContext ctx) {
        return todo();
    }

    @Override
    public Block visitBlock(BlockContext ctx) {
        return new Block(ctx.stmt().stream().map(this::visitStmt).toArray(Stmt[]::new));
    }

    public Stmt visitStmt(StmtContext ctx) {
        if (ctx instanceof EmptyStmtContext it) {
            return visitEmptyStmt(it);
        } else if (ctx instanceof VarDefStmtContext it) {
            return visitVarDefStmt(it);
        } else if (ctx instanceof AssignmentStmtContext it) {
            return visitAssignmentStmt(it);
        } else if (ctx instanceof ExpStmtContext it) {
            return visitExpStmt(it);
        } else if (ctx instanceof BlockStmtContext it) {
            return visitBlockStmt(it);
        } else if (ctx instanceof IfStmtContext it) {
            return visitIfStmt(it);
        } else if (ctx instanceof WhileStmtContext it) {
            return visitWhileStmt(it);
        } else if (ctx instanceof BreakStmtContext it) {
            return visitBreakStmt(it);
        } else if (ctx instanceof ContinueStmtContext it) {
            return visitContinueStmt(it);
        } else if (ctx instanceof ReturnStmtContext it) {
            return visitReturnStmt(it);
        }

        return unreachable();
    }

    @Override
    public EmptyStmt visitEmptyStmt(EmptyStmtContext ctx) {
        return EmptyStmt.INSTANCE;
    }

    @Override
    public Stmt visitVarDefStmt(VarDefStmtContext ctx) {
        return todo();
    }

    @Override
    public Stmt visitAssignmentStmt(AssignmentStmtContext ctx) {
        var dest = visitAssignableExp(ctx.assignableExp());
        var value = ctx.exp();
        return todo();
    }

    @Override
    public Stmt visitExpStmt(ExpStmtContext ctx) {
        var exp = visitExp(ctx.exp());
        return todo();
    }

    @Override
    public BlockStmt visitBlockStmt(BlockStmtContext ctx) {
        return new BlockStmt(visitBlock(ctx.block()));
    }

    @Override
    public Stmt visitIfStmt(IfStmtContext ctx) {
        return todo();
    }

    @Override
    public Stmt visitWhileStmt(WhileStmtContext ctx) {
        return todo();
    }

    @Override
    public BreakStmt visitBreakStmt(BreakStmtContext ctx) {
        return BreakStmt.INSTANCE;
    }

    @Override
    public ContinueStmt visitContinueStmt(ContinueStmtContext ctx) {
        return ContinueStmt.INSTANCE;
    }

    @Override
    public Stmt visitReturnStmt(ReturnStmtContext ctx) {
        return new ReturnStmt(visitExp(ctx.exp()));
    }

    /**
     * cond 应该也由 expr 进行表示
     */
    public Expr visitCond(CondContext ctx) {
        if (ctx instanceof ExpCondContext it) {
            return visitExpCond(it);
        } else if (ctx instanceof OrCondContext it) {
            return visitOrCond(it);
        } else if (ctx instanceof RelCondContext it) {
            return visitRelCond(it);
        } else if (ctx instanceof AndCondContext it) {
            return visitAndCond(it);
        } else if (ctx instanceof EqCondContext it) {
            return visitEqCond(it);
        }

        // java 17 没法用 switch 模式匹配，检查不了穷尽性...
        return unreachable();
    }

    @Override
    public Expr visitExpCond(ExpCondContext ctx) {
        return visitExp(ctx.exp());
    }

    @Override
    public BinaryExpr visitOrCond(OrCondContext ctx) {
        var lhs = visitCond(ctx.cond(0));
        var rhs = visitCond(ctx.cond(1));
        return new BinaryExpr(OR, lhs, rhs);
    }

    @Override
    public BinaryExpr visitAndCond(AndCondContext ctx) {
        var lhs = visitCond(ctx.cond(0));
        var rhs = visitCond(ctx.cond(1));
        return new BinaryExpr(AND, lhs, rhs);
    }

    @Override
    public BinaryExpr visitRelCond(RelCondContext ctx) {
        var op = ctx.op.getText();
        var lhs = visitCond(ctx.cond(0));
        var rhs = visitCond(ctx.cond(1));
        return new BinaryExpr(byString(op), lhs, rhs);
    }

    @Override
    public BinaryExpr visitEqCond(EqCondContext ctx) {
        var op = ctx.op.getText();
        var lhs = visitCond(ctx.cond(0));
        var rhs = visitCond(ctx.cond(1));
        return new BinaryExpr(byString(op), lhs, rhs);
    }

    public Expr visitExp(ExpContext ctx) {
        todo();

        if(ctx == null) return null;
        else if (ctx instanceof IntConstExpContext ic) {
            return visitIntConstExp(ic);
        }

        // java 17 没法用 switch 模式匹配，检查不了穷尽性...
        return unreachable();
    }

    @Override
    public Expr visitParenExp(ParenExpContext ctx) {
        return visitExp(ctx.exp());
    }

    @Override
    public Node visitUnaryExp(UnaryExpContext ctx) {
        var op = ctx.op.getText();
        var operand = visitExp(ctx.exp());
        return new UnaryExpr(byString(op), operand);
    }

    @Override
    public IntConstExpr visitIntConstExp(IntConstExpContext ctx) {
        return new IntConstExpr(Integer.parseInt(ctx.IntLiteral().getText()));
    }

    @Override
    public Node visitFloatConstExp(FloatConstExpContext ctx) {
        return todo();
    }

    @Override
    public BinaryExpr visitAddExp(AddExpContext ctx) {
        var op = ctx.op.getText();
        var lhs = visitExp(ctx.exp(0));
        var rhs = visitExp(ctx.exp(1));
        return new BinaryExpr(byString(op), lhs, rhs);
    }

    @Override
    public BinaryExpr visitMulExp(MulExpContext ctx) {
        var op = ctx.op.getText();
        var lhs = visitExp(ctx.exp(0));
        var rhs = visitExp(ctx.exp(1));
        return new BinaryExpr(byString(op), lhs, rhs);
    }

    @Override
    public Node visitFuncCallExp(FuncCallExpContext ctx) {
        var funcName = ctx.Id().getText();
        var args = ctx.exp().stream().map(this::visitExp).toArray(Expr[]::new);
        return todo();
    }

    @Override
    public Expr visitVarAccessExp(VarAccessExpContext ctx) {
        return visitAssignableExp(ctx.assignableExp());
    }

    public Expr visitAssignableExp(AssignableExpContext ctx) {
        if (ctx instanceof ScalarAssignableContext it) {
            return visitScalarAssignable(it);
        } else if (ctx instanceof ArrayAssignableContext it) {
            return visitArrayAssignable(it);
        }

        // java 17 没法用 switch 模式匹配，检查不了穷尽性...
        return unreachable();
    }

    @Override
    public Expr visitScalarAssignable(ScalarAssignableContext ctx) {
        return todo();
    }

    @Override
    public Expr visitArrayAssignable(ArrayAssignableContext ctx) {
        return todo();
    }

    @Override
    public Expr visitElementExp(ElementExpContext ctx) {
        return visitExp(ctx.exp());
    }

    @Override
    public Node visitArrayExp(ArrayExpContext ctx) {
        return todo();
    }

    /**
     * 不应该调用这个方法，在转换到抽象语法树时这个节点应该被裁剪掉
     */
    @Override
    public Node visitVarType(VarTypeContext ctx) {
        return unreachable();
    }

    /**
     * 不应该调用这个方法，在转换到抽象语法树时这个节点应该被裁剪掉
     */
    @Override
    public Node visitReturnableType(ReturnableTypeContext ctx) {
        return unreachable();
    }
}
