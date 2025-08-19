package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.*;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

// 函数内联
// 指令数少于阈值 且 非递归 的函数，调用会被内联
@SuppressWarnings("unused")
public final class Inline extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    // 被内联的函数的指令条数上限
    private static final int INLINE_THRESHOLD = 1600;

    private FuncInfo funcInfo;
    private final HashSet<Function> inlineCandidate = new HashSet<>();
    @Override
    public void visit(Module module) {
        var modified = false;
        do {
            modified = false;
            funcInfo = getRefreshedResult(FuncInfoAnalysis.class);
            selectCandidate(module.getFunctions());
            for (var function : module.getFunctions()) modified |= processFunction(function);
        } while (modified);
    }

    private void selectCandidate(Collection<Function> functions) {
        inlineCandidate.clear();
        for (var function : functions) {
            if (funcInfo.isRecursive(function)) continue;
            // if (function.getAllInstructions().size() > INLINE_THRESHOLD) continue;
            inlineCandidate.add(function);
        }
    }

    private boolean isInliningCandidate(Function function) {
        return inlineCandidate.contains(function);
    }

    private boolean processFunction(Function function) {
        var modified = false;
        var changed = false;
        outer: do {
            changed = false;
            for (var block : function.blocks) {
                if (processBlock(block)) {
                    changed = true;
                    modified = true;
                    continue outer;
                }
            }
        } while (changed);
        return modified;
    }


    private final HashMap<Value, Value> valueCopy = new HashMap<>();
    private Value getClonedValue(Value value) {
        if (value instanceof BasicBlock || value instanceof Function) return unreachable();
        if (value instanceof ImmediateValue || value instanceof GlobalVar) return value;
        if (value instanceof BlockArgument || value instanceof Instruction)
            if (valueCopy.containsKey(value)) return valueCopy.get(value);

        throw new NoSuchElementException("not found " + value);
    }

    private final HashMap<BasicBlock, BasicBlock> blockCopy = new HashMap<>();
    private boolean processBlock(BasicBlock callBlock) {
        // 内联到一个就直接返回，所以不用建副本
        for (int idx = 0, instructionsSize = callBlock.getInstructionCount(); idx < instructionsSize; idx++) {
            var inst = callBlock.getInstruction(idx);
            if (!(inst instanceof Instruction.Call call)) continue;
            var callee = call.getCallee();
            var caller = callBlock.getFunction();
            // 没有被选择为内联对象
            // 注意，由于如果 caller 递归，不会被选择为 inlining candidate，所以不用判断 caller == callee
            if (!isInliningCandidate(callee)) continue;

            invalidate(CFGAnalysis.class);
            var domInfo = getRefreshedResult(DominanceAnalysis.class);
            valueCopy.clear();
            blockCopy.clear();

            var continuationBlock = new BasicBlock(caller);
            caller.addBlock(continuationBlock);

            var callType = call.type;
            BlockArgument retValArg = null;
            if (callType != Types.Void) {
                retValArg = continuationBlock.addBlockArgument(callType);
                call.replaceAllUsesWith(retValArg);
            }

            // 由于后面会直接 return 掉，直接在 call 的 idx 这里把所有 call 后面的指令都移动掉
            callBlock.removeInstruction(idx).dispose(); // 删掉 call
            // 删到 call idx = block size 为止 (call 及后面的指令全部被移动走
            while (idx < callBlock.getInstructionCount()) {
                var toMove = callBlock.removeInstruction(idx);
                continuationBlock.insertAtLast(toMove);
            }

            var originalTerm = callBlock.terminator;
            callBlock.removeTerminator();
            continuationBlock.setTerminator(originalTerm);

            for (var oldBlock : callee.blocks) {
                var newBlock = new BasicBlock(caller);
                blockCopy.put(oldBlock, newBlock);
                caller.addBlock(newBlock);

                if (oldBlock != callee.entry) {
                    for (var oldArg : oldBlock.args) {
                        var newArg = newBlock.addBlockArgument(oldArg.type);
                        valueCopy.put(oldArg, newArg);
                    }
                }
            }

            var newEntry = blockCopy.get(callee.entry);
            helper.changeBlock(callBlock);
            helper.insertJmp(newEntry);
            helper.changeBlockToNull();

            var callArgs = call.getArgs();
            var params = callee.params;
            for (int i = 0; i < callArgs.size(); i++) {
                var arg = callArgs.get(i);
                var param = params.get(i).second();
                valueCopy.put(param, arg);
            }

            var dfn = domInfo.getDFN(callee);
            for (var oldBlock : dfn) {
                var newBlock = blockCopy.get(oldBlock);

                for (var oldInst : oldBlock.instructions) {
                    var newInst = cloneInstruction(newBlock, oldInst, valueCopy);
                    newBlock.insertAtLast(newInst);
                    valueCopy.put(oldInst, newInst);
                }

                var oldTerm = oldBlock.terminator;
                var newTerm = cloneTerminator(newBlock, oldTerm, valueCopy, blockCopy, continuationBlock, retValArg);
                newBlock.setTerminator(newTerm);
            }
            return true;
        }
        return false;
    }

    private Terminator cloneTerminator(
            BasicBlock block,
            Terminator terminator,
            HashMap<Value, Value> valueCopy,
            HashMap<BasicBlock, BasicBlock> blockCopy,
            BasicBlock retBlock,
            BlockArgument retValBlockArg
    ) {
        helper.changeBlock(block);
        Terminator result = switch (terminator) {
            case Instruction.Jmp it -> {
                var target = blockCopy.get(it.getTarget());
                var jmp = helper.jmp(target);
                it.params.forEach((arg, use) -> {
                    var newArg = (BlockArgument) getClonedValue(arg);
                    var value = getClonedValue(use.value);
                    jmp.putParam(newArg, value);
                });
                yield jmp;
            }
            case Instruction.Br it -> {
                var cond = getClonedValue(it.getCondition());
                var tp = blockCopy.get(it.getTrueTarget());
                var fp =  blockCopy.get(it.getFalseTarget());
                var br = helper.br(cond, tp, fp);
                it.trueParams.forEach((arg, use) -> {
                    var newArg = (BlockArgument) getClonedValue(arg);
                    var value = getClonedValue(use.value);
                    br.putTrueParam(newArg, value);
                });
                it.falseParams.forEach((arg, use) -> {
                    var newArg = (BlockArgument) getClonedValue(arg);
                    var value = getClonedValue(use.value);
                    br.putFalseParam(newArg, value);
                });
                yield br;
            }
            case Instruction.Ret it -> {
                var retVal = getClonedValue(it.getRetVal());
                var jmp = helper.jmp(retBlock);
                jmp.putParam(retValBlockArg, retVal);
                yield jmp;
            }
            case Instruction.RetV _ -> helper.jmp(retBlock);
            default -> unreachable();
        };
        helper.changeBlockToNull();
        return result;
    }

    private Instruction cloneInstruction(
            BasicBlock block,
            Instruction instruction,
            HashMap<Value, Value> valueCopy
    ) {
        helper.changeBlock(block);
        Instruction result = switch (instruction) {
            case Alloca it -> helper.alloca(it.allocatedType);
            case Load it -> helper.load(getClonedValue(it.getAddress()));
            case Store store -> {
                var addr = getClonedValue(store.getAddress());
                var val = getClonedValue(store.getStoreVal());
                yield helper.store(addr, val);
            }
            case GetElemPtr it -> {
                var base = getClonedValue(it.getBasePtr());
                var indices = it.getIndices().stream().map(this::getClonedValue).toArray(Value[]::new);
                yield helper.getElementPtr(base, indices);
            }
            // 整数运算指令
            case IAdd it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.add(lhs, rhs);
            }
            case ISub it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.sub(lhs, rhs);
            }
            case IMul it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.mul(lhs, rhs);
            }
            case IDiv it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.div(lhs, rhs);
            }
            case IMod it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.mod(lhs, rhs);
            }
            case INeg it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.neg(operand);
            }
            // 浮点运算指令
            case FAdd it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.add(lhs, rhs);
            }
            case FSub it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.sub(lhs, rhs);
            }
            case FMul it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.mul(lhs, rhs);
            }
            case FDiv it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.div(lhs, rhs);
            }
            case FMod it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.mod(lhs, rhs);
            }
            case FNeg it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.neg(operand);
            }
            // 位运算指令
            case Shl it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.shl(lhs, rhs);
            }
            case Shr it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.shr(lhs, rhs);
            }
            case AShr it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.ashr(lhs, rhs);
            }
            case And it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.and(lhs, rhs);
            }
            case Or it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.or(lhs, rhs);
            }
            case Xor it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.xor(lhs, rhs);
            }
            case Not it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.not(operand);
            }
            // 比较指令
            case IEq it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.eq(lhs, rhs);
            }
            case INe it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.ne(lhs, rhs);
            }
            case IGt it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.gt(lhs, rhs);
            }
            case ILt it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.lt(lhs, rhs);
            }
            case IGe it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.ge(lhs, rhs);
            }
            case ILe it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.le(lhs, rhs);
            }
            case FEq it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.eq(lhs, rhs);
            }
            case FNe it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.ne(lhs, rhs);
            }
            case FGt it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.gt(lhs, rhs);
            }
            case FLt it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.lt(lhs, rhs);
            }
            case FGe it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.ge(lhs, rhs);
            }
            case FLe it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.le(lhs, rhs);
            }
            // 类型转换指令
            case I2F it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.i2f(operand);
            }
            case F2I it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.f2i(operand);
            }
            case BitCastI2F it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.bitCastI2F(operand);
            }
            case BitCastF2I it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.bitCastF2I(operand);
            }
            // 内建函数指令
            case FSqrt it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.fsqrt(operand);
            }
            case FAbs it -> {
                var operand = getClonedValue(it.getOperand());
                yield helper.fabs(operand);
            }
            case FMin it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.fmin(lhs, rhs);
            }
            case FMax it -> {
                var lhs = getClonedValue(it.getLhs());
                var rhs = getClonedValue(it.getRhs());
                yield helper.fmax(lhs, rhs);
            }
            // 函数调用指令
            case Call it -> {
                var args = it.getArgs().stream().map(this::getClonedValue).toArray(Value[]::new);
                yield helper.call(it.getCallee(), args);
            }
            case CallExternal it -> {
                var args = it.getArgs().stream().map(this::getClonedValue).toArray(Value[]::new);
                yield helper.callBuiltin(it.function, args);
            }
            case Terminator _, Dummy _, FCpy _, FLi _, FMv _, ICpy _, ILi _, IMv _, Imm _ -> unreachable();
        };
        helper.changeBlockToNull();
        return result;
    }

}
