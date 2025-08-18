package cn.edu.xjtu.sysy.mir.pass;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.GlobalVar;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.mir.node.ImmediateValues;
import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

// 用于解释执行 MIR 代码，以检测正确性
public final class Interpreter extends ModulePass<Void> {
    private final PrintStream out;
    private final InputStream in;
    private final Scanner sc;
    private long startTime;
    private final HashMap<GlobalVar, ImmediateValue> globals = new HashMap<>();
    private final ArrayDeque<HashMap<Value, ImmediateValue>> stackframes = new ArrayDeque<>();
    private HashMap<Value, ImmediateValue> stackframe;
    private BasicBlock currentBlock;
    private final ArrayDeque<Value> retAddrs = new ArrayDeque<>();

    public Interpreter(PrintStream out, InputStream in) {
        this.out = out;
        this.in = in;
        this.sc = new Scanner(in);
    }

    private Module currentModule;
    @Override
    public void visit(Module module) {
        currentModule = module;
        globals.clear();
        stackframes.clear();
        stackframes.push(new HashMap<>());
        retAddrs.clear();
        retAddrs.push(iZero);
        stackframe = new HashMap<>();
        module.getGlobalVars().forEach(var -> {
            var init = module.globalVarInitValues.get(var);
            if (init instanceof SparseArray sp) init = sparseToDense(sp);
            else if (init instanceof ZeroInit) {
                var varType = var.varType;
                if (varType instanceof Type.Array arrType) init = zeroedDenseArray(arrType);
                else if (varType == Types.Int) init = iZero;
                else if (varType == Types.Float) init = fZero;
            }
            globals.put(var, init);
        });

        var main = module.getFunction("main");
        visit(main);
        var mainRet = stackframe.get(iZero);
        out.println(mainRet);
        out.flush();
    }

    @Override
    public void visit(Function function) {
        currentBlock = function.entry;
        while (currentBlock != null) {
            var terminator = currentBlock.terminator;
            for (var instruction : currentBlock.instructions)
                visit(instruction);

            visit(terminator);
        }
    }

    private int getInt(Value value) {
        return ((IntConst) toImm(value)).value;
    }

    private float getFloat(Value value) {
        return ((FloatConst) toImm(value)).value;
    }

    private Value[] getArray(Value value) {
        return ((DenseArray) toImm(value)).values;
    }

    private ImmediateValue toImm(Value value) {
        if (value instanceof ImmediateValue imm) return imm;
        if (value instanceof GlobalVar var) return globals.get(var);
        var val = stackframe.get(value);
        if (val != null) return val;
        throw new NoSuchElementException("Undefined value: " + value.shortName());
    }

    @Override
    public void visit(Instruction instruction) {
        try {
            switch (instruction) {
                case Call it -> {
                    var oldSF = stackframe;
                    var newSF = new HashMap<Value, ImmediateValue>();
                    var callee = it.getCallee();
                    var params = callee.params;
                    for (int i = 0, size = params.size(); i < size; i++) {
                        var param = params.get(i);
                        var arg = it.getArg(i);
                        var dummy = callee.paramToDummy.get(param.second());
                        var value = toImm(arg);
                        if (dummy != null) newSF.put(dummy, value);
                        newSF.put(param.second(), value);
                    }
                    retAddrs.push(it);
                    stackframes.push(oldSF);
                    stackframe = newSF;
                    visit(callee);
                }
                case CallExternal it -> {
                    switch (it.function) {
                        case GETINT -> {
                            var value = sc.nextInt();
                            stackframe.put(it, intConst(value));
                        }
                        case GETCH -> {
                            var value = in.read();
                            stackframe.put(it, intConst(value));
                        }
                        case GETFLOAT -> {
                            var value = sc.nextFloat();
                            stackframe.put(it, floatConst(value));
                        }
                        case GETARRAY -> {
                            var count = sc.nextInt();
                            stackframe.put(it, intConst(count));
                            var ptr = getArray(it.getArg(0));
                            for (int i = 0; i < count; i++) ptr[i] = intConst(sc.nextInt());
                        }
                        case GETFARRAY -> {
                            var count = sc.nextInt();
                            stackframe.put(it, intConst(count));
                            var ptr = getArray(it.getArg(0));
                            for (int i = 0; i < count; i++) ptr[i] = floatConst(sc.nextFloat());
                        }
                        case PUTINT -> {
                            var value = getInt(it.getArg(0));
                            out.print(value);
                        }
                        case PUTCH -> {
                            var value = (char) getInt(it.getArg(0));
                            out.print(value);
                        }
                        case PUTFLOAT -> {
                            var value = getFloat(it.getArg(0));
                            out.print(value);
                        }
                        case PUTARRAY -> {
                            var count = getInt(it.getArg(0));
                            var ptr = getArray(it.getArg(1));
                            for (int i = 0; i < count; i++) {
                                var value = getInt(ptr[i]);
                                out.print(value);
                                if (i != count - 1) out.print(" ");
                            }
                        }
                        case PUTFARRAY -> {
                            var count = getInt(it.getArg(0));
                            var ptr = getArray(it.getArg(1));
                            for (int i = 0; i < count; i++) {
                                var value = getFloat(ptr[i]);
                                out.print(value);
                                if (i != count - 1) out.print(" ");
                            }
                        }
                        case STARTTIME -> {
                            startTime = System.currentTimeMillis();
                        }
                        case STOPTIME -> {
                            var time = System.currentTimeMillis() - startTime;
                            out.printf("Time: %d ms%n", time);
                        }
                    }
                }
                case Ret it -> {
                    var retVal = toImm(it.getRetVal());
                    stackframe = stackframes.pop();
                    var retAddr = retAddrs.pop();
                    stackframe.put(retAddr, retVal);
                    currentBlock = null;
                }
                case RetV it -> {
                    retAddrs.pop();
                    stackframe = stackframes.pop();
                    currentBlock = null;
                }
                case Jmp it -> {
                    var target = it.getTarget();
                    it.params.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                    currentBlock = target;
                }
                case Br it -> {
                    var cond = toImm(it.getCondition());
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Instruction.BEq it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    var cond = lhs == rhs ? iOne : iZero;
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Instruction.BNe it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    var cond = lhs != rhs ? iOne : iZero;
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Instruction.BLt it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    var cond = lhs < rhs ? iOne : iZero;
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Instruction.BLe it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    var cond = lhs <= rhs ? iOne : iZero;
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Instruction.BGt it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    var cond = lhs > rhs ? iOne : iZero;
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Instruction.BGe it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    var cond = lhs >= rhs ? iOne : iZero;
                    if (!cond.equals(iZero)) {
                        it.trueParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getTrueTarget();
                    } else {
                        it.falseParams.forEach((arg, use) -> stackframe.put(arg, toImm(use.value)));
                        currentBlock = it.getFalseTarget();
                    }
                }
                case Alloca it -> {
                    switch (it.allocatedType) {
                        case Type.Array ty -> {
                            var array = zeroedDenseArray(ty);
                            stackframe.put(it, array);
                        }
                        default -> {
                            stackframe.put(it, ImmediateValues.undefined());
                        }
                    }
                }
                case Load it -> {
                    var addr = it.getAddress();
                    switch (addr) {
                        case GlobalVar var -> stackframe.put(it, globals.get(var));
                        case GetElemPtr gep -> {
                            var bv = gep.getBasePtr();
                            var base = (DenseArray) toImm(bv);
                            var strides = Types.strides(bv.type);
                            var offset = 0;
                            for (int i = 0, len = gep.getIndexCount(); i < len; i++)
                                offset += getInt(gep.getIndex(i)) * strides[i];
                            stackframe.put(it, toImm(base.values[offset]));
                        }
                        default -> stackframe.put(it, toImm(addr));
                    }
                }
                case Store it -> {
                    var addr = it.getAddress();
                    var value = toImm(it.getStoreVal());
                    switch (addr) {
                        case GlobalVar var -> globals.put(var, value);
                        case GetElemPtr gep -> {
                            var bv = gep.getBasePtr();
                            var base = (DenseArray) toImm(bv);
                            var strides = Types.strides(bv.type);
                            var offset = 0;
                            for (int i = 0, len = gep.getIndexCount(); i < len; i++)
                                offset += getInt(gep.getIndex(i)) * strides[i];
                            base.values[offset] = value;
                        }
                        default -> stackframe.put(it, toImm(addr));
                    }
                }
                case GetElemPtr it -> {
                    if (it.getIndices().stream().allMatch(v -> v.equals(iZero)))
                        stackframe.put(it, toImm(it.getBasePtr()));
                    // 延迟计算
                }
                // 数学运算
                case IAdd it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs + rhs));
                }
                case ISub it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs - rhs));
                }
                case IMul it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs * rhs));
                }
                case IDiv it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs / rhs));
                }
                case IMod it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs % rhs));
                }
                case INeg it -> {
                    var lhs = getInt(it.operand.value);
                    stackframe.put(it, intConst(-lhs));
                }
                case FAdd it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(lhs + rhs));
                }
                case FSub it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(lhs - rhs));
                }
                case FMul it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(lhs * rhs));
                }
                case FDiv it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(lhs / rhs));
                }
                case FMod it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(lhs % rhs));
                }
                case FNeg it -> {
                    var lhs = getFloat(it.operand.value);
                    stackframe.put(it, floatConst(-lhs));
                }

                // 相等运算
                case IEq it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, lhs == rhs ? iOne : iZero);
                }
                case INe it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, lhs != rhs ? iOne : iZero);
                }
                case FEq it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, lhs == rhs ? iOne : iZero);
                }
                case FNe it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, lhs != rhs ? iOne : iZero);
                }

                // 比较运算
                case IGe it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, lhs >= rhs ? iOne : iZero);
                }
                case ILe it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, lhs <= rhs ? iOne : iZero);
                }
                case IGt it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, lhs > rhs ? iOne : iZero);
                }
                case ILt it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, lhs < rhs ? iOne : iZero);
                }
                case FGe it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, lhs >= rhs ? iOne : iZero);
                }
                case FLe it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, lhs <= rhs ? iOne : iZero);
                }
                case FGt it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, lhs > rhs ? iOne : iZero);
                }
                case FLt it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, lhs < rhs ? iOne : iZero);
                }

                // 转型运算
                case I2F it -> {
                    var value = getInt(it.getOperand());
                    stackframe.put(it, floatConst(value));
                }
                case F2I it -> {
                    var value = getFloat(it.getOperand());
                    stackframe.put(it, intConst((int) value));
                }
                case BitCastI2F it -> {
                    var value = getInt(it.getOperand());
                    stackframe.put(it, floatConst(Float.intBitsToFloat(value)));
                }
                case BitCastF2I it -> {
                    var value = getFloat(it.getOperand());
                    stackframe.put(it, intConst(Float.floatToIntBits(value)));
                }

                // 位运算
                case Shl it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs << rhs));
                }
                // 逻辑右移
                case Shr it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs >>> rhs));
                }
                // 算数右移
                case AShr it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs >> rhs));
                }

                // 逻辑运算
                case And it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs & rhs));
                }
                case Or it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs | rhs));
                }
                case Xor it -> {
                    var lhs = getInt(it.getLhs());
                    var rhs = getInt(it.getRhs());
                    stackframe.put(it, intConst(lhs ^ rhs));
                }
                case Not it -> {
                    var value = getInt(it.operand.value);
                    stackframe.put(it, value == 0 ? iOne : iZero);
                }

                // intrinsic
                case FAbs it -> {
                    var lhs = getFloat(it.getOperand());
                    stackframe.put(it, floatConst(Math.abs(lhs)));
                }
                case FMax it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(Math.max(lhs, rhs)));
                }
                case FMin it -> {
                    var lhs = getFloat(it.getLhs());
                    var rhs = getFloat(it.getRhs());
                    stackframe.put(it, floatConst(Math.min(lhs, rhs)));
                }
                case FSqrt it -> {
                    var lhs = getFloat(it.getOperand());
                    stackframe.put(it, floatConst((float) Math.sqrt(lhs)));
                }
                case ILi it -> {
                    stackframe.put(it, intConst(it.imm));
                }
                case FLi it -> {
                    stackframe.put(it, intConst(Float.floatToRawIntBits(it.imm)));
                }
                case IMv it -> {
                    var src = toImm(it.src.value);
                    stackframe.put(it.dst, src);
                }
                case FMv it -> {
                    var src = toImm(it.src.value);
                    stackframe.put(it.dst, src);
                }
                case Instruction.ICpy it -> {
                    stackframe.put(it, toImm(it.src.value));
                }
                case Instruction.FCpy it -> {
                    stackframe.put(it, toImm(it.src.value));
                }
                case Instruction.Addi it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs + it.imm));
                }
                case Instruction.Andi it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs & it.imm));
                }
                case Instruction.Ori it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs | it.imm));
                }
                case Instruction.Xori it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs ^ it.imm));
                }
                case Instruction.Slli it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs << it.imm));
                }
                case Instruction.Srli it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs >> it.imm));
                }
                case Instruction.Srai it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, intConst(lhs >>> it.imm));
                }
                case Instruction.Slti it -> {
                    var lhs = getInt(it.getLhs());
                    stackframe.put(it, lhs < it.imm ? iOne : iZero);
                }
                case Dummy _ -> {
                    // Ignore
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
