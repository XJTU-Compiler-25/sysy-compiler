package cn.edu.xjtu.sysy.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.ast.node.*;
import cn.edu.xjtu.sysy.error.*;
import cn.edu.xjtu.sysy.parse.*;
import cn.edu.xjtu.sysy.parse.SysYParser.*;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import static cn.edu.xjtu.sysy.util.Assertions.unsupported;
import cn.edu.xjtu.sysy.util.Placeholder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AstBuilder extends SysYBaseVisitor<Node> implements ErrManaged {
    private final ErrManager errManager;

    public AstBuilder(ErrManager em) {
        errManager = em;
    }

    public AstBuilder() {
        this(ErrManager.GLOBAL);
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
            switch (node) {
                case VarDefsContext decl ->
                        decls.addAll(this.visitVarDefs(decl, true));
                case FuncDefContext funcDef -> decls.add(visitFuncDef(funcDef));
                default -> Placeholder.pass();
            }
        }

        return new CompUnit(ctx.getStart(), ctx.getStop(), decls);
    }

    public List<Decl.VarDef> visitVarDefs(VarDefsContext ctx, boolean isGlobal) {
        return switch (ctx) {
            case SysYParser.ConstVarDefsContext cv ->
                    cv.varDef().stream()
                            .map(it -> visitVarDef(it, isGlobal, cv.type.getText(), true))
                            .collect(Collectors.toList());
            case SysYParser.NormalVarDefsContext nv ->
                    nv.varDef().stream()
                            .map(it -> visitVarDef(it, isGlobal, nv.type.getText(), false))
                            .collect(Collectors.toList());
            default -> throw new IllegalStateException("Unexpected value: " + ctx);
        };
    }

    public Decl.VarDef visitVarDef(
            SysYParser.VarDefContext ctx, boolean isGlobal, String baseType, boolean isConst) {
        return switch (ctx) {
            case ScalarVarDefContext scalarVar ->
                    visitScalarVarDef(scalarVar, isGlobal, baseType, isConst);
            case ArrayVarDefContext arrayVar -> visitArrayVarDef(arrayVar, isGlobal, baseType, isConst);
            default -> unreachable();
        };
    }

    public Decl.VarDef visitScalarVarDef(
            ScalarVarDefContext ctx, boolean isGlobal, String baseType, boolean isConst) {
        String name = ctx.name.getText();

        Expr exp = null;
        var init = ctx.initVal;
        if (init != null) exp = visitExp(init);

        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), name, isGlobal, baseType, isConst, exp);
    }

    public Decl.VarDef visitArrayVarDef(ArrayVarDefContext ctx, boolean isGlobal, String baseType, boolean isConst) {
        String name = ctx.name.getText();
        List<Expr> dimensions = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());

        Expr exp = null;
        var assignableInit = ctx.assignableExp();
        if (assignableInit != null) exp = visitAssignable(assignableInit);
        else {
            var literalInit = ctx.arrayLiteralExp();
            if (literalInit != null) exp = visitArrayLiteralExp(literalInit);
        }

        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), name, isGlobal, baseType, dimensions, isConst, exp);
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
        return new Decl.VarDef(ctx.getStart(), ctx.getStop(), name, false, type, dimensions, false, null);
    }

    @Override
    public Stmt.Block visitBlock(SysYParser.BlockContext ctx) {
        List<Stmt> stmts = ctx.stmt().stream().map(this::visitStmt).collect(Collectors.toList());
        return new Stmt.Block(ctx.getStart(), ctx.getStop(), stmts);
    }

    public Stmt visitStmt(SysYParser.StmtContext ctx) {
        return switch (ctx) {
            case EmptyStmtContext it -> visitEmptyStmt(it);
            case VarDefStmtContext it -> visitVarDefStmt(it);
            case AssignmentStmtContext it -> visitAssignmentStmt(it);
            case ExpStmtContext it -> visitExpStmt(it);
            case BlockStmtContext it -> visitBlockStmt(it);
            case IfStmtContext it -> visitIfStmt(it);
            case WhileStmtContext it -> visitWhileStmt(it);
            case BreakStmtContext it -> visitBreakStmt(it);
            case ContinueStmtContext it -> visitContinueStmt(it);
            case ReturnStmtContext it -> visitReturnStmt(it);
            default -> unreachable();
        };
    }

    @Override
    public Stmt.Empty visitEmptyStmt(EmptyStmtContext ctx) {
        return new Stmt.Empty(ctx.getStart(), ctx.getStop());
    }

    @Override
    public Stmt.LocalVarDef visitVarDefStmt(VarDefStmtContext ctx) {
        return new Stmt.LocalVarDef(ctx.getStart(), ctx.getStop(), visitVarDefs(ctx.varDefs(), false));
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
        return visitOrExp(ctx.orExp());
    }

    @Override
    public Expr visitOrExp(OrExpContext ctx) {
        if (ctx.Or().isEmpty()) return visitAndExp(ctx.andExp().getFirst());
        else {
            var opers = ctx.andExp().stream().map(this::visitAndExp).collect(Collectors.toList());
            var ops = new ArrayList<>(Collections.nCopies(opers.size() - 1, Expr.Operator.OR));
            return new Expr.Binary(ctx.getStart(), ctx.getStop(), ops, opers);
        }
    }

    @Override
    public Expr visitAndExp(AndExpContext ctx) {
        if (ctx.And().isEmpty()) return visitEqExp(ctx.eqExp().getFirst());
        else {
            var opers = ctx.eqExp().stream().map(this::visitEqExp).collect(Collectors.toList());
            var ops = new ArrayList<>(Collections.nCopies(opers.size() - 1, Expr.Operator.AND));
            return new Expr.Binary(ctx.getStart(), ctx.getStop(), ops, opers);
        }
    }

    @Override
    public Expr visitEqExp(EqExpContext ctx) {
        if (ctx.eqOp().isEmpty()) return visitRelExp(ctx.relExp().getFirst());
        else {
            var opers = ctx.relExp().stream().map(this::visitRelExp).collect(Collectors.toList());
            var ops = ctx.eqOp().stream().map(ParserRuleContext::getText).map(Expr.Operator::of).collect(Collectors.toList());
            return new Expr.Binary(ctx.getStart(), ctx.getStop(), ops, opers);
        }
    }

    @Override
    public Expr visitRelExp(RelExpContext ctx) {
        if (ctx.relOp().isEmpty()) return visitExp(ctx.exp().getFirst());
        else {
            var opers = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
            var ops = ctx.relOp().stream().map(ParserRuleContext::getText).map(Expr.Operator::of).collect(Collectors.toList());
            return new Expr.Binary(ctx.getStart(), ctx.getStop(), ops, opers);
        }
    }

    public Expr visitExp(SysYParser.ExpContext ctx) {
        return visitAddExp(ctx.addExp());
    }

    @Override
    public Expr visitAddExp(AddExpContext ctx) {
        if (ctx.addOp().isEmpty()) return visitMulExp(ctx.mulExp().getFirst());
        else {
            var opers = ctx.mulExp().stream().map(this::visitMulExp).collect(Collectors.toList());
            var ops = ctx.addOp().stream().map(ParserRuleContext::getText).map(Expr.Operator::of).collect(Collectors.toList());
            return new Expr.Binary(ctx.getStart(), ctx.getStop(), ops, opers);
        }
    }

    @Override
    public Expr visitMulExp(MulExpContext ctx) {
        if (ctx.mulOp().isEmpty()) return visitUnaryExp(ctx.unaryExp().getFirst());
        else {
            var opers = ctx.unaryExp().stream().map(this::visitUnaryExp).collect(Collectors.toList());
            var ops = ctx.mulOp().stream().map(ParserRuleContext::getText).map(Expr.Operator::of).collect(Collectors.toList());
            return new Expr.Binary(ctx.getStart(), ctx.getStop(), ops, opers);
        }
    }

    @Override
    public Expr visitUnaryExp(UnaryExpContext ctx) {
        if (ctx.unaryOp().isEmpty()) return visitPrimaryExp(ctx.primaryExp());
        else {
            var operand = visitPrimaryExp(ctx.primaryExp());
            // reverse 是为了保证原有顺序运算， +-a = (+, (-, a))
            return new Expr.Unary(ctx.getStart(), ctx.getStop(),
                    ctx.unaryOp().reversed().stream().map(ParserRuleContext::getText).map(Expr.Operator::of).toList(), operand);
        }
    }

    public Expr visitPrimaryExp(PrimaryExpContext ctx) {
        return switch (ctx) {
            case IntConstExpContext it -> visitIntConstExp(it);
            case FloatConstExpContext it -> visitFloatConstExp(it);
            case ParenExpContext it -> visitParenExp(it);
            case FuncCallExpContext it -> visitFuncCallExp(it);
            case VarAccessExpContext it -> visitVarAccessExp(it);
            default -> unsupported(ctx);
        };
    }

    @Override
    public Expr.Call visitFuncCallExp(FuncCallExpContext ctx) {
        String name = ctx.name.getText();
        List<Expr> args = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new Expr.Call(ctx.getStart(), ctx.getStop(), name, args);
    }

    @Override
    public Expr visitParenExp(ParenExpContext ctx) {
        return visitExp(ctx.value);
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
        var text = ctx.IntLiteral().getText();
        int value = 0;
        if (text.equals("0")) Placeholder.pass();
        else if (text.startsWith("0x") || text.startsWith("0X"))
            value = Integer.parseInt(text.substring(2), 16);
        else if (text.startsWith("0")) value = Integer.parseInt(text.substring(1), 8);
        else value = Integer.parseInt(text, 10);

        return new Expr.Literal(ctx.getStart(), ctx.getStop(), value);
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
        return switch (ctx) {
            case SysYParser.ElementExpContext it -> visitElementExp(it);
            case SysYParser.ArrayExpContext it -> visitArrayExp(it);
            default -> unreachable();
        };
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
