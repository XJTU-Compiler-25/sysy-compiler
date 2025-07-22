package cn.edu.xjtu.sysy.ast.pass;

import static java.lang.Integer.max;
import java.util.Stack;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

/** 计算栈帧大小以及每个变量在栈帧的相对位置 */
public class StackCalculator extends AstVisitor {
    SymbolTable currentST = null;
    Symbol.FuncSymbol currentFunc = null;
    int currentMx = 0;
    
    public StackCalculator() { super(null); }

    @Override
    public void visit(CompUnit node) {
        currentST = node.globalST;
        node.decls.forEach(this::visit);
    }

    @Override
    public void visit(Decl.FuncDef node) {
        currentMx = 0;
        currentST = node.symbolTable;
        currentFunc = node.resolution;
        visit(node.body, 8);
        node.resolution.localSize = currentMx;
        currentST = currentST.getParent(); 
    }

    private void updateMax(int nt) {
        currentMx = max(currentMx, nt);
    }

    @Override
    public void visit(Decl.VarDef node) {}

    public int visit(Decl.VarDef node, int nt) {
        updateMax(nt);
        node.resolution.addr = nt + node.resolution.type.size - 4;
        if (node.init != null) visit(node.init, nt + node.resolution.type.size - 4);
        return node.resolution.type.size; 
    }

    public int visit(Stmt node, int nt) {
        updateMax(nt);
        return switch (node) {
            case Stmt.Assign it -> visit(it, nt);
            case Stmt.Block it -> visit(it, nt);
            case Stmt.Break _ -> nt;
            case Stmt.Continue _ -> nt;
            case Stmt.ExprEval it -> visit(it, nt);
            case Stmt.If it -> visit(it, nt);
            case Stmt.LocalVarDef it -> visit(it, nt);
            case Stmt.Return it -> visit(it, nt);
            case Stmt.While it -> visit(it, nt);
            case Stmt.Empty _ -> nt;
            default -> unreachable();
        };
    }

    public int visit(Stmt.Block node, int nt) {
        updateMax(nt);
        currentST = node.symbolTable;
        int m = nt;
        for (var stmt : node.stmts) {
            m = visit(stmt, m);
        }
        currentST = currentST.getParent();
        return nt;
    }

    public int visit(Stmt.Assign node, int nt) {
        updateMax(nt);
        visit(node.target, nt);
        visit(node.value, Symbol.FuncSymbol.align(nt, 8) + 16);
        return nt;
    }

    public int visit(Stmt.Return node, int nt) {
        updateMax(nt);
        var value = node.value;
        if (value != null) visit(node.value, nt);
        return nt;
    }

    public int visit(Stmt.ExprEval node, int nt) {
        updateMax(nt);
        visit(node.expr, nt);
        return nt;
    }

    public int visit(Stmt.LocalVarDef node, int nt) {
        updateMax(nt);
        int m = 0;
        for (var varDef : node.varDefs) {
            m += visit(varDef, nt+m);
        }
        return nt+m;
    }

    public int visit(Stmt.If node, int nt) {
        updateMax(nt);
        visit(node.cond, nt);
        visit(node.thenStmt, nt);
        visit(node.elseStmt, nt);
        return nt;
    }

    public int visit(Stmt.While node, int nt) {
        updateMax(nt);
        visit(node.cond, nt);
        visit(node.body, nt);
        return nt;
    }

    public int visit(Expr node, int nt) {
        updateMax(nt);
        return switch (node) {
            case Expr.Assignable it -> visit(it, nt);
            case Expr.Array it -> visit(it, nt);
            case Expr.Binary it -> visit(it, nt);
            case Expr.Call it -> visit(it, nt);
            case Expr.Literal _ -> nt;
            case Expr.Unary it -> visit(it, nt);
            case Expr.Cast it -> visit(it, nt);
            default -> unreachable();
        };
    }

    public int visit(Expr.Binary node, int nt) {
        updateMax(nt);
        var opers = node.operands;
        var ops = node.operators;
        var first = opers.getFirst();
        visit(first, nt);
        for (var oper : opers) visit(oper, nt + 4);
        return nt;
    }

    public int visit(Expr.Unary node, int nt) {
        updateMax(nt);
        visit(node.operand, nt);
        return nt;
    }
    
    public int visit(Expr.Array node, int nt) {
        updateMax(nt);
        node.elements.forEach(e -> visit(e, nt));
        return nt;
    }

    public int visit(Expr.Call node, int nt) {
        updateMax(nt);
        var resolution = node.resolution;
        currentFunc.raSave = true;
        currentFunc.outSize = max(currentFunc.outSize, resolution.inSize);
        int m = nt;
        for (var arg : node.args) {
            visit(arg, m);
            if (arg.type instanceof Type.Int || arg.type instanceof Type.Float) m += 4;
            else m = Symbol.FuncSymbol.align(m, 8) + 16;
        }
        return nt;
    }

    public int visit(Expr.Assignable node, int nt) {
        updateMax(nt);
        return switch (node) {
            case Expr.VarAccess _ -> nt;
            case Expr.IndexAccess it -> visit(it, nt);
            case null, default -> unreachable();
        };
    }

    public int visit(Expr.IndexAccess node, int nt) {
        updateMax(nt);
        visit(node.lhs, nt);
        visit(node.indexes.get(0), nt+4);
        if (node.indexes.size() > 1) {
            for (int i = 1; i < node.indexes.size(); i++) {
                visit(node.indexes.get(i), Symbol.FuncSymbol.align(nt+4, 8) + 16);
            }
        }
        return nt;
    }

    public int visit(Expr.Cast node, int nt) {
        updateMax(nt);
        visit(node.value, nt);
        return nt;
    }
}
