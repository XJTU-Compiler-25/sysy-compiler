package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.Symbol;

public final class PureFunctionChecker extends AstVisitor {
    public PureFunctionChecker(ErrManager errManager) {
        super(errManager);
    }

    private boolean isPure = true;

    @Override
    public void visit(Decl.FuncDef node) {
        super.visit(node);
        node.resolution.isPure = isPure;
    }

    @Override
    public void visit(Expr.Call node) {
        // 如果调用的函数不是纯函数，则当前函数也不是纯函数
        if(!node.resolution.isPure) isPure = false;

        super.visit(node);
    }

    @Override
    public void visit(Expr.VarAccess node) {
        // 对全局变量有读写都会导致函数不纯
        if(node.resolution.kind != Symbol.Var.Kind.LOCAL) isPure = false;
    }

    @Override
    public void visit(Stmt node) {
        // 函数的纯性只可能由纯到不纯，不可能由不纯到纯
        // 因此如果已经发现不纯，就不再继续遍历
        if(isPure) super.visit(node);
    }

    @Override
    public void visit(Expr node) {
        // 函数的纯性只可能由纯到不纯，不可能由不纯到纯
        // 因此如果已经发现不纯，就不再继续遍历
        if(isPure) super.visit(node);
    }
}
