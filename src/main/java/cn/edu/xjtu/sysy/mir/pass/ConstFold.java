package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;

import java.util.ArrayDeque;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public final class ConstFold extends ModuleVisitor {
    public ConstFold(ErrManager errManager) {
        super(errManager);
    }

    private final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(BasicBlock block) {
        var worklist = new ArrayDeque<>(block.instructions);

        while (!worklist.isEmpty()) {
            var inst = worklist.removeFirst();

            // 诸如 jmp br store 之类不能被折叠为常量值的就不做
            switch (inst) {
                case Call _, CallExternal _, Alloca _, Load _, Store _, GetElemPtr _ -> { }
                // 工作列表里没有加 terminator
                case Terminator _ -> unreachable();
                default -> {
                    var used = inst.used;
                    // 一个指令所有操作数都是立即数时，才可以进行常量折叠
                    if (!used.stream().allMatch(it -> it.value instanceof ImmediateValue)) continue;

                    ImmediateValue result = null;
                    // 进行常量折叠 + 传播
                    switch (inst) {
                        // 数学运算
                        case IAdd it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = intConst(lhs + rhs);
                        }
                        case ISub it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = intConst(lhs - rhs);
                        }
                        case IMul it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = intConst(lhs * rhs);
                        }
                        case IDiv it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            // 除 0 留给运行时行为
                            if (rhs != 0) result = intConst(lhs / rhs);
                        }
                        case IMod it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            // 取模 0 留给运行时行为
                            if (rhs != 0) result = intConst(lhs % rhs);
                        }
                        case FAdd it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(lhs + rhs);
                        }
                        case FSub it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(lhs - rhs);
                        }
                        case FMul it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(lhs * rhs);
                        }
                        // 浮点数似乎可以除 0
                        case FDiv it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(lhs / rhs);
                        }
                        case FMod it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(lhs % rhs);
                        }
                        case FNeg it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            result = floatConst(-lhs);
                        }

                        // 相等运算
                        case IEq it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = lhs == rhs ? iTrue : iFalse;
                        }
                        case INe it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = lhs != rhs ? iTrue : iFalse;
                        }
                        case FEq it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = lhs == rhs ? iTrue : iFalse;
                        }
                        case FNe it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = lhs != rhs ? iTrue : iFalse;
                        }

                        // 比较运算
                        case IGe it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = lhs >= rhs ? iTrue : iFalse;
                        }
                        case ILe it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = lhs <= rhs ? iTrue : iFalse;
                        }
                        case IGt it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = lhs > rhs ? iTrue : iFalse;
                        }
                        case ILt it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            result = lhs < rhs ? iTrue : iFalse;
                        }
                        case FGe it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = lhs >= rhs ? iTrue : iFalse;
                        }
                        case FLe it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = lhs <= rhs ? iTrue : iFalse;
                        }
                        case FGt it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = lhs > rhs ? iTrue : iFalse;
                        }
                        case FLt it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = lhs < rhs ? iTrue : iFalse;
                        }

                        // 转型运算
                        case I2F it -> {
                            var lhs = ((IntConst) it.value.value).value;
                            result = floatConst((float) lhs);
                        }
                        case F2I it -> {
                            var lhs = ((FloatConst) it.value.value).value;
                            result = intConst((int) lhs);
                        }
                        case BitCastI2F it -> {
                            var lhs = ((IntConst) it.value.value).value;
                            result = floatConst(Float.intBitsToFloat(lhs));
                        }
                        case BitCastF2I it -> {
                            var lhs = ((FloatConst) it.value.value).value;
                            result = intConst(Float.floatToIntBits(lhs));
                        }

                        // 位运算
                        case Shl it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            // 如果右操作数大于等于 32 就留给运行时行为
                            if (rhs >= 0 && rhs < 32) result = intConst(lhs << rhs);
                        }
                        // 逻辑右移
                        case Shr it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            // 如果右操作数大于等于 32 就留给运行时行为
                            if (rhs >= 0 && rhs < 32) result = intConst(lhs >>> rhs);
                        }
                        // 算数右移
                        case AShr it -> {
                            var lhs = ((IntConst) it.lhs.value).value;
                            var rhs = ((IntConst) it.rhs.value).value;
                            // 如果右操作数大于等于 32 就留给运行时行为
                            if (rhs >= 0 && rhs < 32) result = intConst(lhs >> rhs);
                        }

                        // 逻辑运算
                        case And it -> {
                            var lhs = ((IntConst) it.lhs.value).value != 0;
                            var rhs = ((IntConst) it.rhs.value).value != 0;
                            result = lhs & rhs ? iTrue : iFalse;
                        }
                        case Or it -> {
                            var lhs = ((IntConst) it.lhs.value).value != 0;
                            var rhs = ((IntConst) it.rhs.value).value != 0;
                            result = lhs | rhs ? iTrue : iFalse;
                        }
                        case Xor it -> {
                            var lhs = ((IntConst) it.lhs.value).value != 0;
                            var rhs = ((IntConst) it.rhs.value).value != 0;
                            result = lhs ^ rhs ? iTrue : iFalse;
                        }

                        // intrinsic
                        case FAbs it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            result = floatConst(Math.abs(lhs));
                        }
                        case FMax it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(Math.max(lhs, rhs));
                        }
                        case FMin it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            var rhs = ((FloatConst) it.rhs.value).value;
                            result = floatConst(Math.min(lhs, rhs));
                        }
                        case FSqrt it -> {
                            var lhs = ((FloatConst) it.lhs.value).value;
                            result = floatConst((float) Math.sqrt(lhs));
                        }
                        default -> unreachable();
                    }

                    // 如果常量折叠成功了
                    if (result != null) {
                        for (var use : inst.usedBy) {
                            var user = use.user;
                            // 使用了该指令的所有指令也应该再次检查，但不用检查终结指令
                            if (user instanceof Instruction instr && !(instr instanceof Instruction.Terminator))
                                worklist.add(instr);
                        }
                        inst.replaceAllUsesWith(result);
                    }
                }
            }
        }

        // 只有 br 有可能被折成 jmp
        if (block.terminator instanceof Instruction.Br br) {
            var cond = br.condition.value;
            if (cond instanceof ImmediateValue) {
                if (cond != iZero) {
                    helper.changeBlock(block);
                    var jmp = helper.jmp(br.trueTarget.value);
                    br.trueParams.forEach((key, value) -> jmp.putParam(key, value.value));
                    helper.changeBlock(null);
                    br.dispose();
                } else {
                    helper.changeBlock(block);
                    var jmp = helper.jmp(br.falseTarget.value);
                    br.falseParams.forEach((key, value) -> jmp.putParam(key, value.value));
                    helper.changeBlock(null);
                    br.dispose();
                }
            }
        }
    }
}
