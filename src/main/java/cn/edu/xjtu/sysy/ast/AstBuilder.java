package cn.edu.xjtu.sysy.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.error.Err;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.Symbol;
import org.antlr.v4.runtime.tree.TerminalNode;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Node;
import cn.edu.xjtu.sysy.ast.node.SemanticError;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.parse.SysYBaseVisitor;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.parse.SysYParser.AddExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.AndCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ArrayVarDefContext;
import cn.edu.xjtu.sysy.parse.SysYParser.AssignmentStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.BlockStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.BreakStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ContinueStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.EmptyStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.EqCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ExpCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ExpStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.FloatConstExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.FuncCallExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.FuncDefContext;
import cn.edu.xjtu.sysy.parse.SysYParser.IfStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.IntConstExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.MulExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.OrCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ParenExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.RelCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ReturnStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ScalarVarDefContext;
import cn.edu.xjtu.sysy.parse.SysYParser.UnaryExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.VarAccessExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.VarDefStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.VarDefsContext;
import cn.edu.xjtu.sysy.parse.SysYParser.WhileStmtContext;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public class AstBuilder extends SysYBaseVisitor<Node> {
    private ErrManager errManager = new ErrManager();
    
    public void err(Node node, String msgForm, Object... args) {
        errManager.add(new SemanticError(node, String.format(msgForm, args)));
    }

    public boolean hasError() {
        return errManager.hasErr();
    }

    @Override
    public CompUnit visitCompUnit(SysYParser.CompUnitContext ctx) {
        List<Decl> decls = new ArrayList<>();

        for (var node : ctx.children) {
            if (node instanceof VarDefsContext decl) {
                decls.addAll(this.visitVarDefs(decl, Symbol.Var.Kind.GLOBAL));
            } else if (node instanceof FuncDefContext funcDef) {
                decls.add(visitFuncDef(funcDef));
            }
        }

        return new CompUnit(ctx.getStart(), ctx.getStop(), decls);
    }

    public List<Decl.VarDef> visitVarDefs(SysYParser.VarDefsContext ctx, Symbol.Var.Kind kind) {
        var baseType = ctx.type.getText();
        var isConst = ctx.constmark != null;

        return ctx.varDef().stream().map(it ->
                visitVarDef(it, kind, baseType, isConst)).collect(Collectors.toList());
    }

    public Decl.VarDef visitVarDef(SysYParser.VarDefContext ctx, Symbol.Var.Kind kind, String baseType, boolean isConst) {
        if (ctx instanceof ScalarVarDefContext scalarVar) {
            return visitScalarVarDef(scalarVar, kind, baseType, isConst);
        } else if (ctx instanceof ArrayVarDefContext arrayVar) {
            return visitArrayVarDef(arrayVar, kind, baseType, isConst);
        }
        return unreachable();
    }

    public Decl.VarDef visitScalarVarDef(SysYParser.ScalarVarDefContext ctx, Symbol.Var.Kind kind, String baseType, boolean isConst) {
        String name = ctx.name.getText();

        Expr exp = null;
        var init = ctx.initVal;
        if (init != null) exp = visitExp(init);

        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), kind, name, baseType, isConst, exp);
    }

    public Decl.VarDef visitArrayVarDef(SysYParser.ArrayVarDefContext ctx, Symbol.Var.Kind kind, String baseType, boolean isConst) {
        String name = ctx.name.getText();
        List<Expr> dimensions = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());

        Expr exp = null;
        var assignableInit = ctx.assignableExp();
        if(assignableInit != null) exp = visitAssignable(assignableInit);
        else {
            var literalInit = ctx.arrayLiteralExp();
            if(literalInit != null) exp = visitArrayLiteralExp(literalInit);
        }

        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), kind, name, baseType, dimensions, isConst, exp);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
     */
    @Override
    public Decl.FuncDef visitFuncDef(SysYParser.FuncDefContext ctx) {
        List<Decl.VarDef> params = ctx.param().stream().map(this::visitParam).collect(Collectors.toList());
        String name = ctx.name.getText();
        String retType = ctx.retType.getText();
        Stmt.Block body = visitBlock(ctx.body);
        return new Decl.FuncDef(ctx.getStart(), ctx.getStop(), name, retType, params, body);
    }

    @Override
    public Decl.VarDef visitParam(SysYParser.ParamContext ctx) {
        String name = ctx.name.getText();
        String type = ctx.type.getText();
        List<Expr> dimensions = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        // 有空第一维，在最前加上 -1 项
        if(ctx.emptyDim != null) dimensions.addFirst(new Expr.IntLiteral(null, null, -1));
        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), Symbol.Var.Kind.LOCAL,  name, type, dimensions, false, null);
    }

    @Override
    public Stmt.Block visitBlock(SysYParser.BlockContext ctx) {
        List<Stmt> stmts = ctx.stmt().stream().map(this::visitStmt).collect(Collectors.toList());
        return new Stmt.Block(ctx.getStart(), ctx.getStop(), stmts);
    }

    public Stmt visitStmt(SysYParser.StmtContext ctx) {
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
    public Stmt.Empty visitEmptyStmt(SysYParser.EmptyStmtContext ctx) {
        return new Stmt.Empty(ctx.getStart(), ctx.getStop());
    }

    @Override
    public Stmt.LocalVarDef visitVarDefStmt(SysYParser.VarDefStmtContext ctx) {
        var varDefs = visitVarDefs(ctx.varDefs(), Symbol.Var.Kind.LOCAL);
        return new Stmt.LocalVarDef(ctx.getStart(), ctx.getStop(), varDefs);
    }

    @Override
    public Stmt.Assign visitAssignmentStmt(SysYParser.AssignmentStmtContext ctx) {
        Expr.Assignable target = visitAssignable(ctx.target);
        Expr value = visitExp(ctx.value);
        return new Stmt.Assign(ctx.getStart(), ctx.getStop(), target, value);
    }

    @Override
    public Stmt.ExprEval visitExpStmt(SysYParser.ExpStmtContext ctx) {
        Expr expr = visitExp(ctx.value);
        return new Stmt.ExprEval(ctx.getStart(), ctx.getStop(), expr);
    }

    @Override
    public Stmt.Block visitBlockStmt(SysYParser.BlockStmtContext ctx) {
        return visitBlock(ctx.body);
    }

    @Override
    public Stmt.Return visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        Expr value = visitExp(ctx.value);
        return new Stmt.Return(ctx.getStart(), ctx.getStop(), value);
    }

    @Override
    public Stmt.If visitIfStmt(SysYParser.IfStmtContext ctx) {
        Expr cond = visitCond(ctx.condition);
        Stmt thenStmt = visitStmt(ctx.thenStmt);
        var es = ctx.elseStmt;
        Stmt elseStmt = es == null ? Stmt.Empty.INSTANCE : visitStmt(ctx.elseStmt);
        return new Stmt.If(ctx.getStart(), ctx.getStop(), cond, thenStmt, elseStmt);
    }

    @Override
    public Stmt.While visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        Expr cond = visitCond(ctx.condition);
        Stmt bodyStmt = visitStmt(ctx.body);
        return new Stmt.While(ctx.getStart(), ctx.getStop(), cond, bodyStmt);
    }

    @Override
    public Stmt.Break visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        return new Stmt.Break(ctx.getStart(), ctx.getStop());
    }

    @Override
    public Stmt.Continue visitContinueStmt(SysYParser.ContinueStmtContext ctx) {
        return new Stmt.Continue(ctx.getStart(), ctx.getStop());
    }

    public Expr visitCond(SysYParser.CondContext ctx) {
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
        // 确实（悲
        return unreachable();
    }

    @Override
    public Expr visitExpCond(SysYParser.ExpCondContext ctx) {
        return visitExp(ctx.value);
    }

    @Override
    public Expr.Binary visitAndCond(SysYParser.AndCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.AND, right);
    }

    @Override
    public Expr.Binary visitOrCond(SysYParser.OrCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.OR, right);
    }

    @Override
    public Expr.Binary visitRelCond(SysYParser.RelCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    @Override
    public Expr.Binary visitEqCond(SysYParser.EqCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    public Expr visitExp(SysYParser.ExpContext ctx) {
        if (ctx instanceof ParenExpContext it) {
            return visitParenExp(it);
        } else if (ctx instanceof IntConstExpContext it) {
            return visitIntConstExp(it);
        } else if (ctx instanceof FloatConstExpContext it) {
            return visitFloatConstExp(it);
        } else if (ctx instanceof VarAccessExpContext it) {
            return visitVarAccessExp(it);
        } else if (ctx instanceof UnaryExpContext it) {
            return visitUnaryExp(it);
        } else if (ctx instanceof FuncCallExpContext it) {
            return visitFuncCallExp(it);
        } else if (ctx instanceof MulExpContext it) {
            return visitMulExp(it);
        } else if (ctx instanceof AddExpContext it) {
            return visitAddExp(it);
        }
        return unreachable();
    }

    @Override
    public Expr visitParenExp(SysYParser.ParenExpContext ctx) {
        return visitExp(ctx.value);
    }

    @Override
    public Expr.Unary visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        Expr operand = visitExp(ctx.rhs);
        return new Expr.Unary(ctx.getStart(), ctx.getStop(), Expr.Operator.of(ctx.op.getText()), operand);
    }

    @Override
    public Expr.Binary visitAddExp(SysYParser.AddExpContext ctx) {
        Expr left = visitExp(ctx.lhs);
        Expr right = visitExp(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    @Override
    public Expr.Binary visitMulExp(SysYParser.MulExpContext ctx) {
        Expr left = visitExp(ctx.lhs);
        Expr right = visitExp(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    @Override
    public Expr.Call visitFuncCallExp(SysYParser.FuncCallExpContext ctx) {
        String name = ctx.name.getText();
        List<Expr> args = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new Expr.Call(ctx.getStart(), ctx.getStop(), name, args);
    }

    @Override
    public Expr.Assignable visitVarAccessExp(SysYParser.VarAccessExpContext ctx) {
        return visitAssignable(ctx.assignableExp());
    }

    public Expr.Assignable visitAssignable(SysYParser.AssignableExpContext ctx) {
        if (ctx instanceof SysYParser.ScalarAssignableContext it) {
            return visitScalarAssignable(it);
        } else if (ctx instanceof SysYParser.ArrayAssignableContext it) {
            return visitArrayAssignable(it);
        }
        return unreachable();
    }

    @Override
    public Expr.VarAccess visitScalarAssignable(SysYParser.ScalarAssignableContext ctx) {
        return new Expr.VarAccess(ctx.getStart(), ctx.getStop(), ctx.name.getText());
    }

    @Override
    public Expr.IndexAccess visitArrayAssignable(SysYParser.ArrayAssignableContext ctx) {
        var nameToken = ctx.name;
        var varAccess = new Expr.VarAccess(nameToken, nameToken, nameToken.getText());
        List<Expr> indexes = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new Expr.IndexAccess(ctx.getStart(), ctx.getStop(), varAccess, indexes);
    }

    @Override
    public Expr.IntLiteral visitIntConstExp(SysYParser.IntConstExpContext ctx) {
        return new Expr.IntLiteral(ctx.getStart(), ctx.getStop(), Integer.parseInt(ctx.IntLiteral().getText()));
    }

    @Override
    public Expr.FloatLiteral visitFloatConstExp(SysYParser.FloatConstExpContext ctx) {
        return new Expr.FloatLiteral(ctx.getStart(), ctx.getStop(), Float.parseFloat(ctx.FloatLiteral().getText()));
    }

    public Expr.Array visitArrayLiteralExp(SysYParser.ArrayLiteralExpContext ctx) {
        if (ctx instanceof SysYParser.ArrayExpContext it) {
            return visitArrayExp(it);
        } else if (ctx instanceof SysYParser.ElementExpContext it) {
            // 第一次进入 ArrayLiteralExp 只可能是 Array，不可能是单值
            Expr expr = visitElementExp(it);
            err(expr, "invalid initializer");
            return null;
        }
        return unreachable();
    }

    public Expr visitArrayLiteralExpRecursive(SysYParser.ArrayLiteralExpContext ctx) {
        if (ctx instanceof SysYParser.ElementExpContext it) {
            return visitElementExp(it);
        } else if (ctx instanceof SysYParser.ArrayExpContext it) {
            return visitArrayExp(it);
        }
        return unreachable();
    }

    @Override
    public Expr visitElementExp(SysYParser.ElementExpContext ctx) {
        return visitExp(ctx.value);
    }

    @Override
    public Expr.Array visitArrayExp(SysYParser.ArrayExpContext ctx) {
        List<Expr> elements =
                ctx.arrayLiteralExp().stream()
                        .map(this::visitArrayLiteralExpRecursive)
                        .collect(Collectors.toList());
        return new Expr.Array(ctx.getStart(), ctx.getStop(), elements);
    }
}
