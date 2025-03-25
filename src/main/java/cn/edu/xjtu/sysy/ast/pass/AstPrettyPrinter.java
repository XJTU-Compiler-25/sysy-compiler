package cn.edu.xjtu.sysy.ast.pass;

import java.io.PrintStream;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;

public final class AstPrettyPrinter extends AstVisitor {
    private final PrintStream output;
    private int indentLevel = 0;

    public AstPrettyPrinter(PrintStream output) {
        super(null);
        this.output = output;
    }

    public AstPrettyPrinter() {
        this(System.out);
    }

    @Override
    public void visit(CompUnit node) {
        println("[CompUnit | decls = {");
        incIndent();
        for (var decl : node.decls) visit(decl);
        decIndent();
    }

    @Override
    public void visit(Decl.FuncDef node) {
        printf("[Decl.FuncDef | name = %s, returnType = %s, params = {\n", node.name, node.retType);
        incIndent();
        for (var param : node.params) visit(param);
        decIndent();
        println("}, body = ");
        incIndent();
        visit(node.body);
        decIndent();
    }

    @Override
    public void visit(Decl.VarDef node) {
        var symbol = node.resolution;
        printf("[Decl.VarDef | name = %s, type = %s, isConst = %b, initExpr = \n",
                symbol.name, symbol.type, symbol.isConst);
        incIndent();
        var initExpr = node.init;
        if (initExpr != null) visit(initExpr);
        decIndent();
        printf(", comptimeValue = %s]\n", symbol.comptimeValue);
    }

    @Override
    public void visit(Stmt.Block node) {
        println("[Stmt.Block | stmts = {");
        incIndent();
        for (var stmt : node.stmts) visit(stmt);
        decIndent();
        println("}]");
    }

    @Override
    public void visit(Stmt.Return node) {
        println("[Stmt.Return | retValue = ");
        incIndent();
        visit(node.value);
        decIndent();
        println("]");
    }

    @Override
    public void visit(Stmt.Assign node) {
        println("[Stmt.Assign | target = ");
        incIndent();
        visit(node.target);
        decIndent();
        println(", value =");
        incIndent();
        visit(node.value);
        decIndent();
        println("]");
    }

    @Override
    public void visit(Stmt.ExprEval node) {
        println("[Stmt.ExprEval | expr = ");
        incIndent();
        visit(node.expr);
        decIndent();
    }

    @Override
    public void visit(Stmt.LocalVarDef node) {
        println("[Stmt.LocalVarDef | varDefs = {");
        incIndent();
        for (var def : node.varDefs) visit(def);
        decIndent();
        println("}]");
    }

    @Override
    public void visit(Stmt.If node) {
        println("[Stmt.If | cond = ");
        incIndent();
        visit(node.cond);
        decIndent();
        println(", thenStmt = ");
        incIndent();
        visit(node.thenStmt);
        decIndent();
        println(", elseStmt = ");
        incIndent();
        visit(node.elseStmt);
        decIndent();
    }

    @Override
    public void visit(Stmt.While node) {
        println("[Stmt.While | cond = ");
        incIndent();
        visit(node.cond);
        decIndent();
        println(", body = ");
        incIndent();
        visit(node.body);
        decIndent();
    }

    @Override
    public void visit(Stmt.Break node) {
        println("[Stmt.Break]");
    }

    @Override
    public void visit(Stmt.Continue node) {
        println("[Stmt.Continue]");
    }

    @Override
    public void visit(Expr.Binary node) {
        printf("[Expr.Binary | type = %s, comptimeValue = %s, op = %s, lhs = \n", node.type, node.comptimeValue, node.op);
        incIndent();
        visit(node.lhs);
        decIndent();
        println(", rhs = ");
        incIndent();
        visit(node.rhs);
        decIndent();
        println("]");
    }

    @Override
    public void visit(Expr.Unary node) {
        printf("[Expr.Unary | type = %s, comptimeValue = %s, op = %s, rhs = \n", node.type, node.comptimeValue, node.op);
        incIndent();
        visit(node.rhs);
        decIndent();
        println("]");
    }

    @Override
    public void visit(Expr.Array node) {
        printf("[Expr.Array | type = %s, comptimeValue = %s, elements = {\n", node.type, node.comptimeValue);
        incIndent();
        for (var element : node.elements) visit(element);
        decIndent();
        println("}]");
    }

    @Override
    public void visit(Expr.NormalizedArray node) {
        printf("[Expr.Array | type = %s, comptimeValue = %s, elements = {\n", node.type, node.comptimeValue);
        incIndent();
        for (var element : node.elements) visit(element);
        decIndent();
        println("}]");
    }

    @Override
    public void visit(Expr.Call node) {
        var symbol = node.resolution;
        printf("[Expr.Call | funcName = %s, type = %s, comptimeValue = %s, args = {\n", node.type, node.comptimeValue, symbol.name);
        incIndent();
        for (var arg : node.args) visit(arg);
        decIndent();
        println("}]");
    }

    @Override
    public void visit(Expr.VarAccess node) {
        var symbol = node.resolution;
        printf("[Expr.VarAccess | varName = %s, type = %s, comptimeValue = %s]\n", symbol.name, node.type, node.comptimeValue);
    }

    @Override
    public void visit(Expr.IndexAccess node) {
        printf("[Expr.IndexAccess | type = %s, comptimeValue = %s, lhs = \n", node.type, node.comptimeValue);
        incIndent();
        visit(node.lhs);
        decIndent();
        println(", indexes = {");
        incIndent();
        for (var index : node.indexes) visit(index);
        decIndent();
        println("}]");
    }

    @Override
    public void visit(Expr.Literal node) {
        printf("[Expr.Literal | type = %s, comptimeValue = %s]\n", node.type, node.comptimeValue);
    }

    @Override
    public void visit(Expr.Cast node) {
        printf("[Expr.Cast | type = %s, comptimeValue = %s, target = \n", node.type, node.comptimeValue);
    }

    // tool functions

    private void printf(String format, Object... args) {
        printIndent();
        output.printf(format, args);
    }

    private void println(String str) {
        printIndent();
        output.println(str);
    }

    private void printIndent() {
        for (int i = 0; i < indentLevel; i++) {
            output.print("  ");
        }
    }

    private void incIndent() {
        indentLevel++;
    }

    private void decIndent() {
        indentLevel--;
    }

}
