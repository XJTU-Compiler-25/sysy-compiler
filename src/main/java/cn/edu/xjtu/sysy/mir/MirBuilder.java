package cn.edu.xjtu.sysy.mir;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Placeholder;

import static cn.edu.xjtu.sysy.util.Assertions.todo;
import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

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
    private InstructionHelper helper;

    public Module build(CompUnit compUnit) {
        return visit(compUnit);
    }

    public Module visit(CompUnit node) {
        var mod = new Module();
        curMod = mod;

        var globalInitBB = new BasicBlock("init-globals");

        for (var decl : node.decls) {
            switch (decl) {
                case Decl.VarDef varDef -> {
                    var symbol = varDef.resolution;
                    var globalVar = mod.newGlobalVar(symbol.name, symbol.type);
                    symbol.address = globalVar;
                    // 若没有初始化表达式，则自动使用零初始化
                    var initExpr = varDef.init;
                    var initInstruction = helper.store(globalVar, initExpr == null ? ImmediateValues.ZeroInit : visit(initExpr));
                    globalInitBB.addInstruction(initInstruction);
                }
                case Decl.FuncDef funcDef -> visit(funcDef);
                default -> unsupported(decl);
            }
        }

        var main = mod.functions.get("main");
        globalInitBB.setTerminator(helper.jmp(main.entry));
        main.entry = globalInitBB;
        main.blocks.addFirst(globalInitBB);

        return mod;
    }

    public void visit(Decl.FuncDef node) {
        helper = new InstructionHelper();

        var symbol = node.resolution;

        var func = curMod.newFunction(symbol.name, symbol.funcType);
        var entryBB = func.newBlock();

        curFunc = func;
        symbol.address = func;

        func.entry = entryBB;
        curBB = entryBB;

        for (Symbol.Var arg : symbol.params) {
            var bbArg = helper.newBlockArgument(arg.type, entryBB);
            arg.address = bbArg;
            entryBB.addArgument(bbArg);
        }
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
            var symbol = varDef.resolution;
            // 为局部变量分配栈上内存
            var varAddr = helper.alloca(symbol.type);
            symbol.address = varAddr;
            curBB.addInstruction(varAddr);

            // 若没有初始化表达式，则自动使用零初始化
            var initExpr = varDef.init;
            if (initExpr != null) {
                var initInstruction = helper.store(varAddr, visit(initExpr));
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
        curBB.setTerminator(retVal == null ? helper.ret() : helper.ret(visit(retVal)));
    }

    public void visit(Stmt.Assign node) {
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
                curBB.addInstruction(helper.getElementPtr(symbol.address, indices));
            }
            default -> unsupported(target);
        }
    }

    public void visit(Stmt.ExprEval node) {
        visit(node.expr);
    }

    public void visit(Stmt.If node) {
        var br = helper.br(visit(node.cond), null, null);
        curBB.setTerminator(br);

        var thenBB = curFunc.newBlock();
        br.trueTarget = thenBB;
        curBB = thenBB;
        visit(node.thenStmt);

        var elseStmt = node.elseStmt;
        if (elseStmt != Stmt.Empty.INSTANCE) {
            var elseBB = curFunc.newBlock();
            br.falseTarget = elseBB;
            curBB = elseBB;
            visit(node.elseStmt);

            var mergeBB = curFunc.newBlock();
            curBB = mergeBB;
            thenBB.setTerminator(helper.jmp(mergeBB));
            elseBB.setTerminator(helper.jmp(mergeBB));
        } else {
            // 如果没有 else 分支，则直接跳转到 mergeBB
            var mergeBB = curFunc.newBlock();
            br.falseTarget = mergeBB;
            curBB = mergeBB;
            thenBB.setTerminator(helper.jmp(mergeBB));
        }
    }

    public void visit(Stmt.While node) {
        var cond = node.cond;
        var brBeforeLoop = helper.br(visit(cond), null, null);
        curBB.setTerminator(brBeforeLoop);

        var loopBB = curFunc.newBlock();
        curBB = loopBB;
        visit(node.body);
        var brAfterLoop = helper.br(visit(cond), null, null);
        loopBB.setTerminator(brAfterLoop);
        brBeforeLoop.trueTarget = loopBB;
        brAfterLoop.trueTarget = loopBB;

        var mergeBB = curFunc.newBlock();
        curBB = mergeBB;
        brBeforeLoop.falseTarget = mergeBB;
        brAfterLoop.falseTarget = mergeBB;
    }

    public void visit(Stmt.Break node) {
        todo();
    }

    public void visit(Stmt.Continue node) {
        todo();
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
            case ADD -> {
                var i = helper.add(lhs, rhs);
                curBB.addInstruction(i);
                yield i;
            }
            case SUB -> {
                var i = helper.sub(lhs, rhs);
                curBB.addInstruction(i);
                yield i;
            }
            case MUL -> {
                var i = helper.mul(lhs, rhs);
                curBB.addInstruction(i);
                yield i;
            }
            case DIV -> {
                var i = helper.div(lhs, rhs);
                curBB.addInstruction(i);
                yield i;
            }
            case MOD -> {
                var i = helper.mod(lhs, rhs);
                curBB.addInstruction(i);
                yield i;
            }
            case EQ, NE, LT, LE, GT, GE -> todo();
            default -> unsupported(node.op);
        };
    }

    public Value visit(Expr.Unary node) {
        var rhs = visit(node.rhs);
        return switch (node.op) {
            case ADD -> rhs;
            case SUB -> {
                var i = helper.neg(rhs);
                curBB.addInstruction(i);
                yield i;
            }
            case NOT -> {
                var i = helper.not(rhs);
                curBB.addInstruction(i);
                yield i;
            }
            default -> unsupported(node.op);
        };
    }

    public Value visit(Expr.Call node) {
        var symbol = node.resolution;
        var args = new Value[node.args.size()];
        for (int i = 0; i < node.args.size(); i++) args[i] = visit(node.args.get(i));
        var call = helper.call(symbol.address, args);
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

        var load = helper.load(symbol.address);
        curBB.addInstruction(load);

        return load;
    }

    public Value visit(Expr.IndexAccess node) {
        var symbol = node.lhs.resolution;
        var indexes = node.indexes;
        var indexSize = indexes.size();
        var indices = new Value[indexSize];
        for (int i = 0; i < indexSize; i++) indices[i] = visit(indexes.get(i));

        var elementPtr = helper.getElementPtr(symbol.address, indices);
        curBB.addInstruction(elementPtr);

        var load = helper.load(elementPtr);
        curBB.addInstruction(load);

        return load;
    }

    public Value visit(Expr.Literal node) {
        var type = node.type;
        if (type == Types.Int)
            return ImmediateValues.intConst((Integer) node.getComptimeValue());
        else if (type == Types.Float)
            return ImmediateValues.floatConst((Float) node.getComptimeValue());

        return unsupported(node.type);
    }

    public Value visit(Expr.Array node) {
        var elements = node.elements;
        var elementCount = elements.size();
        var values = new Value[elementCount];
        for (int i = 0; i < elementCount; i++) values[i] = visit(elements.get(i));
        var indexes = node.indexes.stream().mapToInt(Integer::intValue).toArray();
        return ImmediateValues.sparseArrayOf(node.type, indexes, values);
    }

    public Value visit(Expr.Cast node) {
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
