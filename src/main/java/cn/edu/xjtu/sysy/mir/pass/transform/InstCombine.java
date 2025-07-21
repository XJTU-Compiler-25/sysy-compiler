package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

// 常量折叠和传播，指令合并
@SuppressWarnings("unchecked")
public final class InstCombine extends ModuleVisitor {
    public InstCombine(ErrManager errManager) {
        super(errManager);
    }

    private final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(BasicBlock block) {
        var worklist = new Worklist<>(block.instructions);

        while (!worklist.isEmpty()) {
            var inst = worklist.poll();
            var result = foldConstant(inst);
            // 如果指令被改变了
            if (result != null) {
                for (var use : inst.usedBy) {
                    var user = use.user;
                    // 使用了该指令的所有指令也应该再次检查，但不用检查终结指令
                    if (user instanceof Instruction instr && !(instr instanceof Instruction.Terminator))
                        worklist.add(instr);
                }
                inst.replaceAllUsesWith(result);
            } else combineInst(inst); // 常量折叠没有成功，尝试一下模式识别
        }
    }

    private Value foldConstant(Instruction inst) {
        return switch (inst) {
            // 诸如 jmp br store 之类不能被折叠为常量值的就不做
            case Call _, CallExternal _, Alloca _, Load _, Store _, GetElemPtr _ -> null;
            // 工作列表里没有加 terminator
            case Terminator _ -> unreachable();
            // 数学运算
            case IAdd it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield intConst(lVal + rVal);
                } else yield null;
            }
            case ISub it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield intConst(lVal - rVal);
                } else yield null;
            }
            case IMul it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield intConst(lVal * rVal);
                } else yield null;
            }
            case IDiv it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    if (rVal != 0) yield intConst(lVal / rVal);
                        // 除 0 留给运行时行为
                    else yield null;
                } else yield null;
            }
            case IMod it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    if (rVal != 0) yield intConst(lVal % rVal);
                        // 除 0 留给运行时行为
                    else yield null;
                } else yield null;
            }
            case INeg it -> {
                if (it.lhs.value instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    yield intConst(-lVal);
                } else yield null;
            }
            case FAdd it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(lVal + rVal);
                } else yield null;
            }
            case FSub it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(lVal - rVal);
                } else yield null;
            }
            case FMul it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(lVal * rVal);
                } else yield null;
            }
            // 浮点数似乎可以除 0
            case FDiv it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(lVal / rVal);
                } else yield null;
            }
            case FMod it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(lVal % rVal);
                } else yield null;
            }
            case FNeg it -> {
                if (it.lhs.value instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    yield floatConst(-lVal);
                } else yield null;
            }

            // 相等运算
            case IEq it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal == rVal ? iTrue : iFalse;
                } else yield null;
            }
            case INe it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal != rVal ? iTrue : iFalse;
                } else yield null;
            }
            case FEq it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal == rVal ? iTrue : iFalse;
                } else yield null;
            }
            case FNe it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal != rVal ? iTrue : iFalse;
                } else yield null;
            }

            // 比较运算
            case IGe it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal >= rVal ? iTrue : iFalse;
                } else yield null;
            }
            case ILe it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal <= rVal ? iTrue : iFalse;
                } else yield null;
            }
            case IGt it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal > rVal ? iTrue : iFalse;
                } else yield null;
            }
            case ILt it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal < rVal ? iTrue : iFalse;
                } else yield null;
            }
            case FGe it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal >= rVal ? iTrue : iFalse;
                } else yield null;
            }
            case FLe it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal <= rVal ? iTrue : iFalse;
                } else yield null;
            }
            case FGt it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal > rVal ? iTrue : iFalse;
                } else yield null;
            }
            case FLt it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield lVal < rVal ? iTrue : iFalse;
                } else yield null;
            }

            // 转型运算
            case I2F it -> {
                if (it.value.value instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    yield floatConst((float) lVal);
                } else yield null;
            }
            case F2I it -> {
                if (it.value.value instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    yield intConst((int) lVal);
                } else yield null;
            }
            case BitCastI2F it -> {
                if (it.value.value instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    yield floatConst(Float.intBitsToFloat(lVal));
                } else yield null;
            }
            case BitCastF2I it -> {
                if (it.value.value instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    yield intConst(Float.floatToIntBits(lVal));
                } else yield null;
            }

            // 位运算
            case Shl it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    // 如果右操作数大于等于 32 就留给运行时行为
                    if (rVal >= 0 && rVal < 32) yield intConst(lVal << rVal);
                    else yield null;
                } else yield null;
            }
            // 逻辑右移
            case Shr it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    if (rVal >= 0 && rVal < 32) yield intConst(lVal >>> rVal);
                    else yield null;
                } else yield null;
            }
            // 算数右移
            case AShr it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    if (rVal >= 0 && rVal < 32) yield intConst(lVal >> rVal);
                    else yield null;
                } else yield null;
            }

            // 逻辑运算
            case And it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value != 0;
                    var rVal = rConst.value != 0;
                    yield lVal & rVal ? iTrue : iFalse;
                } else yield null;
            }
            case Or it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value != 0;
                    var rVal = rConst.value != 0;
                    yield lVal | rVal ? iTrue : iFalse;
                } else yield null;
            }
            case Xor it -> {
                if (it.lhs.value instanceof IntConst lConst && it.rhs.value instanceof IntConst rConst) {
                    var lVal = lConst.value != 0;
                    var rVal = rConst.value != 0;
                    yield lVal ^ rVal ? iTrue : iFalse;
                } else yield null;
            }
            case Not it -> {
                if (it.rhs.value instanceof IntConst rConst) {
                    var rVal = rConst.value != 0;
                    yield rVal ? iFalse : iTrue;
                } else yield null;
            }

            // intrinsic
            case FAbs it -> {
                if (it.lhs.value instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    yield floatConst(Math.abs(lVal));
                } else yield null;
            }
            case FMax it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(Math.max(lVal, rVal));
                } else yield null;
            }
            case FMin it -> {
                if (it.lhs.value instanceof FloatConst lConst && it.rhs.value instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    yield floatConst(Math.min(lVal, rVal));
                } else yield null;
            }
            case FSqrt it -> {
                if (it.lhs.value instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    yield floatConst((float) Math.sqrt(lVal));
                } else yield null;
            }
        };
    }

    private void combineInst(Instruction inst) {
        switch (inst) {
            case Call _, CallExternal _, Alloca _, Load _, Store _ -> { }
            case Terminator _ -> unreachable();
            // 数学运算
            case IAdd it -> {
                var lUse = it.lhs;
                var rUse = it.rhs;
                var lhs = lUse.value;
                var rhs = rUse.value;

                if (lhs instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    if (lVal == 0) {
                        it.replaceAllUsesWith(rhs);
                        it.dispose();
                    } else if (rhs instanceof IAdd inner && inner.onlyOneUse()) {
                        var innerLhs = inner.lhs.value;
                        var innerRhs = inner.rhs.value;
                        if (innerLhs instanceof IntConst innerLConst) {
                            var innerLVal = innerLConst.value;
                            lUse.replaceValue(intConst(lVal + innerLVal));
                            rUse.replaceValue(innerRhs);
                        } else if (innerRhs instanceof IntConst innerRConst) {
                            var innerRVal = innerRConst.value;
                            lUse.replaceValue(intConst(lVal + innerRVal));
                            rUse.replaceValue(innerLhs);
                        }
                    }
                } else if (rhs instanceof IntConst rConst) {
                    var rVal = rConst.value;
                    if (rVal == 0) {
                        it.replaceAllUsesWith(lhs);
                        it.dispose();
                    } else if (lhs instanceof IAdd inner && inner.onlyOneUse()) {
                        var innerLhs = inner.lhs.value;
                        var innerRhs = inner.rhs.value;
                        if (innerLhs instanceof IntConst innerLConst) {
                            var innerLVal = innerLConst.value;
                            rUse.replaceValue(intConst(innerLVal + rVal));
                            lUse.replaceValue(innerRhs);
                        } else if (innerRhs instanceof IntConst innerRConst) {
                            var innerRVal = innerRConst.value;
                            rUse.replaceValue(intConst(innerRVal + rVal));
                            lUse.replaceValue(innerLhs);
                        }
                    }
                }
            }
            case GetElemPtr it -> {
                var base = it.basePtr.value;
                var indices = it.indices;
                var indexLen = indices.length;
                // 合并 GEP
                if (base instanceof GetElemPtr inner) {
                    var innerBase = inner.basePtr.value;
                    var innerIndices = inner.indices;
                    var innerIndexLen = innerIndices.length;
                    if (indices[0].value.equals(iZero)) {
                        // 外层的第一维是 0，则直接在外层的 indices 前面连接内层的 indices
                        it.basePtr.replaceValue(innerBase);
                        var newIndices = new Use[innerIndexLen + indexLen - 1];
                        for (int i = 0; i < innerIndexLen; i++) {
                            newIndices[i] = it.use(innerIndices[i].value);
                        }
                        indices[0].dispose();
                        if (indexLen > 1) System.arraycopy(indices, 1, newIndices, innerIndexLen, indexLen);
                        it.indices = newIndices;
                    } else if (indices[0].value instanceof IntConst firstIndex
                            && innerIndices[0].value instanceof IntConst innerLastIndex) {
                        // 外层的第一维不是 0，但内层的最后一维也是常数，则累加该维并连接其余 indices
                        it.basePtr.replaceValue(innerBase);
                        var newIndices = new Use[innerIndexLen + indexLen - 1];
                        for (int i = 0; i < innerIndexLen - 1; i++) {
                            newIndices[i] = it.use(innerIndices[i].value);
                        }
                        newIndices[innerIndexLen - 1] = it.use(intConst(firstIndex.value + innerLastIndex.value));
                        indices[0].dispose();
                        if (indexLen > 1) System.arraycopy(indices, 1, newIndices, innerIndexLen, indexLen);
                        it.indices = newIndices;
                    }
                }
            }
            default -> {}
        }
    }
}
