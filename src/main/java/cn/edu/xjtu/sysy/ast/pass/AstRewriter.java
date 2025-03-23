package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.util.Placeholder;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

/**
 * 抽象类，用于重写AST。
 * 派生类应该重写所有需要使用的方法。
 */
public abstract class AstRewriter extends Pass<CompUnit> {
    public AstRewriter(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void process(CompUnit obj) {
        var ignored = visit(obj);
    }

    public CompUnit visit(CompUnit node) {
        node.decls = node.decls.stream().map(this::visit).toList();
        return node;
    }

    public Decl visit(Decl node) {
        if (node instanceof Decl.FuncDef it) return visit(it);
        else if (node instanceof Decl.VarDef it) return visit(it);
        else return unreachable();
    }

    public Decl.FuncDef visit(Decl.FuncDef node) {
        node.params = node.params.stream().map(this::visit).toList();
        node.body = visit(node.body);
        return node;
    }

    public Decl.VarDef visit(Decl.VarDef node) {
        node.dimensions = node.dimensions.stream().map(this::visit).toList();
        var initExpr = node.init;
        if(initExpr != null) node.init = visit(initExpr);
        return node;
    }

    public Stmt visit(Stmt node) {
        if (node instanceof Stmt.Assign it) return visit(it);
        else if (node instanceof Stmt.Block it) return visit(it);
        else if (node instanceof Stmt.Break it) return visit(it);
        else if (node instanceof Stmt.Continue it) return visit(it);
        else if (node instanceof Stmt.ExprEval it) return visit(it);
        else if (node instanceof Stmt.If it) return visit(it);
        else if (node instanceof Stmt.LocalVarDef it) return visit(it);
        else if (node instanceof Stmt.Return it) return visit(it);
        else if (node instanceof Stmt.While it) return visit(it);
        else if (node instanceof Stmt.Empty it) return visit(it);
        else return unreachable();
    }

    public Stmt visit(Stmt.Empty node) { return node; }

    public Stmt.Block visit(Stmt.Block node) {
        node.stmts = node.stmts.stream().map(this::visit).toList();
        return node;
    }

    public Stmt visit(Stmt.Return node) {
        node.value = visit(node.value);
        return node;
    }

    public Stmt visit(Stmt.Assign node) {
        node.target = visit(node.target);
        node.value = visit(node.value);
        return node;
    }

    public Stmt visit(Stmt.ExprEval node) {
        node.expr = visit(node.expr);
        return node;
    }

    public Stmt visit(Stmt.LocalVarDef node) {
        node.varDefs = node.varDefs.stream().map(this::visit).toList();
        return node;
    }

    public Stmt visit(Stmt.If node) {
        node.cond = visit(node.cond);
        node.thenStmt = visit(node.thenStmt);
        node.elseStmt = visit(node.elseStmt);
        return node;
    }

    public Stmt visit(Stmt.While node) {
        node.cond = visit(node.cond);
        node.body = visit(node.body);
        return node;
    }

    public Stmt visit(Stmt.Break node) {
        return node;
    }

    public Stmt visit(Stmt.Continue node) {
        return node;
    }

    public Expr visit(Expr node) {
        if (node instanceof Expr.Assignable it) return visit(it);
        else if (node instanceof Expr.Array it) return visit(it);
        else if (node instanceof Expr.Binary it) return visit(it);
        else if (node instanceof Expr.Call it) return visit(it);
        else if (node instanceof Expr.Literal it) return visit(it);
        else if (node instanceof Expr.Unary it) return visit(it);
        else if (node instanceof Expr.Cast it) return visit(it);
        else return unreachable();
    }

    public Expr visit(Expr.Binary node) {
        node.lhs = visit(node.lhs);
        node.rhs = visit(node.rhs);
        return node;
    }

    public Expr visit(Expr.Unary node) {
        node.rhs = visit(node.rhs);
        return node;
    }

    public Expr visit(Expr.Array node) {
        node.elements = node.elements.stream().map(this::visit).toList();
        return node;
    }

    public Expr visit(Expr.Call node) {
        node.args = node.args.stream().map(this::visit).toList();
        return node;
    }

    public Expr.Assignable visit(Expr.Assignable node) {
        if (node instanceof Expr.VarAccess it) return visit(it);
        else if (node instanceof Expr.IndexAccess it) return visit(it);
        else return unreachable();
    }

    public Expr.VarAccess visit(Expr.VarAccess node) {
        return node;
    }

    public Expr.Assignable visit(Expr.IndexAccess node) {
        node.lhs = visit(node.lhs);
        node.indexes = node.indexes.stream().map(this::visit).toList();
        return node;
    }

    public Expr visit(Expr.Literal node) {
        return node;
    }

    public Expr visit(Expr.Cast node) {
        node.value = visit(node.value);
        return node;
    }
}
