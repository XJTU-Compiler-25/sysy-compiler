package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.ast.node.*;
import cn.edu.xjtu.sysy.error.ErrManager;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

/**
 * 抽象类，用于遍历AST。
 * 派生类应该重写所有需要使用的方法。
 */
public abstract class AstVisitor extends Pass<CompUnit> {
    public AstVisitor(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void process(CompUnit obj) {
        visit(obj);
    }

    public void visit(CompUnit node) {
        for (var d : node.decls) visit(d);
    }

    public void visit(Decl node) {
        if (node instanceof Decl.FuncDef it) visit(it);
        else if (node instanceof Decl.VarDef it) visit(it);

        else unreachable();
    }

    public void visit(Decl.FuncDef node) {
        for (var p : node.params) visit(p);
        visit(node.body);
    }

    public void visit(Decl.VarDef node) {
        for(var d : node.dimensions) visit(d);
        visit(node.init);
    }

    public void visit(Stmt node) {
        if (node instanceof Stmt.Assign it) visit(it);
        else if (node instanceof Stmt.Block it) visit(it);
        else if (node instanceof Stmt.Break it) visit(it);
        else if (node instanceof Stmt.Continue it) visit(it);
        else if (node instanceof Stmt.ExprEval it) visit(it);
        else if (node instanceof Stmt.If it) visit(it);
        else if (node instanceof Stmt.LocalVarDef it) visit(it);
        else if (node instanceof Stmt.Return it) visit(it);
        else if (node instanceof Stmt.While it) visit(it);

        else unreachable();
    }

    public void visit(Stmt.Block node) {
        for (var s : node.stmts) visit(s);
    }

    public void visit(Stmt.Return node) {
        visit(node.value);
    }

    public void visit(Stmt.Assign node) {
        visit(node.target);
        visit(node.value);
    }

    public void visit(Stmt.ExprEval node) {
        visit(node.expr);
    }

    public void visit(Stmt.LocalVarDef node) {
        for(var v : node.varDefs) visit(v);
    }

    public void visit(Stmt.If node) {
        visit(node.cond);
        visit(node.thenStmt);
        visit(node.elseStmt);
    }

    public void visit(Stmt.While node) {
        visit(node.cond);
        visit(node.body);
    }

    public void visit(Stmt.Break node) { }

    public void visit(Stmt.Continue node) { }

    public void visit(Expr node) {
        if (node instanceof Expr.Assignable it) visit(it);
        else if (node instanceof Expr.Array it) visit(it);
        else if (node instanceof Expr.Binary it) visit(it);
        else if (node instanceof Expr.Call it) visit(it);
        else if (node instanceof Expr.Literal it) visit(it);
        else if (node instanceof Expr.Unary it) visit(it);
        else if (node instanceof Expr.Cast it) visit(it);

        else unreachable();
    }

    public void visit(Expr.Binary node) {
        visit(node.lhs);
        visit(node.rhs);
    }

    public void visit(Expr.Unary node) {
        visit(node.rhs);
    }

    public void visit(Expr.Array node) {
        for (var e : node.elements) visit(e);
    }

    public void visit(Expr.Call node) {
        for (var a : node.args) visit(a);
    }

    public void visit(Expr.Assignable node) {
        if (node instanceof Expr.VarAccess it) visit(it);
        else if (node instanceof Expr.IndexAccess it) visit(it);

        else unreachable();
    }

    public void visit(Expr.VarAccess node) { }

    public void visit(Expr.IndexAccess node) {
        visit(node.lhs);
        for (var i : node.indexes) visit(i);
    }

    public void visit(Expr.Literal node) { }

    public void visit(Expr.Cast node) {
        visit(node.value);
    }
}
