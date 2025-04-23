package cn.edu.xjtu.sysy.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Node;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;
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
import cn.edu.xjtu.sysy.symbol.Symbol;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

public final class AstBuilder extends SysYBaseVisitor<Node> implements ErrManaged {
    private final ErrManager errManager;

    public AstBuilder(ErrManager em) {
        errManager = em;
    }

    @Override
    public ErrManager getErrManager() {
        return errManager;
    }

    public void err(Node node, String msgForm, Object... args) {
        errManager.err(new SemanticError(node, String.format(msgForm, args)));
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

    public List<Decl.VarDef> visitVarDefs(VarDefsContext ctx, Symbol.Var.Kind kind) {
        return switch (ctx) {
            case SysYParser.ConstVarDefsContext cv ->
                    cv.varDef().stream()
                            .map(it -> visitVarDef(it, kind, cv.type.getText(), true))
                            .collect(Collectors.toList());
            case SysYParser.NormalVarDefsContext nv ->
                    nv.varDef().stream()
                            .map(it -> visitVarDef(it, kind, nv.type.getText(), false))
                            .collect(Collectors.toList());
            default -> throw new IllegalStateException("Unexpected value: " + ctx);
        };
    }

    public Decl.VarDef visitVarDef(
            SysYParser.VarDefContext ctx, Symbol.Var.Kind kind, String baseType, boolean isConst) {
        if (ctx instanceof ScalarVarDefContext scalarVar) {
            return visitScalarVarDef(scalarVar, kind, baseType, isConst);
        } else if (ctx instanceof ArrayVarDefContext arrayVar) {
            return visitArrayVarDef(arrayVar, kind, baseType, isConst);
        }
        return unreachable();
    }

    public Decl.VarDef visitScalarVarDef(
            ScalarVarDefContext ctx, Symbol.Var.Kind kind, String baseType, boolean isConst) {
        String name = ctx.name.getText();

        Expr exp = null;
        var init = ctx.initVal;
        if (init != null) exp = visitExp(init);

        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), kind, name, baseType, isConst, exp);
    }

    public Decl.VarDef visitArrayVarDef(
            ArrayVarDefContext ctx, Symbol.Var.Kind kind, String baseType, boolean isConst) {
        String name = ctx.name.getText();
        List<Expr> dimensions = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());

        Expr exp = null;
        var assignableInit = ctx.assignableExp();
        if (assignableInit != null) exp = visitAssignable(assignableInit);
        else {
            var literalInit = ctx.arrayLiteralExp();
            if (literalInit != null) exp = visitArrayLiteralExp(literalInit);
        }

        return new Decl.VarDef(
                ctx.getStart(), ctx.getStop(), kind, name, baseType, dimensions, isConst, exp);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
     * ctx}.
     */
    @Override
    public Decl.FuncDef visitFuncDef(FuncDefContext ctx) {
        List<Decl.VarDef> params =
                ctx.param().stream().map(this::visitParam).collect(Collectors.toList());
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
        if (ctx.emptyDim != null) dimensions.addFirst(new Expr.Literal(null, null, -1));
        return new Decl.VarDef(
                ctx.getStart(),
                ctx.getStop(),
                Symbol.Var.Kind.LOCAL,
                name,
                type,
                dimensions,
                false,
                null);
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
    public Stmt.Empty visitEmptyStmt(EmptyStmtContext ctx) {
        return new Stmt.Empty(ctx.getStart(), ctx.getStop());
    }

    @Override
    public Stmt.LocalVarDef visitVarDefStmt(VarDefStmtContext ctx) {
        var varDefs = visitVarDefs(ctx.varDefs(), Symbol.Var.Kind.LOCAL);
        return new Stmt.LocalVarDef(ctx.getStart(), ctx.getStop(), varDefs);
    }

    @Override
    public Stmt.Assign visitAssignmentStmt(AssignmentStmtContext ctx) {
        Expr.Assignable target = visitAssignable(ctx.target);
        Expr value = visitExp(ctx.value);
        return new Stmt.Assign(ctx.getStart(), ctx.getStop(), target, value);
    }

    @Override
    public Stmt.ExprEval visitExpStmt(ExpStmtContext ctx) {
        Expr expr = visitExp(ctx.value);
        return new Stmt.ExprEval(ctx.getStart(), ctx.getStop(), expr);
    }

    @Override
    public Stmt.Block visitBlockStmt(BlockStmtContext ctx) {
        return visitBlock(ctx.body);
    }

    @Override
    public Stmt.Return visitReturnStmt(ReturnStmtContext ctx) {
        if (ctx.value == null) return new Stmt.Return(ctx.getStart(), ctx.getStop(), null);
        Expr value = visitExp(ctx.value);
        return new Stmt.Return(ctx.getStart(), ctx.getStop(), value);
    }

    @Override
    public Stmt.If visitIfStmt(IfStmtContext ctx) {
        Expr cond = visitCond(ctx.condition);
        Stmt thenStmt = visitStmt(ctx.thenStmt);
        var es = ctx.elseStmt;
        var elseStmt = es == null ? Stmt.Empty.INSTANCE : visitStmt(ctx.elseStmt);
        return new Stmt.If(ctx.getStart(), ctx.getStop(), cond, thenStmt, elseStmt);
    }

    @Override
    public Stmt.While visitWhileStmt(WhileStmtContext ctx) {
        Expr cond = visitCond(ctx.condition);
        Stmt bodyStmt = visitStmt(ctx.body);
        return new Stmt.While(ctx.getStart(), ctx.getStop(), cond, bodyStmt);
    }

    @Override
    public Stmt.Break visitBreakStmt(BreakStmtContext ctx) {
        return new Stmt.Break(ctx.getStart(), ctx.getStop());
    }

    @Override
    public Stmt.Continue visitContinueStmt(ContinueStmtContext ctx) {
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
    public Expr visitExpCond(ExpCondContext ctx) {
        return visitExp(ctx.value);
    }

    @Override
    public Expr.Binary visitAndCond(AndCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.AND, right);
    }

    @Override
    public Expr.Binary visitOrCond(OrCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(ctx.getStart(), ctx.getStop(), left, Expr.Operator.OR, right);
    }

    @Override
    public Expr.Binary visitRelCond(RelCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(
                ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    @Override
    public Expr.Binary visitEqCond(EqCondContext ctx) {
        Expr left = visitCond(ctx.lhs);
        Expr right = visitCond(ctx.rhs);
        return new Expr.Binary(
                ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    public Expr visitExp(SysYParser.ExpContext ctx) {
        return switch (ctx) {
            case ParenExpContext it -> visitParenExp(it);
            case IntConstExpContext it -> visitIntConstExp(it);
            case FloatConstExpContext it -> visitFloatConstExp(it);
            case VarAccessExpContext it -> visitVarAccessExp(it);
            case UnaryExpContext it -> visitUnaryExp(it);
            case FuncCallExpContext it -> visitFuncCallExp(it);
            case MulExpContext it -> visitMulExp(it);
            case AddExpContext it -> visitAddExp(it);
            default -> unsupported(ctx);
        };
    }

    @Override
    public Expr visitParenExp(ParenExpContext ctx) {
        return visitExp(ctx.value);
    }

    @Override
    public Expr.Unary visitUnaryExp(UnaryExpContext ctx) {
        Expr operand = visitExp(ctx.rhs);
        return new Expr.Unary(
                ctx.getStart(), ctx.getStop(), Expr.Operator.of(ctx.op.getText()), operand);
    }

    @Override
    public Expr.Binary visitAddExp(AddExpContext ctx) {
        List<Expr> elems = new ArrayList<>();
        List<Expr.Operator> ops = new ArrayList<>();
        List<Token> ends = new ArrayList<>();

        SysYParser.ExpContext context = ctx;
        while (context instanceof AddExpContext it) {
            elems.add(visitExp(it.rhs));
            ops.add(Expr.Operator.of(it.op.getText()));
            ends.add(context.getStop());
            context = it.lhs;
        }
        Expr left = visitExp(context);
        
        for (int i = 0; i < elems.size(); i++) {
            var elem = elems.get(i);
            var op = ops.get(i);
            var end = ends.get(i);

            left = new Expr.Binary(ctx.getStart(), end, left, op, elem);
        }
        return (Expr.Binary) left;
    }

    @Override
    public Expr.Binary visitMulExp(MulExpContext ctx) {
        Expr left = visitExp(ctx.lhs);
        Expr right = visitExp(ctx.rhs);
        return new Expr.Binary(
                ctx.getStart(), ctx.getStop(), left, Expr.Operator.of(ctx.op.getText()), right);
    }

    @Override
    public Expr.Call visitFuncCallExp(FuncCallExpContext ctx) {
        String name = ctx.name.getText();
        List<Expr> args = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new Expr.Call(ctx.getStart(), ctx.getStop(), name, args);
    }

    @Override
    public Expr.Assignable visitVarAccessExp(VarAccessExpContext ctx) {
        return visitAssignable(ctx.assignableExp());
    }

    public Expr.Assignable visitAssignable(SysYParser.AssignableExpContext ctx) {
        return switch (ctx) {
            case SysYParser.ScalarAssignableContext it -> visitScalarAssignable(it);
            case SysYParser.ArrayAssignableContext it -> visitArrayAssignable(it);
            default -> unsupported(ctx);
        };
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
    public Expr.Literal visitIntConstExp(IntConstExpContext ctx) {
        String number = ctx.IntLiteral().getText();
        if (number.startsWith("0x") || number.startsWith("0X")) {
            return new Expr.Literal(
                    ctx.getStart(), ctx.getStop(), Integer.parseInt(number.substring(2), 16));
        }

        if (number.startsWith("0")) {
            return new Expr.Literal(ctx.getStart(), ctx.getStop(), Integer.parseInt(number, 8));
        }

        return new Expr.Literal(
                ctx.getStart(), ctx.getStop(), Integer.parseInt(ctx.IntLiteral().getText()));
    }

    @Override
    public Expr.Literal visitFloatConstExp(FloatConstExpContext ctx) {
        return new Expr.Literal(
                ctx.getStart(), ctx.getStop(), Float.parseFloat(ctx.FloatLiteral().getText()));
    }

    public Expr.RawArray visitArrayLiteralExp(SysYParser.ArrayLiteralExpContext ctx) {
        switch (ctx) {
            case SysYParser.ArrayExpContext it -> {
                return visitArrayExp(it);
            }
            case SysYParser.ElementExpContext it -> {
                // 第一次进入 ArrayLiteralExp 只可能是 Array，不可能是单值
                Expr expr = visitElementExp(it);
                err(expr, "invalid initializer");
                return null;
            }
            default -> {
                return unsupported(ctx);
            }
        }
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
    public Expr.RawArray visitArrayExp(SysYParser.ArrayExpContext ctx) {
        List<Expr> elements =
                ctx.arrayLiteralExp().stream()
                        .map(this::visitArrayLiteralExpRecursive)
                        .collect(Collectors.toList());
        return new Expr.RawArray(ctx.getStart(), ctx.getStop(), elements);
    }
}
