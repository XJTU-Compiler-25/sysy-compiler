package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.Terminator;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;

// 由于 sccp, gvn, inst combine 都得做常量折叠，把常量折叠计算的逻辑单独提取出来了
public final class ConstFold extends ModulePass {

    @Override
    public void visit(Module module) {
        run(module);
    }

    public static boolean run(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) {
            for (var block : function.blocks) {
                for (var inst : block.instructions) {
                    // 尝试对指令进行常量折叠
                    var folded = foldConstant(inst);
                    if (folded != null) {
                        inst.replaceAllUsesWith(folded);
                        modified = true;
                    }
                }
            }
        }
        return modified;
    }

    // 如果常量折叠成功了，就返回一个常量值，否则返回 null
    public static ImmediateValue foldConstant(Instruction inst) {
        ImmediateValue result = null;
        switch (inst) {
            // 不能被折叠为常量值的指令
            case Call _, CallExternal _, Alloca _, Load _, Store _, GetElemPtr _, Terminator _,
                Dummy _, Imm _, ILi _, FLi _, IMv _, FMv _, ICpy _, FCpy _ -> { }
            // 数学运算
            case IAdd it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lC) {
                    var lV = lC.value;
                    if (r instanceof IntConst rC) {
                        var rV = rC.value;
                        result = intConst(lV + rV);
                    }
                }
            }
            case ISub it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lC) {
                    var lV = lC.value;
                    if (r instanceof IntConst rC) {
                        var rV = rC.value;
                        result = intConst(lV - rV);
                    }
                }
            }
            case IMul it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lC) {
                    var lV = lC.value;
                    if (r instanceof IntConst rC) {
                        var rV = rC.value;
                        result = intConst(lV * rV);
                    }
                    else if (lV == 0) result = iZero;
                } else if (r instanceof IntConst rC) {
                    var rV = rC.value;
                    if (rV == 0) result = iZero;
                }
            }
            case IDiv it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lC) {
                    var lV = lC.value;
                    if (r instanceof IntConst rC) {
                        var rV = rC.value;
                        // 除 0 交给运行时行为
                        if (rV != 0) result = intConst(lV / rV);
                    }
                    // 0 / n = 0
                    else if (lV == 0) result = iZero;
                }
            }
            case IMod it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lC) {
                    var lV = lC.value;
                    if (r instanceof IntConst rC) {
                        var rV = rC.value;
                        // 除 0 交给运行时行为
                        if (rV != 0) result = intConst(lV % rV);
                    }
                    // 0 % n = 0
                    else if (lV == 0) result = iZero;
                } else if (r instanceof IntConst rC) {
                    var rV = rC.value;
                    // n % 1 = 0
                    if (rV == 1) result = iZero;
                }
            }
            case INeg it -> {
                var l = it.lhs.value;
                if (l instanceof IntConst lC) {
                    var lV = lC.value;
                    result = intConst(-lV);
                }
            }
            case FAdd it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    if (r instanceof FloatConst rConst) {
                        var rVal = rConst.value;
                        result = floatConst(lVal + rVal);
                    }
                }
            }
            case FSub it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    if (r instanceof FloatConst rConst) {
                        var rVal = rConst.value;
                        result = floatConst(lVal - rVal);
                    }
                }
            }
            case FMul it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    if (r instanceof FloatConst rConst) {
                        var rVal = rConst.value;
                        result = floatConst(lVal * rVal);
                    }
                    else if (lVal == 0.0f) result = floatConst(0.0f);
                }
            }
            case FDiv it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    if (r instanceof FloatConst rConst) {
                        var rVal = rConst.value;
                        result = floatConst(lVal / rVal);
                    } else if (lVal == 0.0f) result = floatConst(0.0f);
                }
            }
            case FMod it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    if (r instanceof FloatConst rConst) {
                        var rVal = rConst.value;
                        result = floatConst(lVal % rVal);
                    } else if (lVal == 0.0f) result = floatConst(0.0f);
                } else if (r instanceof FloatConst rConst) {
                    var rVal = rConst.value;
                    if (rVal == 1.0f) result = floatConst(0.0f);
                }
            }
            case FNeg it -> {
                var l = it.lhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    result = floatConst(-lVal);
                }
            }

            // 相等运算
            case IEq it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal == rVal ? iOne : iZero;
                }
            }
            case INe it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal != rVal ? iOne : iZero;
                }
            }
            case FEq it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal == rVal ? iOne : iZero;
                }
            }
            case FNe it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal != rVal ? iOne : iZero;
                }
            }

            // 比较运算
            case IGe it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal >= rVal ? iOne : iZero;
                }
            }
            case ILe it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal <= rVal ? iOne : iZero;
                }
            }
            case IGt it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal > rVal ? iOne : iZero;
                }
            }
            case ILt it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal < rVal ? iOne : iZero;
                }
            }
            case FGe it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal >= rVal ? iOne : iZero;
                }
            }
            case FLe it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal <= rVal ? iOne : iZero;
                }
            }
            case FGt it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal > rVal ? iOne : iZero;
                }
            }
            case FLt it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = lVal < rVal ? iOne : iZero;
                }
            }

            // 转型运算
            case I2F it -> {
                var v = it.value.value;
                if (v instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    result = floatConst((float) lVal);
                }
            }
            case F2I it -> {
                var v = it.value.value;
                if (v instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    result = intConst((int) lVal);
                }
            }
            case BitCastI2F it -> {
                var v = it.value.value;
                if (v instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    result = floatConst(Float.intBitsToFloat(lVal));
                }
            }
            case BitCastF2I it -> {
                var v = it.value.value;
                if (v instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    result = intConst(Float.floatToIntBits(lVal));
                }
            }

            // 位运算
            case Shl it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    // 如果右操作数大于等于 32 就留给运行时行为
                    if (rVal >= 0 && rVal < 32) result = intConst(lVal << rVal);
                }
            }
            // 逻辑右移
            case Shr it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    if (rVal >= 0 && rVal < 32) result = intConst(lVal >>> rVal);
                }
            }
            // 算数右移
            case AShr it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    if (rVal >= 0 && rVal < 32) result = intConst(lVal >> rVal);
                }
            }

            // 逻辑运算
            case And it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value != 0;
                    var rVal = rConst.value != 0;
                    result = lVal & rVal ? iOne : iZero;
                }
            }
            case Or it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value != 0;
                    var rVal = rConst.value != 0;
                    result = lVal | rVal ? iOne : iZero;
                }
            }
            case Xor it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof IntConst lConst && r instanceof IntConst rConst) {
                    var lVal = lConst.value != 0;
                    var rVal = rConst.value != 0;
                    result = lVal ^ rVal ? iOne : iZero;
                }
            }
            case Not it -> {
                var r = it.rhs.value;
                if (r instanceof IntConst rConst) {
                    var rVal = rConst.value != 0;
                    result = rVal ? iZero : iOne;
                }
            }

            // intrinsic
            case FAbs it -> {
                var l = it.lhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    result = floatConst(Math.abs(lVal));
                }
            }
            case FMax it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = floatConst(Math.max(lVal, rVal));
                }
            }
            case FMin it -> {
                var l = it.lhs.value;
                var r = it.rhs.value;
                if (l instanceof FloatConst lConst && r instanceof FloatConst rConst) {
                    var lVal = lConst.value;
                    var rVal = rConst.value;
                    result = floatConst(Math.min(lVal, rVal));
                }
            }
            case FSqrt it -> {
                var l = it.lhs.value;
                if (l instanceof FloatConst lConst) {
                    var lVal = lConst.value;
                    result = floatConst((float) Math.sqrt(lVal));
                }
            }
        }
        return result;
    }

}
