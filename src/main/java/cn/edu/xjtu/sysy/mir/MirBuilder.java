package cn.edu.xjtu.sysy.mir;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;

/**
 * Middle IR Builder
 * 这个类不是线程安全的，但是好像我们也没有多线程的需求
 */
public final class MirBuilder implements ErrManaged {
    
    private final ErrManager errManager;

    public MirBuilder(ErrManager errManager) {
        this.errManager = errManager;
    }

    public MirBuilder() {
        this(ErrManager.GLOBAL);
    }

    @Override
    public ErrManager getErrManager() {
        return errManager;
    }
    
    // current building module
    private Module curMod;
    // current building function
    private Function curFunc;
    // current building basic block
    private BasicBlock curBB;

    public Module build(CompUnit node) {
        return visit(node);
    }

    public Module visit(CompUnit node) {
        var mod = new Module();
        curMod = mod;

        node.decls.forEach(this::visit);

        curMod = null;
        return mod;
    }

    public void visit(Decl node) {
        switch (node) {
            case Decl.FuncDef f -> visit(f);
            case Decl.VarDef v -> visit(v);
        }
    }

    public void visit(Decl.FuncDef node) {
        var symbol = node.resolution;

        var func = curMod.newFunction(symbol.name);
        var entryBB = func.newBlock();

        curFunc = func;
        func.entry = entryBB;
        curBB = entryBB;
    }

    public void visit(Decl.VarDef node) {
    }

    public void visit(Stmt node) {
    }

    public void visit(Stmt.Empty node) {
    }

    public void visit(Stmt.Block node) {
    }

    public void visit(Stmt.Return node) {
    }

    public void visit(Stmt.Assign node) {
    }

    public void visit(Stmt.ExprEval node) {
    }

    public void visit(Stmt.LocalVarDef node) {
    }

    public void visit(Stmt.If node) {
    }

    public void visit(Stmt.While node) {
    }

    public void visit(Stmt.Break node) {
    }

    public void visit(Stmt.Continue node) {
    }

    public void visit(Expr node) {
    }

    public void visit(Expr.Binary node) {
    }

    public void visit(Expr.Unary node) {
    }

    public void visit(Expr.Array node) {
    }

    public void visit(Expr.Call node) {
    }

    public void visit(Expr.Assignable node) {
    }

    public void visit(Expr.VarAccess node) {
    }

    public void visit(Expr.IndexAccess node) {
    }

    public void visit(Expr.Literal node) {
    }

    public void visit(Expr.Cast node) {
    }
}
