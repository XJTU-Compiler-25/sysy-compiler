package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.ast.SemanticError;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Node;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManager;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import cn.edu.xjtu.sysy.util.Placeholder;


/**
 * 抽象类，用于遍历AST。
 * 派生类应该重写所有需要使用的方法。
 */
public abstract class AstVisitor extends Pass<CompUnit, Void> {
    public AstVisitor(ErrManager errManager) {
        super(errManager);
    }

    public void err(Node node, String msg) {
        this.errManager.err(new SemanticError(node, msg));
    }

    @Override
    public Void process(CompUnit obj) {
        visit(obj);
        return null;
    }

    public void visit(CompUnit node) {
        node.decls.forEach(this::visit);
    }

    public void visit(Decl node) {
        switch (node) {
            case Decl.FuncDef it -> visit(it);
            case Decl.VarDef it -> visit(it);
            case null, default -> unreachable();
        }
    }

    public void visit(Decl.FuncDef node) {
        node.params.forEach(this::visit);
        visit(node.body);
    }

    public void visit(Decl.VarDef node) {
        node.dimensions.forEach(this::visit);
        visit(node.init);
    }

    public void visit(Stmt node) {
        switch (node) {
            case null -> Placeholder.pass();
            case Stmt.Assign it -> visit(it);
            case Stmt.Block it -> visit(it);
            case Stmt.Break it -> visit(it);
            case Stmt.Continue it -> visit(it);
            case Stmt.ExprEval it -> visit(it);
            case Stmt.If it -> visit(it);
            case Stmt.LocalVarDef it -> visit(it);
            case Stmt.Return it -> visit(it);
            case Stmt.While it -> visit(it);
            case Stmt.Empty it -> visit(it);
            default -> unreachable();
        }
    }

    public void visit(Stmt.Empty node) { }

    public void visit(Stmt.Block node) {
        node.stmts.forEach(this::visit);
    }

    public void visit(Stmt.Return node) {
        var value = node.value;
        if (value != null) visit(node.value);
    }

    public void visit(Stmt.Assign node) {
        visit(node.target);
        visit(node.value);
    }

    public void visit(Stmt.ExprEval node) {
        visit(node.expr);
    }

    public void visit(Stmt.LocalVarDef node) {
        node.varDefs.forEach(this::visit);
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
        switch (node) {
            case null -> Placeholder.pass();
            case Expr.Assignable it -> visit(it);
            case Expr.RawArray it -> visit(it);
            case Expr.Array it -> visit(it);
            case Expr.Binary it -> visit(it);
            case Expr.Call it -> visit(it);
            case Expr.Literal it -> visit(it);
            case Expr.Unary it -> visit(it);
            case Expr.Cast it -> visit(it);
            default -> unreachable();
        }
    }

    public void visit(Expr.Binary node) {
        node.operands.forEach(this::visit);
    }

    public void visit(Expr.Unary node) {
        visit(node.operand);
    }

    public void visit(Expr.RawArray node) {
        node.elements.forEach(this::visit);
    }

    public void visit(Expr.Array node) {
        node.elements.forEach(this::visit);
    }

    public void visit(Expr.Call node) {
        node.args.forEach(this::visit);
    }

    public void visit(Expr.Assignable node) {
        switch (node) {
            case Expr.VarAccess it -> visit(it);
            case Expr.IndexAccess it -> visit(it);
            case null, default -> unreachable();
        }
    }

    public void visit(Expr.VarAccess node) { }

    public void visit(Expr.IndexAccess node) {
        visit(node.lhs);
        node.indexes.forEach(this::visit);
    }

    public void visit(Expr.Literal node) { }

    public void visit(Expr.Cast node) {
        visit(node.value);
    }

}
