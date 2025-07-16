package cn.edu.xjtu.sysy.mir;

import java.util.ArrayDeque;
import java.util.Arrays;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.InstructionHelper;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.unsupported;
import cn.edu.xjtu.sysy.util.Placeholder;

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
                symbol.address = mod.newGlobalVar(symbol, foldGlobalInit(varDef.init));
            } else if (decl instanceof Decl.FuncDef funcDef) {
                var symbol = funcDef.resolution;
                symbol.address = mod.newFunction(symbol.name, symbol.funcType);
                visit(funcDef);
            }
        }

        return mod;
    }

    private ImmediateValue foldGlobalInit(Expr node) {
        if (node == null) return ZeroInit;

        var ctv = node.getComptimeValue();
        if (ctv != null) {
            return switch (ctv) {
                case Integer intVal -> intConst(intVal);
                case Float floatVal -> floatConst(floatVal);
                default -> {
                    err("global variable initial value must be a constant expression, but got: " + node);
                    yield undefined();
                }
            };
        } else if (node instanceof Expr.Array arr) {
            var arrType = (Type.Array) arr.type;
            return sparseArrayOf(arrType, arrType.elementCount,
                    arr.indexes.stream().mapToInt(Integer::intValue).toArray(),
                    arr.elements.stream().map(this::foldGlobalInit).toArray(Value[]::new));
        } else {
            err("global variable initial value must be a constant expression, but got: " + node);
            return undefined();
        }
    }

    public void visit(Decl.FuncDef node) {
        var symbol = node.resolution;
        var funcType = symbol.funcType;

        var func = curMod.newFunction(symbol.name, funcType);
        var entryBB = func.addNewBlock();

        curFunc = func;
        symbol.address = func;

        func.entry = entryBB;
        helper.changeBlock(entryBB);

        for (var arg : symbol.params) arg.address = func.addNewParam(arg.name, arg.type);
        for (var var : node.allVars) var.address = var.type instanceof Type.Array arr ? helper.alloca(arr)
                : curFunc.addNewLocalVar(var.name, var.type);

        visit(node.body);

        if (helper.getBlock() != null && !helper.hasTerminator()) {
            var retType = funcType.returnType;
            if (retType == Types.Int) helper.ret(iZero);
            else if (retType == Types.Float) helper.ret(fZero);
            else if (retType == Types.Void) helper.ret();
        }
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
            var initExpr = varDef.init;
            if (initExpr != null) {
                var address = varDef.resolution.address;
                if (varDef.type instanceof Type.Array arr) {
                    if (!(initExpr instanceof Expr.Array init)) {
                        err("array variable must be initialized with an array literal, but got: " + initExpr);
                        continue;
                    }

                    var indices = new Value[arr.dimensions.length + 1];
                    Arrays.fill(indices, iZero);
                    var decayAll = helper.getElementPtr(address, indices);

                    var indexes = init.indexes;
                    var values = init.elements;
                    for (int i = 0, size = init.indexes.size(); i < size; i++) {
                        var value = visit(values.get(i));
                        var ptr = helper.getElementPtr(decayAll, intConst(indexes.get(i)));
                        helper.store(ptr, value);
                    }
                } else helper.store(address, visit(initExpr));
            }
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
                var indices = new Value[indexSize + 1];
                // “数组类型的变量”的值（Alloca） 的类型是 指向数组的指针，需要先解引用
                indices[0] = iZero;
                for (int i = 0; i < indexSize; i++) indices[i + 1] = visit(indexes.get(i));
                var getPtr = helper.getElementPtr(symbol.address, indices);
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
        if (helper.getBlock() != null && !helper.hasTerminator()) {
            needMerge = true;
            helper.jmp(mergeBB);
        }

        if (hasElse) {
            curFunc.addBlock(elseBB);
            helper.changeBlock(elseBB);
            visit(elseStmt);

            if (helper.getBlock() != null && !helper.hasTerminator()) {
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
        var condBB = new BasicBlock(curFunc);
        var loopBB = new BasicBlock(curFunc);
        var mergeBB = new BasicBlock(curFunc);

        curFunc.addBlock(condBB);
        helper.jmp(condBB);
        helper.changeBlock(condBB);

        visitCond(node.cond, loopBB, mergeBB);

        loopBlocks.addLast(loopBB);
        loopExits.addLast(mergeBB);

        curFunc.addBlock(loopBB);
        helper.changeBlock(loopBB);

        visit(node.body);
        if (helper.getBlock() != null && !helper.hasTerminator()) helper.jmp(condBB);

        loopBlocks.removeLast();
        loopExits.removeLast();

        curFunc.addBlock(mergeBB);
        helper.changeBlock(mergeBB);
    }

    private void visitCond(Expr cond, BasicBlock trueTarget, BasicBlock falseTarget) {
        if (cond instanceof Expr.Binary binary) {
            var ops = binary.operators;
            var opers = binary.operands;
            var firstOp = ops.getFirst();
            // 由于 binary 包含同一级的操作符，and 和 or 没有相同优先级的别的操作符
            // 所以判断第一个就知道整体是 and 还是 or 了
            if (firstOp == Expr.Operator.AND) {
                for (int i = 0, size = opers.size() - 1; i < size; i++) {
                    var oper = opers.get(i);
                    var checkRhs = new BasicBlock(curFunc);
                    visitCond(oper, checkRhs, falseTarget);
                    curFunc.addBlock(checkRhs);
                    helper.changeBlock(checkRhs);
                }
                visitCond(opers.getLast(), trueTarget, falseTarget);
                return;
            } else if (firstOp == Expr.Operator.OR) {
                for (int i = 0, size = opers.size() - 1; i < size; i++) {
                    var oper = opers.get(i);
                    var checkRhs = new BasicBlock(curFunc);
                    visitCond(oper, trueTarget, checkRhs);
                    curFunc.addBlock(checkRhs);
                    helper.changeBlock(checkRhs);
                }
                visitCond(opers.getLast(), trueTarget, falseTarget);
                return;
            }
            // 如果第一个操作符不是 and 或者 or 最后还会回落到下面普通表达式的
        }
        // 普通的表达式
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
        var opers = node.operands;
        var ops = node.operators;
        var result = visit(opers.getFirst());
        for (int i = 0, size = ops.size(); i < size; i++) {
            var op = ops.get(i);
            var oper = visit(opers.get(i + 1));
            result = switch (op) {
                case ADD -> helper.add(result, oper);
                case SUB -> helper.sub(result, oper);
                case MUL -> helper.mul(result, oper);
                case DIV -> helper.div(result, oper);
                case MOD -> helper.mod(result, oper);

                case EQ -> helper.eq(result, oper);
                case NE -> helper.ne(result, oper);
                case GT -> helper.gt(result, oper);
                case GE -> helper.ge(result, oper);
                case LT -> helper.lt(result, oper);
                case LE -> helper.le(result, oper);

                case AND -> helper.and(result, oper);
                case OR -> helper.or(result, oper);
                default -> (Instruction) unsupported(op);
            };
        }
        return result;
    }

    public Value visit(Expr.Unary node) {
        var result = visit(node.operand);
        for (var op : node.operators) {
            result = switch (op) {
                case ADD -> result; // unary plus, do nothing
                case SUB -> helper.neg(result);
                case NOT -> helper.not(result);
                default -> (Instruction) unsupported(node.operators);
            };
        }
        return result;
    }

    public Value visit(Expr.Call node) {
        var symbol = node.resolution;
        var args = new Value[node.args.size()];
        for (int i = 0; i < node.args.size(); i++) args[i] = visit(node.args.get(i));

        return symbol.isExternal ? helper.callBuiltin(symbol.name, args)
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
        var indices = new Value[indexSize + 1];
        indices[0] = iZero; // 数组类型的变量的值（Alloca） 的类型是 指向数组的指针，需要先解引用
        for (int i = 0; i < indexSize; i++) indices[i + 1] = visit(indexes.get(i));
        var elementPtr = helper.getElementPtr(symbol.address, indices);

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
        var type = (Type.Array) node.type;
        var values = node.elements.stream().map(this::visit).toArray(Value[]::new);
        var indexes = node.indexes.stream().mapToInt(Integer::intValue).toArray();
        return sparseArrayOf(type, type.elementCount, indexes, values);
    }

    public Value visit(Expr.Cast node) {
        var value = visit(node.value);

        return switch (node.kind) {
            case Float2Int -> helper.f2i(value);
            case Int2Float -> helper.i2f(value);
            case Ptr2Bool -> iOne; // !ptr 的场景 ptr 都当 true 吧
            case Decay -> helper.getElementPtr(value, iZero);
            case DecayAll -> {
                var indices = new Value[((Type.Array) node.value.type).dimensions.length];
                Arrays.fill(indices, iZero);
                yield helper.getElementPtr(value, indices);
            }
        };
    }
}
