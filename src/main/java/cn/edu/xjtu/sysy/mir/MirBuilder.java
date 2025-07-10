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
                var helper = globalInitBB.getHelper();
                for (var varDef : globals) {
                    var globalVar = varDef.resolution.address;
                    // 若没有初始化表达式，则自动使用零初始化
                    var initExpr = varDef.init;
                    var initInstruction = initExpr == null ?
                            helper.store(globalVar, ZeroInit)
                            : helper.store(globalVar, visit(initExpr));
                    globalInitBB.addInstruction(initInstruction);
                }

                globalInitBB.setTerminator(helper.jmp(main.entry));
                main.entry = globalInitBB;
                main.blocks.addFirst(globalInitBB);
            }
        }

        return mod;
    }

    public void visit(Decl.FuncDef node) {
        var symbol = node.resolution;

        var func = curMod.newFunction(symbol.name, symbol.funcType);
        var entryBB = func.addNewBlock("body");

        curFunc = func;
        symbol.address = func;

        func.entry = entryBB;
        curBB = entryBB;

        for (var arg : symbol.params) arg.address = func.addNewLocalVar(arg.name, arg.type);
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
        var helper = curBB.getHelper();
        for (var varDef : node.varDefs) {
            // 若没有初始化表达式，则自动使用零初始化
            var initExpr = varDef.init;
            if (initExpr != null) {
                var initInstruction = helper.store(varDef.resolution.address, visit(initExpr));
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
        var helper = curBB.getHelper();
        var retVal = node.value;
        curBB.setTerminator(retVal == null ? helper.ret() : helper.ret(visit(retVal)));
    }

    public void visit(Stmt.Assign node) {
        var helper = curBB.getHelper();
        var target = node.target;
        var value = node.value;

        switch (target) {
            case Expr.VarAccess varAccess -> {
                var symbol = varAccess.resolution;
                curBB.addInstruction(helper.store(symbol.address, visit(value)));
            }
            case Expr.IndexAccess indexAccess -> {
                var symbol = indexAccess.lhs.resolution;
                var indexes = indexAccess.indexes;
                var indexSize = indexes.size();
                var indices = new Value[indexSize];
                for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));
                var arr = helper.load(symbol.address);
                curBB.addInstruction(arr);
                var getPtr = helper.getElementPtr(arr, indices);
                curBB.addInstruction(getPtr);
                var store = helper.store(getPtr, visit(value));
                curBB.addInstruction(store);
            }
            default -> unsupported(target);
        }
    }

    public void visit(Stmt.ExprEval node) {
        visit(node.expr);
    }

    public void visit(Stmt.If node) {
        var helper = curBB.getHelper();
        var curLabel = curBB.label;

        var condVal = visit(node.cond);
        var thenBB = curFunc.addNewBlock("%s.if-then".formatted(curLabel));
        var mergeBB = new BasicBlock(curFunc, "%s.if-merge".formatted(curLabel));

        var elseStmt = node.elseStmt;
        if (elseStmt == Stmt.Empty.INSTANCE) {
            curBB.setTerminator(helper.br(condVal, thenBB, mergeBB));

            curBB = thenBB;
            visit(node.thenStmt);
            if (curBB.terminator == null) curBB.setTerminator(helper.jmp(mergeBB));
        } else {
            var elseBB = curFunc.addNewBlock("%s.if-else".formatted(curLabel));
            curBB.setTerminator(helper.br(condVal, thenBB, elseBB));

            curBB = thenBB;
            visit(node.thenStmt);
            if (curBB.terminator == null) curBB.setTerminator(helper.jmp(mergeBB));

            curBB = elseBB;
            visit(elseStmt);
            if (curBB.terminator == null) curBB.setTerminator(helper.jmp(mergeBB));
        }

        curFunc.addBlock(mergeBB);
        curBB = mergeBB;
    }

    public void visit(Stmt.While node) {
        var helper = curBB.getHelper();
        var curLabel = curBB.label;

        var condVal = visit(node.cond);
        var loopBB = curFunc.addNewBlock("%s.while-body".formatted(curLabel));
        var mergeBB = curFunc.newBlock("%s.while-merge".formatted(curLabel));

        curBB.setTerminator(helper.br(condVal, loopBB, mergeBB));

        loopBlocks.addLast(loopBB);
        loopExits.addLast(mergeBB);

        curBB = loopBB;
        visit(node.body);
        if (curBB.terminator == null) curBB.setTerminator(helper.br(condVal, loopBB, mergeBB));

        loopBlocks.removeLast();
        loopExits.removeLast();

        curFunc.addBlock(mergeBB);
        curBB = mergeBB;
    }

    public void visit(Stmt.Break node) {
        var helper = curBB.getHelper();
        if (loopExits.isEmpty()) {
            err("break statement not in loop");
            return;
        }

        var exitBB = loopExits.getLast();
        curBB.setTerminator(helper.jmp(exitBB));
    }

    public void visit(Stmt.Continue node) {
        var helper = curBB.getHelper();
        if (loopBlocks.isEmpty()) {
            err("continue statement not in loop");
            return;
        }

        var loopBB = loopBlocks.getLast();
        curBB.setTerminator(helper.jmp(loopBB));
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
        var helper = curBB.getHelper();
        var lhs = visit(node.lhs);
        var rhs = visit(node.rhs);
        var instr = switch (node.op) {
            case ADD -> helper.add(lhs, rhs);
            case SUB -> helper.sub(lhs, rhs);
            case MUL -> helper.mul(lhs, rhs);
            case DIV -> helper.div(lhs, rhs);
            case MOD -> helper.mod(lhs, rhs);

            case EQ -> helper.eq(lhs, rhs);
            case NE -> helper.ne(lhs, rhs);
            case GT -> helper.gt(lhs, rhs);
            case GE -> helper.ge(lhs, rhs);
            case LT -> helper.lt(lhs, rhs);
            case LE -> helper.le(lhs, rhs);

            case AND -> helper.and(lhs, rhs);
            case OR -> helper.or(lhs, rhs);
            default -> (Instruction) unsupported(node.op);
        };
        curBB.addInstruction(instr);
        return instr;
    }

    public Value visit(Expr.Unary node) {
        var helper = curBB.getHelper();
        var rhs = visit(node.rhs);
        if (node.op == Expr.Operator.ADD) return rhs;

        var instr = switch (node.op) {
            case SUB -> helper.neg(rhs);
            case NOT -> helper.not(rhs);
            default -> (Instruction) unsupported(node.op);
        };
        curBB.addInstruction(instr);
        return instr;
    }

    public Value visit(Expr.Call node) {
        var helper = curBB.getHelper();
        var symbol = node.resolution;
        var args = new Value[node.args.size()];
        for (int i = 0; i < node.args.size(); i++) args[i] = visit(node.args.get(i));
        var call = symbol.isExternal ? helper.callExternal(symbol.funcType.returnType, args)
                : helper.call(symbol.address, args);
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
        var helper = curBB.getHelper();
        var symbol = node.resolution;

        var load = helper.load(symbol.address);
        curBB.addInstruction(load);

        return load;
    }

    public Value visit(Expr.IndexAccess node) {
        var helper = curBB.getHelper();
        var symbol = node.lhs.resolution;
        var indexes = node.indexes;
        var indexSize = indexes.size();
        var indices = new Value[indexSize];
        for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));

        // 先要取 “变量类型的值” 指向的 “数组类型的值”
        var arr = helper.load(symbol.address);
        curBB.addInstruction(arr);

        // 再计算 indices 偏移
        var elementPtr = helper.getElementPtr(arr, indices);
        curBB.addInstruction(elementPtr);

        var load = helper.load(elementPtr);
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
        var helper = curBB.getHelper();
        var value = visit(node.value);
        if (node.toType == Types.Int) {
            var f2i = helper.f2i(value);
            curBB.addInstruction(f2i);
            return f2i;
        } else if (node.toType == Types.Float) {
            var i2f = helper.i2f(value);
            curBB.addInstruction(i2f);
            return i2f;
        }

        return unsupported(value);
    }
}
