package cn.edu.xjtu.sysy.mir;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Placeholder;

import java.util.ArrayDeque;
import java.util.ArrayList;

import static cn.edu.xjtu.sysy.mir.node.InstructionHelper.*;
import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.*;

/**
 * Middle IR Builder
 * 这个类不是线程安全的，但是好像我们也没有多线程的需求
 */
public final class MirBuilder implements ErrManaged {

    private ErrManager errManager;

    public MirBuilder() {
        this(ErrManager.GLOBAL);
    }

    public MirBuilder(ErrManager errManager) {
        this.errManager = errManager;
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

    private int valueCount;
    private int nextIndex() {
        return valueCount++;
    }

    private ArrayDeque<BasicBlock> loopBlocks = new ArrayDeque<>();
    private ArrayDeque<BasicBlock> loopExits = new ArrayDeque<>();


    public Module build(CompUnit compUnit) {
        return visit(compUnit);
    }

    public Module visit(CompUnit node) {
        var mod = new Module();
        curMod = mod;

        var globals = new ArrayList<Decl.VarDef>();
        var funcs = new ArrayList<Decl.FuncDef>();

        for (var decl : node.decls) {
            if (decl instanceof Decl.VarDef varDef) {
                globals.add(varDef);
                var symbol = varDef.resolution;
                symbol.address = mod.newGlobalVar(symbol.name, symbol.type);
            } else if (decl instanceof Decl.FuncDef funcDef) {
                funcs.add(funcDef);
                var symbol = funcDef.resolution;
                symbol.address = mod.newFunction(symbol.name, symbol.funcType.returnType);
            }
        }

        for (var funcDef : funcs) {
            visit(funcDef);

            if (funcDef.resolution.name.equals("main")) {
                var main = funcDef.resolution.address;
                var globalInitBB = main.newBlock("init-globals");
                for (var varDef : globals) {
                    var globalVar = varDef.resolution.address;
                    // 若没有初始化表达式，则自动使用零初始化
                    var initExpr = varDef.init;
                    var initInstruction = initExpr == null ?
                            store(globalVar, ZeroInit)
                            : store(globalVar, visit(initExpr));
                    globalInitBB.addInstruction(initInstruction);
                }

                globalInitBB.setTerminator(jmp(main.entry));
                main.entry = globalInitBB;
                main.blocks.addFirst(globalInitBB);
            }
        }

        return mod;
    }

    public void visit(Decl.FuncDef node) {
        valueCount = 0;

        var symbol = node.resolution;

        var func = curMod.newFunction(symbol.name, symbol.funcType);
        var entryBB = func.addNewBlock("body");

        curFunc = func;
        symbol.address = func;

        func.entry = entryBB;
        curBB = entryBB;

        for (var arg : symbol.params) {
            // 函数的参数就是入口基本块的参数
            arg.address = entryBB.newBlockArgument(arg.type);
        }

        for (var var : node.allVars) var.address = curFunc.addNewLocalVar(var.name, var.type);

        visit(node.body);
    }

    public void visit(Stmt node) {
        switch (node) {
            case null -> Placeholder.pass();
            case Stmt.Empty _ -> Placeholder.pass();
            case Stmt.Assign it -> visit(it);
            case Stmt.Block it -> visit(it);
            case Stmt.Break it -> visit(it);
            case Stmt.Continue it -> visit(it);
            case Stmt.ExprEval it -> visit(it);
            case Stmt.If it -> visit(it);
            case Stmt.LocalVarDef it -> visit(it);
            case Stmt.Return it -> visit(it);
            case Stmt.While it -> visit(it);
            default -> unsupported(node);
        }
    }

    public void visit(Stmt.LocalVarDef node) {
        for (var varDef : node.varDefs) {
            // 若没有初始化表达式，则自动使用零初始化
            var initExpr = varDef.init;
            if (initExpr != null) {
                var initInstruction = store(varDef.resolution.address, visit(initExpr));
                curBB.addInstruction(initInstruction);
            }
        }
    }

    public void visit(Stmt.Block node) {
        for (Stmt stmt : node.stmts) {
            visit(stmt);

            if (stmt instanceof Stmt.Return || stmt instanceof Stmt.Break || stmt instanceof Stmt.Continue) {
                // 如果是返回、break或continue语句，后面的语句不应该被 emit
                return;
            }
        }
    }

    public void visit(Stmt.Return node) {
        var retVal = node.value;
        curBB.setTerminator(retVal == null ? ret() : ret(visit(retVal)));
    }

    public void visit(Stmt.Assign node) {
        var target = node.target;
        var value = node.value;

        switch (target) {
            case Expr.VarAccess varAccess -> {
                var symbol = varAccess.resolution;
                curBB.addInstruction(store(symbol.address, visit(value)));
            }
            case Expr.IndexAccess indexAccess -> {
                var symbol = indexAccess.lhs.resolution;
                var indexes = indexAccess.indexes;
                var indexSize = indexes.size();
                var indices = new Value[indexSize];
                for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));
                var arr = load(nextIndex(), symbol.address);
                curBB.addInstruction(arr);
                var getPtr = getElementPtr(nextIndex(), arr, indices);
                curBB.addInstruction(getPtr);
                var store = store(getPtr, visit(value));
                curBB.addInstruction(store);
            }
            default -> unsupported(target);
        }
    }

    public void visit(Stmt.ExprEval node) {
        visit(node.expr);
    }

    public void visit(Stmt.If node) {
        var curLabel = curBB.label;

        var condVal = visit(node.cond);
        var thenBB = curFunc.addNewBlock("%s.if-then".formatted(curLabel));
        var mergeBB = new BasicBlock(curFunc, "%s.if-merge".formatted(curLabel));

        var elseStmt = node.elseStmt;
        if (elseStmt == Stmt.Empty.INSTANCE) {
            curBB.setTerminator(br(condVal, thenBB, mergeBB));

            curBB = thenBB;
            visit(node.thenStmt);
            if (curBB.terminator == null) curBB.setTerminator(jmp(mergeBB));
        } else {
            var elseBB = curFunc.addNewBlock("%s.if-else".formatted(curLabel));
            curBB.setTerminator(br(condVal, thenBB, elseBB));

            curBB = thenBB;
            visit(node.thenStmt);
            if (curBB.terminator == null) curBB.setTerminator(jmp(mergeBB));

            curBB = elseBB;
            visit(elseStmt);
            if (curBB.terminator == null) curBB.setTerminator(jmp(mergeBB));
        }

        curFunc.addBlock(mergeBB);
        curBB = mergeBB;
    }

    public void visit(Stmt.While node) {
        var curLabel = curBB.label;

        var condVal = visit(node.cond);
        var loopBB = curFunc.addNewBlock("%s.while-body".formatted(curLabel));
        var mergeBB = curFunc.newBlock("%s.while-merge".formatted(curLabel));

        curBB.setTerminator(br(condVal, loopBB, mergeBB));

        loopBlocks.addLast(loopBB);
        loopExits.addLast(mergeBB);

        curBB = loopBB;
        visit(node.body);
        if (curBB.terminator == null) curBB.setTerminator(br(condVal, loopBB, mergeBB));

        loopBlocks.removeLast();
        loopExits.removeLast();

        curFunc.addBlock(mergeBB);
        curBB = mergeBB;
    }

    public void visit(Stmt.Break node) {
        if (loopExits.isEmpty()) {
            err("break statement not in loop");
            return;
        }

        var exitBB = loopExits.getLast();
        curBB.setTerminator(jmp(exitBB));
    }

    public void visit(Stmt.Continue node) {
        if (loopBlocks.isEmpty()) {
            err("continue statement not in loop");
            return;
        }

        var loopBB = loopBlocks.getLast();
        curBB.setTerminator(jmp(loopBB));
    }

    public Value visit(Expr node) {
        return switch (node) {
            case Expr.Binary it -> visit(it);
            case Expr.Unary it -> visit(it);
            case Expr.Call it -> visit(it);
            case Expr.IndexAccess it -> visit(it);
            case Expr.VarAccess it -> visit(it);
            case Expr.Array it -> visit(it);
            case Expr.Literal it -> visit(it);
            case Expr.Cast it -> visit(it);
            default -> unsupported(node);
        };
    }

    public Value visit(Expr.Binary node) {
        var lhs = visit(node.lhs);
        var rhs = visit(node.rhs);
        var instr = switch (node.op) {
            case ADD -> add(nextIndex(), lhs, rhs);
            case SUB -> sub(nextIndex(), lhs, rhs);
            case MUL -> mul(nextIndex(), lhs, rhs);
            case DIV -> div(nextIndex(), lhs, rhs);
            case MOD -> mod(nextIndex(), lhs, rhs);

            case EQ -> eq(nextIndex(), lhs, rhs);
            case NE -> ne(nextIndex(), lhs, rhs);
            case GT -> gt(nextIndex(), lhs, rhs);
            case GE -> ge(nextIndex(), lhs, rhs);
            case LT -> lt(nextIndex(), lhs, rhs);
            case LE -> le(nextIndex(), lhs, rhs);

            case AND -> and(nextIndex(), lhs, rhs);
            case OR -> or(nextIndex(), lhs, rhs);
            default -> (Instruction) unsupported(node.op);
        };
        curBB.addInstruction(instr);
        return instr;
    }

    public Value visit(Expr.Unary node) {
        var rhs = visit(node.rhs);
        if (node.op == Expr.Operator.ADD) return rhs;

        var instr = switch (node.op) {
            case SUB -> neg(nextIndex(), rhs);
            case NOT -> not(nextIndex(), rhs);
            default -> (Instruction) unsupported(node.op);
        };
        curBB.addInstruction(instr);
        return instr;
    }

    public Value visit(Expr.Call node) {
        var symbol = node.resolution;
        var args = new Value[node.args.size()];
        for (int i = 0; i < node.args.size(); i++) args[i] = visit(node.args.get(i));
        var call = symbol.isExternal ? callExternal(nextIndex(), symbol.funcType.returnType, args)
                : call(nextIndex(), symbol.address, args);
        curBB.addInstruction(call);
        return call;
    }

    public Value visit(Expr.Assignable node) {
        return switch (node) {
            case Expr.VarAccess varAccess -> visit(varAccess);
            case Expr.IndexAccess indexAccess -> visit(indexAccess);
        };
    }

    public Value visit(Expr.VarAccess node) {
        var symbol = node.resolution;

        var load = load(nextIndex(), symbol.address);
        curBB.addInstruction(load);

        return load;
    }

    public Value visit(Expr.IndexAccess node) {
        var symbol = node.lhs.resolution;
        var indexes = node.indexes;
        var indexSize = indexes.size();
        var indices = new Value[indexSize];
        for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));

        // 先要取 “变量类型的值” 指向的 “数组类型的值”
        var arr = load(nextIndex(), symbol.address);
        curBB.addInstruction(arr);

        // 再计算 indices 偏移
        var elementPtr = getElementPtr(nextIndex(), arr, indices);
        curBB.addInstruction(elementPtr);

        var load = load(nextIndex(), elementPtr);
        curBB.addInstruction(load);

        return load;
    }

    public Value visit(Expr.Literal node) {
        var type = node.type;
        if (type == Types.Int)
            return intConst((Integer) node.getComptimeValue());
        else if (type == Types.Float)
            return floatConst((Float) node.getComptimeValue());

        return unsupported(node.type);
    }

    public Value visit(Expr.Array node) {
        var elements = node.elements;
        var elementCount = elements.size();
        var values = new Value[elementCount];
        for (int i = 0; i < elementCount; i++) values[i] = visit(elements.get(i));
        var indexes = node.indexes.stream().mapToInt(Integer::intValue).toArray();
        return sparseArrayOf(node.type, indexes, values);
    }

    public Value visit(Expr.Cast node) {
        var value = visit(node.value);
        if (node.toType == Types.Int) {
            var f2i = f2i(nextIndex(), value);
            curBB.addInstruction(f2i);
            return f2i;
        } else if (node.toType == Types.Float) {
            var i2f = i2f(nextIndex(), value);
            curBB.addInstruction(i2f);
            return i2f;
        }

        return unsupported(value);
    }
}
