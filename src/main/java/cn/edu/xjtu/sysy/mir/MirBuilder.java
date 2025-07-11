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

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.*;

/**
 * Middle IR Builder
 * 这个类不是线程安全的，但是好像我们也没有多线程的需求
 */
public final class MirBuilder implements ErrManaged {

    private final ErrManager errManager;

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
    private final InstructionHelper helper = new InstructionHelper();

    private final ArrayDeque<BasicBlock> loopBlocks = new ArrayDeque<>();
    private final ArrayDeque<BasicBlock> loopExits = new ArrayDeque<>();

    public Module build(CompUnit compUnit) {
        return visit(compUnit);
    }

    public Module visit(CompUnit node) {
        var mod = new Module();
        curMod = mod;

        for (var decl : node.decls) {
            if (decl instanceof Decl.VarDef varDef) {
                var symbol = varDef.resolution;
                symbol.address = mod.newGlobalVar(symbol.name, symbol.type);
            } else if (decl instanceof Decl.FuncDef funcDef) {
                var symbol = funcDef.resolution;
                symbol.address = mod.newFunction(symbol.name, symbol.funcType);
                visit(funcDef);
            }
        }

        return mod;
    }

    public void visit(Decl.FuncDef node) {
        var symbol = node.resolution;

        var func = curMod.newFunction(symbol.name, symbol.funcType);
        var entryBB = func.addNewBlock();

        curFunc = func;
        symbol.address = func;

        func.entry = entryBB;
        helper.changeBlock(entryBB);

        for (var arg : symbol.params) arg.address = func.addNewParam(arg.name, arg.type);
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
            if (initExpr != null) helper.store(varDef.resolution.address, visit(initExpr));
        }
    }

    public void visit(Stmt.Block node) {
        for (Stmt stmt : node.stmts) {
            visit(stmt);

            // 约定：如果语句不会把控制流交回，它应该把 helper 指向 null
            // 如： if (cond) return; else return;
            if (helper.getBlock() == null) break;

            // 一个 block 的 return、continue、break 后面的语句都是死语句
            if (stmt instanceof Stmt.Return || stmt instanceof Stmt.Continue || stmt instanceof Stmt.Break) break;
        }
    }

    public void visit(Stmt.Return node) {
        var retVal = node.value;

        if (retVal == null) helper.ret();
        else helper.ret(visit(retVal));
    }

    public void visit(Stmt.Assign node) {
        var target = node.target;
        var value = node.value;

        switch (target) {
            case Expr.VarAccess varAccess -> {
                var symbol = varAccess.resolution;
                helper.store(symbol.address, visit(value));
            }
            case Expr.IndexAccess indexAccess -> {
                var symbol = indexAccess.lhs.resolution;
                var indexes = indexAccess.indexes;
                var indexSize = indexes.size();
                var indices = new Value[indexSize];
                for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));
                var arr = helper.load(symbol.address);
                var getPtr = helper.getElementPtr(arr, indices);
                helper.store(getPtr, visit(value));
            }
            default -> unsupported(target);
        }
    }

    public void visit(Stmt.ExprEval node) {
        visit(node.expr);
    }

    public void visit(Stmt.If node) {
        var thenBB = new BasicBlock(curFunc);
        var elseBB = new BasicBlock(curFunc);
        var mergeBB = new BasicBlock(curFunc);

        var elseStmt = node.elseStmt;
        var hasElse = elseStmt != Stmt.Empty.INSTANCE;

        // 对整个表达式来说，值是 true 的走向就是 then，false 的走向就是 else 或者 merge
        visitCond(node.cond, thenBB, hasElse ? elseBB : mergeBB);

        var needMerge = false;

        curFunc.addBlock(thenBB);
        helper.changeBlock(thenBB);
        visit(node.thenStmt);
        // 如果里面有 break 等等，有可能本来就有 terminator，不能覆盖
        if (!helper.hasTerminator()) {
            needMerge = true;
            helper.jmp(mergeBB);
        }

        if (hasElse) {
            curFunc.addBlock(elseBB);
            helper.changeBlock(elseBB);

            visit(elseStmt);
            if (!helper.hasTerminator()) {
                needMerge = true;
                helper.jmp(mergeBB);
            }
        } else needMerge = true; // 如果没有 else，那肯定需要 merge block

        if (needMerge) {
            curFunc.addBlock(mergeBB);
            helper.changeBlock(mergeBB);
        } else helper.changeBlock(null); // 设为 null 方便检测继续插入的错误
    }

    public void visit(Stmt.While node) {
        var loopBB = new BasicBlock(curFunc);
        var mergeBB = new BasicBlock(curFunc);

        visitCond(node.cond, loopBB, mergeBB);

        loopBlocks.addLast(loopBB);
        loopExits.addLast(mergeBB);

        curFunc.addBlock(loopBB);
        helper.changeBlock(loopBB);
        visit(node.body);
        // 需要重新求值 condVal，但是，比如直接写一个 break 在结尾的时候就不需要
        if (!helper.hasTerminator()) visitCond(node.cond, loopBB, mergeBB);

        loopBlocks.removeLast();
        loopExits.removeLast();

        curFunc.addBlock(mergeBB);
        helper.changeBlock(mergeBB);
    }

    private void visitCond(Expr cond, BasicBlock trueTarget, BasicBlock falseTarget) {
        if (cond instanceof Expr.Binary binary) {
            switch (binary.op) {
                case AND -> {
                    var checkRhs = new BasicBlock(curFunc);
                    // lhs 为 false 直接跳 falseTarget，为 true 则检查 rhs
                    visitCond(binary.lhs, checkRhs, falseTarget);
                    curFunc.addBlock(checkRhs);
                    helper.changeBlock(checkRhs);
                    visitCond(binary.rhs, trueTarget, falseTarget);
                    return;
                }
                case OR -> {
                    var checkRhs = new BasicBlock(curFunc);
                    // lhs 为 true 直接跳 trueTarget，为 false 则检查 rhs
                    visitCond(binary.lhs, trueTarget, checkRhs);
                    curFunc.addBlock(checkRhs);
                    helper.changeBlock(checkRhs);
                    visitCond(binary.rhs, trueTarget, falseTarget);
                    return;
                }
            }
        }
        var condVal = visit(cond);
        helper.br(condVal, trueTarget, falseTarget);
    }

    public void visit(Stmt.Break ignored) {
        if (loopExits.isEmpty()) {
            err("break statement not in loop");
            return;
        }

        var exitBB = loopExits.getLast();
        helper.jmp(exitBB);
    }

    public void visit(Stmt.Continue ignored) {
        if (loopBlocks.isEmpty()) {
            err("continue statement not in loop");
            return;
        }

        var loopBB = loopBlocks.getLast();
        helper.jmp(loopBB);
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

        return switch (node.op) {
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
    }

    public Value visit(Expr.Unary node) {
        var rhs = visit(node.rhs);
        if (node.op == Expr.Operator.ADD) return rhs;

        return switch (node.op) {
            case SUB -> helper.neg(rhs);
            case NOT -> helper.not(rhs);
            default -> (Instruction) unsupported(node.op);
        };
    }

    public Value visit(Expr.Call node) {
        var symbol = node.resolution;
        var args = new Value[node.args.size()];
        for (int i = 0; i < node.args.size(); i++) args[i] = visit(node.args.get(i));

        return symbol.isExternal ? helper.callExternal(symbol.funcType.returnType, args)
                : helper.call(symbol.address, args);
    }

    public Value visit(Expr.Assignable node) {
        return switch (node) {
            case Expr.VarAccess varAccess -> visit(varAccess);
            case Expr.IndexAccess indexAccess -> visit(indexAccess);
        };
    }

    public Value visit(Expr.VarAccess node) {
        var symbol = node.resolution;

        return helper.load(symbol.address);
    }

    public Value visit(Expr.IndexAccess node) {
        var symbol = node.lhs.resolution;
        var indexes = node.indexes;
        var indexSize = indexes.size();
        var indices = new Value[indexSize];
        for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));

        // 先要取 “变量类型的值” 指向的 “数组类型的值”
        var arr = helper.load(symbol.address);

        // 再计算 indices 偏移
        var elementPtr = helper.getElementPtr(arr, indices);

        return helper.load(elementPtr);
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

        if (node.toType == Types.Int) return helper.f2i(value);
        else if (node.toType == Types.Float) return helper.i2f(value);
        else return unsupported(value);
    }
}
