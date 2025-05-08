package cn.edu.xjtu.sysy.ast.pass;

import java.util.Stack;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.riscv.Label;
import static cn.edu.xjtu.sysy.riscv.Register.Float.FA0;
import static cn.edu.xjtu.sysy.riscv.Register.Float.FT0;
import static cn.edu.xjtu.sysy.riscv.Register.Int.A;
import static cn.edu.xjtu.sysy.riscv.Register.Int.A0;
import static cn.edu.xjtu.sysy.riscv.Register.Int.FP;
import static cn.edu.xjtu.sysy.riscv.Register.Int.RA;
import static cn.edu.xjtu.sysy.riscv.Register.Int.SP;
import static cn.edu.xjtu.sysy.riscv.Register.Int.T0;
import static cn.edu.xjtu.sysy.riscv.Register.Int.T1;
import static cn.edu.xjtu.sysy.riscv.Register.Int.ZERO;
import cn.edu.xjtu.sysy.riscv.RiscVWriter;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.Symbol.Var;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;



public class RiscVCGen extends AstVisitor {
    protected final RiscVWriter asm;

    protected SymbolTable.Global globalST = null;
    
    protected SymbolTable currentST = null;

    protected Symbol.Func currentFunc = null;

    protected Label funcEpilogue = null;

    protected Label loopEntry = null;

    protected Label loopEpilogue = null;

    protected int nextLabel = 0;

    public RiscVCGen(RiscVWriter asm0) {
        super(null);
        asm = asm0;
    }

    protected Label genLocalLabel() {
        return new Label(String.format(".L%d", nextLabel++));
    }

    @Override
    public void visit(CompUnit node) {
        currentST = node.globalST;
        globalST = node.globalST;
        for (var decl : node.decls) {
            if (decl instanceof Decl.VarDef it) visit(it);
        }
        for (var decl : node.decls) {
            if (decl instanceof Decl.FuncDef it) visit(it);
        }
    }

    @Override
    public void visit(Decl.FuncDef node) {
        var sym = globalST.resolveFunc(node.name);
        asm.alignNext(1);
        asm.emitGlobal(sym.label);
        asm.startCode();
        asm.emitType(sym);
        asm.emitGlobalLabel(sym.label);

        currentST = node.symbolTable;
        funcEpilogue = genLocalLabel();
        currentFunc = node.resolution;
        int funcSize = node.resolution.getSize();
        if (funcSize > 2048) {
            asm .addi(SP, SP, -16);
            
            if (node.resolution.raSave)
                asm.sd(RA, SP, 8)
                .sd(FP, SP, 0);
            else 
                asm.sd(FP, SP, 8);

            asm.addi(FP, SP, 16);
            asm .li(T0, -funcSize+16)
                .add(SP, SP, T0);
        } else {
            asm .addi(SP, SP, -funcSize);
            if (node.resolution.raSave)
                asm.sd(RA, SP, funcSize-8)
                .sd(FP, SP, funcSize-16);
            else 
                asm.sd(FP, SP, funcSize-8);
            
            asm.addi(FP, SP, funcSize);
        }

        for (var param : sym.params) {
            param.declared = true;
        }
        if (node.resolution.raSave)
            visit(node.body, 24);
        else
            visit(node.body, 16);

        asm .mv(A0, ZERO);

        asm.label(funcEpilogue);
        if (funcSize > 2048) {
            asm .li(T0, funcSize-16)
                .add(SP, SP, T0);
            if (node.resolution.raSave)
                asm .ld(RA, SP, 8)
                    .ld(FP, SP, 0);
            else 
                asm .ld(FP, SP, 8);
            
            asm .addi(SP, SP, 16)
                .ret();
        } else {
            if (node.resolution.raSave)
                asm .ld(RA, SP, funcSize-8)
                    .ld(FP, SP, funcSize-16);
            else 
                asm .ld(FP, SP, funcSize-8);
            
            asm .addi(SP, SP, funcSize)
                .ret();
        }
        asm.emitAll();
        asm.emitSize(sym);
        currentST = currentST.getParent();
        currentFunc = null;
    }

    @Override
    public void visit(Decl.VarDef node) {
        var sym = node.resolution;
        sym.declared = true;
        if (node.init == null) {
            asm.emitWeak(sym);
            return;
        }
        switch (sym.type) {
            case Type.Primitive _ -> {
                if (node.init.getComptimeValue().floatValue() == 0) {
                    asm.emitGlobal(sym.label);
                    asm.sbss();
                    asm.alignNext(2);
                    asm.emitType(sym);
                    asm.emitSize(sym);
                    asm.emitGlobalLabel(sym.label);
                    asm.emitZeroLiteral(sym.type.size());
                } else {
                    asm.emitGlobal(sym.label);
                    asm.sdata();
                    asm.alignNext(2);
                    asm.emitType(sym);
                    asm.emitSize(sym);
                    asm.emitGlobalLabel(sym.label);
                    asm.emitWordLiteral(node.init.getComptimeValue());
                }
            }
            case Type.Array _ -> {
                asm.emitGlobal(sym.label);
                var arr = (Expr.Array) node.init;
                if (arr.elements.isEmpty()) asm.bss();
                else asm.data();
                asm.alignNext(3);
                asm.emitType(sym);
                asm.emitSize(sym);
                asm.emitGlobalLabel(sym.label);
                int lasti = -1;
                for (int i = 0; i < arr.indexes.size(); i++) {
                    int index = arr.indexes.get(i);
                    Expr exp = arr.elements.get(i);
                    if (index - lasti > 1) {
                        asm.emitZeroLiteral(4* (index-lasti-1));
                    }
                    asm.emitWordLiteral(exp.getComptimeValue());
                    lasti = index;
                }
                int tailSize = sym.type.size() - 4*(lasti+1);
                if (tailSize > 0)
                    asm.emitZeroLiteral(tailSize);
            }
            default -> unreachable();
        }
    }

    // LocalVarDef
    public int visit(Decl.VarDef node, int nt) {
        var sym = node.resolution;
        sym.declared = true;
        if (node.init == null) {
            return sym.type.size();
        }
        assert (nt + sym.type.size() - 4 == currentFunc.getIndex(sym));
        switch (sym.type) {
            case Type.Primitive _ -> {
                if (sym.comptimeValue != null) {
                    asm .li(A0, sym.comptimeValue)
                        .sw(A0, FP, -currentFunc.getIndex(sym), T0);
                } else {
                    visit(node.init, nt);
                    if (sym.type.equals(Type.Primitive.FLOAT)) 
                        asm.fsw(FA0, FP, -currentFunc.getIndex(sym), T0);
                    else
                        asm.sw(A0, FP, -currentFunc.getIndex(sym), T0);
                }
            }
            case Type.Array arrType -> {
                var arr = (Expr.Array) node.init;
                int arrayIndex = currentFunc.getIndex(sym);
                int lasti = -1;
                
                for (int i = 0; i < arr.indexes.size(); i++) {
                    int index = arr.indexes.get(i);
                    Expr exp = arr.elements.get(i);
                    if (index - lasti > 1) {
                        asm.setzero(arrayIndex - 4*(lasti+1), 4* (index-lasti-1), T0);
                    }
                    if (exp.isComptime) {
                        asm.li(A0, exp.getComptimeValue())
                            .sw(A0, FP, -(arrayIndex - 4* index), T0);
                    } else {
                        visit(exp, arrayIndex + 4);
                        if (arrType.elementType.equals(Type.Primitive.FLOAT)) 
                            asm.fsw(FA0, FP, -(arrayIndex - 4* index), T0);
                        else
                            asm.sw(A0, FP, -(arrayIndex - 4* index), T0);
                    }
                    lasti = index;
                }
                int tailSize = sym.type.size() - 4*(lasti+1);
                if (tailSize > 0)
                    asm.setzero(arrayIndex - 4*(lasti+1), tailSize, T0);
    
            }
            default -> unreachable();
        }
        return sym.type.size();
    }

    public int visit(Stmt node, int nt) {
        return switch (node) {
            case Stmt.Assign it -> visit(it, nt);
            case Stmt.Block it -> visit(it, nt);
            case Stmt.Break it -> visit(it, nt);
            case Stmt.Continue it -> visit(it, nt);
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
        currentST = node.symbolTable;
        int m = nt;
        for (var stmt : node.stmts) {
            m = visit(stmt, m);
        }
        currentST = currentST.getParent();
        return nt;
    }

    public int visit(Stmt.Return node, int nt) {
        var value = node.value;
        if (value != null) visit(node.value, nt);
        asm.j(funcEpilogue);
        return nt;
    }

    public int visit(Stmt.Assign node, int nt) {
        visit(node.value, nt);
        switch (node.target) {
            case Expr.VarAccess it -> {
                var sym = currentST.resolve(it.name);
                var st = currentST;
                while (!sym.declared) {
                    st = st.getParent();
                    sym = st.resolve(it.name);
                }
                switch (sym.kind) {
                    case Var.Kind.GLOBAL -> {
                        if (sym.type.equals(Type.Primitive.FLOAT)) 
                            asm.fsw(FA0, sym.label, T0);
                        else
                            asm.sw(A0, sym.label, T0);
                    }
                    case Var.Kind.LOCAL -> {
                        if (sym.type.equals(Type.Primitive.FLOAT)) 
                            asm.fsw(FA0, FP, -currentFunc.getIndex(sym), T0);
                        else
                            asm.sw(A0, FP, -currentFunc.getIndex(sym), T0);
                    }
                }
            }
            case Expr.IndexAccess it -> {
                var sym = currentST.resolve(it.lhs.name);
                var st = currentST;
                while (!sym.declared) {
                    st = st.getParent();
                    sym = st.resolve(it.lhs.name);
                }
                var type = (Type.Array) sym.type;
                if (type.elementType.equals(Type.Primitive.FLOAT)) 
                    asm.fsw(FA0, FP, -nt, T0);
                else
                    asm.sw(A0, FP, -nt, T0);
                var i0 = it.indexes.get(0);
                visit(i0, nt+4);
                if (it.indexes.size() > 1) {
                    int addr = align(nt+4);
                    for (int i = 1; i < it.indexes.size(); i++) {
                        asm .li(T0, type.dimensions[i])
                            .mul(A0, T0, A0)
                            .sd(A0, FP, -addr, T0);
                        var index = it.indexes.get(i);
                        visit(index, addr+8);
                        asm .ld(T0, FP, -addr, T1)
                            .add(A0, T0, A0);
                    }
                }
                switch (sym.kind) {
                    case Var.Kind.GLOBAL -> {
                        asm .slli(A0, A0, 2)
                            .la(T0, sym.label)
                            .add(A0, T0, A0);
                        if (type.elementType.equals(Type.Primitive.FLOAT)) 
                            asm .flw(FT0, FP, -nt, T1)
                                .fsw(FT0, A0, 0);
                        else
                            asm .lw(T0, FP, -nt, T1)
                                .sw(T0, A0, 0);
                    }
                    case Var.Kind.LOCAL -> {
                        int index = currentFunc.getIndex(sym);
                        if (index <= 0) {
                            asm .slli(A0, A0, 2)
                                .ld(T0, FP, -index, T1)
                                .add(A0, T0, A0);
                        } else {
                            asm .slli(A0, A0, 2)
                                .addi(T0, FP, -index, T1)
                                .add(A0, T0, A0);
                        }
                        if (type.elementType.equals(Type.Primitive.FLOAT)) 
                            asm .flw(FT0, FP, -nt, T1)
                                .fsw(FT0, A0, 0);
                        else
                            asm .lw(T0, FP, -nt, T1)
                                .sw(T0, A0, 0);
                    }
                }
            }
        }
        return nt;
    }

    public int visit(Stmt.ExprEval node, int nt) {
        visit(node.expr, nt);
        return nt;
    }

    public int visit(Stmt.LocalVarDef node, int nt) {
        int m = 0;
        for (var varDef : node.varDefs) {
            m += visit(varDef, nt+m);
        }
        return nt+m;
    }

    public int visit(Stmt.If node, int nt) {
        var end = genLocalLabel();
        var elseLabel = genLocalLabel();
        visit(node.cond, nt);
        asm.beqz(A0, elseLabel);
        visit(node.thenStmt, nt);
        asm.j(end);
        asm.label(elseLabel);
        visit(node.elseStmt, nt);
        asm.label(end);
        return nt;
    }

    public int visit(Stmt.While node, int nt) {
        var outerEntry = loopEntry;
        var outerEpilogue = loopEpilogue;

        loopEntry = genLocalLabel();
        loopEpilogue = genLocalLabel();
        asm.label(loopEntry);
        visit(node.cond, nt);
        asm.beqz(A0, loopEpilogue);
        visit(node.body, nt);
        asm.j(loopEntry);
        asm.label(loopEpilogue); 

        loopEntry = outerEntry;
        loopEpilogue = outerEpilogue;
        return nt;
    }

    public int visit(Stmt.Break node, int nt) { 
        asm.j(loopEpilogue);
        return nt;
    }

    public int visit(Stmt.Continue node, int nt) {
        asm.j(loopEntry);
        return nt;
    }

    public int visit(Expr node, int nt) {
        return switch (node) {
            case Expr.Assignable it -> visit(it, nt);
            case Expr.Binary it -> visit(it, nt);
            case Expr.Call it -> visit(it, nt);
            case Expr.Literal it -> visit(it, nt);
            case Expr.Unary it -> visit(it, nt);
            case Expr.Cast it -> visit(it, nt);
            default -> unreachable();
        };
    }

    public int visit(Expr.Binary node, int nt) {
        Stack<Expr.Binary> binaries = new Stack<>();
        Stack<Expr.Operator> ops = new Stack<>();
        Expr expr = node;
        while (expr instanceof Expr.Binary it) {
            binaries.push(it);
            ops.push(it.op);
            expr = it.lhs;
        }
        visit(expr, nt);
        assert expr != null;
        while (!binaries.isEmpty()) {
            var bin = binaries.pop();
            var op = ops.pop();
            if (!bin.isLogical()) {
                if (expr.type.equals(Type.Primitive.FLOAT)) {
                    asm.fsw(FA0, FP, -nt, T0);
                } else {
                    asm.sw(A0, FP, -nt, T0);
                }
                visit(bin.rhs, nt+4);
                if (expr.type.equals(Type.Primitive.FLOAT)) {
                    asm.flw(FT0, FP, -nt, T1);
                } else {
                    asm.lw(T0, FP, -nt, T1);
                }
            }
            switch (op) {
                case Expr.Operator.ADD -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .fadd(FA0, FT0, FA0);
                    } else {
                        asm .addw(A0, T0, A0);
                    }
                }
                case Expr.Operator.SUB -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .fsub(FA0, FT0, FA0);
                    } else {
                        asm .subw(A0, T0, A0);
                    }
                }
                case Expr.Operator.MUL -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .fmul(FA0, FT0, FA0);
                    } else {
                        asm .mulw(A0, T0, A0);
                    }
                }
                case Expr.Operator.DIV -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .fdiv(FA0, FT0, FA0);
                    } else {
                        asm .divw(A0, T0, A0);
                    }
                }
                case Expr.Operator.MOD -> {
                    asm .remw(A0, T0, A0);
                }
                case Expr.Operator.EQ -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .feq(A0, FT0, FA0);
                    } else {
                        asm .xor(A0, T0, A0)
                            .seqz(A0, A0);
                    }
                }
                case Expr.Operator.NE -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .feq(A0, FT0, FA0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .xor(A0, T0, A0)
                            .snez(A0, A0);
                    }
                }
                case Expr.Operator.LT -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .flt(A0, FT0, FA0);
                    } else {
                        asm .slt(A0, T0, A0);
                    }
                }
                case Expr.Operator.LE -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .flt(A0, FA0, FT0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .slt(A0, A0, T0)
                            .xori(A0, A0, 1);
                    }
                }
                case Expr.Operator.GT -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .flt(A0, FA0, FT0);
                    } else {
                        asm .slt(A0, A0, T0);
                    }
                }
                case Expr.Operator.GE -> {
                    if (bin.rhs.type.equals(Type.Primitive.FLOAT)) {
                        asm .flt(A0, FT0, FA0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .slt(A0, T0, A0)
                            .xori(A0, A0, 1);
                    }
                }
                case Expr.Operator.AND -> {
                    var end = genLocalLabel();
                    if (expr.type == Type.Primitive.FLOAT) {
                        asm .fmv_w_x(FT0, ZERO)
                            .feq(A0, FA0, FT0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .snez(A0, A0);
                    }
                    asm.beqz(A0, end);
                    asm.sw(A0, FP, -nt, T0);
                    visit(bin.rhs, nt+4);
                    if (bin.rhs.type == Type.Primitive.FLOAT) {
                        asm .fmv_w_x(FT0, ZERO)
                            .feq(A0, FA0, FT0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .snez(A0, A0);
                    }
                   
                    asm .lw(T0, FP, -nt, T1)
                        .and(A0, T0, A0);
                    asm.label(end);
                }
                case Expr.Operator.OR -> {
                    var end = genLocalLabel();
                    if (expr.type == Type.Primitive.FLOAT) {
                        asm .fmv_w_x(FT0, ZERO)
                            .feq(A0, FA0, FT0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .snez(A0, A0);
                    }
                    asm.bnez(A0, end);
                    asm.sw(A0, FP, -nt, T0);
                    visit(bin.rhs, nt+4);
                    if (bin.rhs.type == Type.Primitive.FLOAT) {
                        asm .fmv_w_x(FT0, ZERO)
                            .feq(A0, FA0, FT0)
                            .xori(A0, A0, 1);
                    } else {
                        asm .snez(A0, A0);
                    }
                    asm .lw(T0, FP, -nt, T1)
                        .or(A0, T0, A0);
                    asm.label(end);
                }
                default -> unreachable();
            }
            expr = bin;
        }
        return nt;
    }

    public int visit(Expr.Unary node, int nt) {
        visit(node.rhs, nt);
        switch (node.op) {
            case NOT -> {
                if (node.rhs.type == Type.Primitive.FLOAT) {
                    asm .fmv_w_x(FT0, ZERO)
                        .feq(A0, FT0, FA0);
                } else {
                    asm .seqz(A0, A0);
                }
            }
            case ADD -> {
                // skip
            }
            case SUB -> {
                if (node.rhs.type.equals(Type.Primitive.FLOAT)) {
                    asm.fneg(FA0, FA0);
                } else {
                    asm.negw(A0, A0);
                }
            }
            default -> {
                unreachable();
            }
        }
        return nt;
    }

    public int visit(Expr.Assignable node, int nt) {
        return switch (node) {
            case Expr.VarAccess it -> visit(it, nt);
            case Expr.IndexAccess it -> visit(it, nt);
            case null, default -> unreachable();
        };
    }

    private int align(int nt) {
        return nt / 8 * 8 + 8;
    }

    public int visit(Expr.Call node, int nt) {
        var func = node.resolution;
        int m = nt;
        int[] ms = new int[node.args.size()];
        for (int i = 0; i < node.args.size(); i++) {
            var arg = node.args.get(i);
            visit(arg, m);
            switch (arg.type) {
                case Type.Primitive _ -> {
                    ms[i] = m;
                    if (arg.type.equals(Type.Primitive.FLOAT)) {
                        asm.fsw(FA0, FP, -m, T0);
                    } else {
                        asm.sw(A0, FP, -m, T0);
                    }
                    m += 4;
                }
                case Type.Array _ -> {
                    m = align(m);
                    ms[i] = m;
                    asm.sd(A0, FP, -m, T0);
                    m += 8;
                }
                default -> unreachable();
            }
        }

        for (int i = func.params.size() - 1; i >= 0; i--) {
            var param = func.params.get(i);
            switch (param.type) {
                case Type.Primitive _ -> {
                    m = ms[i];
                    asm .lw(A0, FP, -m, T0)
                        .sw(A0, SP, -func.getIndex(param), T0);
                }
                case Type.Array _ -> {
                    m = ms[i];
                    asm .ld(A0, FP, -m, T0)
                        .sd(A0, SP, -func.getIndex(param), T0);
                }
                default -> unreachable();
            }
            if (0 < i && i <= 7) {
                asm.mv(A(i), A0);
            }
        }

        asm.call(func.label);
        return nt;
    }

    public int visit(Expr.VarAccess node, int nt) { 
        var sym = currentST.resolve(node.name);
        var st = currentST;
        while (!sym.declared) {
            st = st.getParent();
            sym = st.resolve(node.name);
        }
        switch (sym.kind) {
            case Var.Kind.GLOBAL -> {
                switch (sym.type) {
                    case Type.Primitive _ -> {
                        asm .la(A0, sym.label);
                        if (node.type.equals(Type.Primitive.FLOAT)) 
                            asm.flw(FA0, A0, 0);
                        else
                            asm.lw(A0, A0, 0);
                    }
                    case Type.Array _ -> {
                        asm .la(A0, sym.label);
                    }
                    default -> unreachable();
                }
            }
            case Var.Kind.LOCAL -> {
                switch (sym.type) {
                    case Type.Primitive _ -> {
                        if (node.type.equals(Type.Primitive.FLOAT)) 
                            asm.flw(FA0, FP, -currentFunc.getIndex(sym), T0);
                        else
                            asm.lw(A0, FP, -currentFunc.getIndex(sym), T0);
                    }
                    case Type.Array _ -> {
                        int index = currentFunc.getIndex(sym);
                        if (index <= 0) {
                            asm.ld(A0, FP, -index, T0);
                        } else {
                            asm.addi(A0, FP, -index, T0);
                        }
                    }
                    default -> unreachable();
                }
            }
        }
        return nt;
    }

    public int visit(Expr.IndexAccess node, int nt) {
        var sym = currentST.resolve(node.lhs.name);
        var st = currentST;
        while (!sym.declared) {
            st = st.getParent();
            sym = st.resolve(node.lhs.name);
        }
        var type = (Type.Array) sym.type;
        var i0 = node.indexes.get(0);
        visit(i0, nt);
        if (node.indexes.size() > 1) {
            int addr = align(nt);
            for (int i = 1; i < node.indexes.size(); i++) {
                asm .li(T0, type.dimensions[i])
                    .mul(A0, T0, A0)
                    .sd(A0, FP, -addr, T0);
                var index = node.indexes.get(i);
                visit(index, addr+8);
                asm .ld(T0, FP, -addr, T1)
                    .add(A0, T0, A0);
            }
        }
        asm.slli(A0, A0, 2);
        switch (sym.kind) {
            case Var.Kind.GLOBAL -> {
                if (node.type instanceof Type.Array) {
                    asm .li(T0, type.dimensions[node.indexes.size()])
                        .mul(A0, T0, A0)
                        .la(T0, sym.label)
                        .add(A0, A0, T0);
                } else {
                    asm .la(T0, sym.label)
                        .add(T0, A0, T0);
                    if (node.type.equals(Type.Primitive.FLOAT)) 
                        asm.flw(FA0, T0, 0);
                    else
                        asm.lw(A0, T0, 0);
                }
            }
            case Var.Kind.LOCAL -> {
                int index = currentFunc.getIndex(sym);
                if (index <= 0) {
                    if (node.type instanceof Type.Array) {
                        asm .li(T0, type.dimensions[node.indexes.size()])
                            .mul(A0, T0, A0)
                            .ld(T0, FP, -index, T1)
                            .add(A0, T0, A0);
                    } else {
                        asm .ld(T0, FP, -index, T1)
                            .add(T0, T0, A0);
                        if (node.type.equals(Type.Primitive.FLOAT)) 
                            asm.flw(FA0, T0, 0);
                        else
                            asm.lw(A0, T0, 0);
                    }
                } else {
                    if (node.type instanceof Type.Array) {
                        asm .li(T0, type.dimensions[node.indexes.size()])
                            .mul(A0, T0, A0)
                            .addi(T0, FP, -index, T1)
                            .add(A0, T0, A0); 
                    } else {
                        asm .addi(T0, FP, -index, T1)
                            .add(T0, T0, A0);
                        if (node.type.equals(Type.Primitive.FLOAT)) 
                            asm.flw(FA0, T0, 0);
                        else
                            asm.lw(A0, T0, 0);
                    }
                }
            }
        }
        return nt;
    }

    public int visit(Expr.Literal node, int nt) {
        asm.li(A0, node.getComptimeValue());
        if (node.type.equals(Type.Primitive.FLOAT)) 
            asm.fmv_w_x(FA0, A0);
        return nt;
    }

    public int visit(Expr.Cast node, int nt) {
        visit(node.value, nt);
        if (node.fromType.equals(Type.Primitive.FLOAT) && node.toType.equals(Type.Primitive.INT)) {
            asm.fcvt_w_s(A0, FA0);
        } 
        if (node.fromType.equals(Type.Primitive.INT) && node.toType.equals(Type.Primitive.FLOAT)) {
            asm.fcvt_s_w(FA0, A0);
        } 
        return nt;
    }
}
