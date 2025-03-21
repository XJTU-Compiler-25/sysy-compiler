package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.SymbolTable;

public final class AstAnnotator extends AstVisitor {
    private SymbolTable.Global globalST;
    private SymbolTable currentST;

    @Override
    public void visit(CompUnit node) {
        var globalST = new SymbolTable.Global();
        currentST = globalST;
        this.globalST = globalST;
        node.globalST = globalST;

        super.visit(node);
    }

    @Override
    public void visit(Decl.FuncDef node) {
        var funcST = new SymbolTable.Local(currentST);
        currentST = funcST;

        for (var p : node.params) visit(p);
        var paramSymbols = node.params.stream().map(p -> p.resolution).toList();

        var funcSymbol = new Symbol.Func(node.name, node.retType, paramSymbols);
        globalST.declareFunc(funcSymbol);
        node.resolution = funcSymbol;
        node.symbolTable = funcST;

        visit(node.body);

        currentST = currentST.getParent();
    }

    @Override
    public void visit(Decl.VarDef node) {
        var symbol = new Symbol.Var(node.kind, node.name, node.type, node.isConst);
        currentST.declare(symbol);
        node.resolution = symbol;

        super.visit(node);
    }

    @Override
    public void visit(Stmt.Block node) {
        var blockST = new SymbolTable.Local(currentST);
        currentST = blockST;

        super.visit(node);
        node.symbolTable = blockST;

        currentST = currentST.getParent();
    }

    @Override
    public void visit(Expr.VarAccess node) {
        node.resolution = currentST.resolve(node.name);

        super.visit(node);
    }

    @Override
    public void visit(Expr.Call node) {
        node.resolution = globalST.resolveFunc(node.funcName);

        super.visit(node);
    }

}
