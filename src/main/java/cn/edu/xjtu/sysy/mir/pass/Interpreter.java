package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.*;

// 用于解释执行 MIR 代码，以检测正确性
public final class Interpreter extends ModuleVisitor<Void> {
    private final PrintStream out;
    private final InputStream in;
    private final Scanner sc;
    private long startTime;
    private final HashMap<Var, ImmediateValue> globals = new HashMap<>();
    private final ArrayDeque<HashMap<Value, ImmediateValue>> stackframes = new ArrayDeque<>();
    private HashMap<Value, ImmediateValue> stackframe;
    private BasicBlock currentBlock;
    private final ArrayDeque<Value> retAddrs = new ArrayDeque<>();

    public Interpreter(PrintStream out, InputStream in) {
        super(null);
        this.out = out;
        this.in = in;
        this.sc = new Scanner(in);
    }

    @Override
    public void visit(Module module) {
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
        if (value instanceof Var var && var.isGlobal) return globals.get(var);
        if (value instanceof BlockArgument arg) value = arg.getVar();
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
                    var callee = it.getFunction();
                    var params = callee.params;
                    for (int i = 0, size = params.size(); i < size; i++) {
                        var param = params.get(i);
                        var arg = it.args[i].value;
                        newSF.put(param, toImm(arg));
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
                            var ptr = getArray(it.args[0].value);
                            for (int i = 0; i < count; i++) ptr[i] = intConst(sc.nextInt());
                        }
                        case GETFARRAY -> {
                            var count = sc.nextInt();
                            stackframe.put(it, intConst(count));
                            var ptr = getArray(it.args[0].value);
                            for (int i = 0; i < count; i++) ptr[i] = floatConst(sc.nextFloat());
                        }
                        case PUTINT -> {
                            var value = getInt(it.args[0].value);
                            out.print(value);
                        }
                        case PUTCH -> {
                            var value = (char) getInt(it.args[0].value);
                            out.print(value);
                        }
                        case PUTFLOAT -> {
                            var value = getFloat(it.args[0].value);
                            out.print(value);
                        }
                        case PUTARRAY -> {
                            var count = getInt(it.args[0].value);
                            var ptr = getArray(it.args[1].value);
                            for (int i = 0; i < count; i++) {
                                var value = getInt(ptr[i]);
                                out.print(value);
                                if (i != count - 1) out.print(" ");
                            }
                        }
                        case PUTFARRAY -> {
                            var count = getInt(it.args[0].value);
                            var ptr = getArray(it.args[1].value);
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
                    var retVal = toImm(it.retVal.value);
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
                    it.params.forEach((var, use) -> stackframe.put(var, toImm(use.value)));
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
                case Alloca it -> {
                    var ty = (Type.Array) it.allocatedType;
                    var array = zeroedDenseArray(ty);
                    stackframe.put(it, array);
                }
                case Load it -> {
                    var addr = it.address.value;
                    switch (addr) {
                        case Var var -> {
                            if (var.isGlobal) stackframe.put(it, globals.get(var));
                            else if (var.isParam) stackframe.put(it, toImm(it));
                            else unreachable();
                        }
                        case ImmediateValue imm -> stackframe.put(it, imm);
                        case GetElemPtr gep -> {
                            var bv = gep.basePtr.value;
                            var base = (DenseArray) toImm(bv);
                            var strides = Types.strides(bv.type);
                            var indices = gep.indices;
                            var offset = 0;
                            for (int i = 0, len = indices.length; i < len; i++)
                                offset += getInt(indices[i].value) * strides[i];
                            stackframe.put(it, toImm(base.values[offset]));
                        }
                        default -> unsupported(addr);
                    }
                }
                case Store it -> {
                    var addr = it.address.value;
                    var value = toImm(it.storeVal.value);
                    switch (addr) {
                        case Var var -> {
                            if (var.isGlobal) globals.put(var, value);
                            else if (var.isParam) stackframe.put(it, value);
                            else unsupported(addr);
                        }
                        case GetElemPtr gep -> {
                            var bv = gep.basePtr.value;
                            var base = (DenseArray) toImm(bv);
                            var strides = Types.strides(bv.type);
                            var indices = gep.indices;
                            var offset = 0;
                            for (int i = 0, len = indices.length; i < len; i++)
                                offset += getInt(indices[i].value) * strides[i];
                            base.values[offset] = value;
                        }
                        default -> unsupported(addr);
                    }
                }
                case GetElemPtr it -> {
                    if (Arrays.stream(it.indices).allMatch(use -> use.value.equals(iZero)))
                        stackframe.put(it, toImm(it.basePtr.value));
                    // 延迟计算
                }
                // 数学运算
                case IAdd it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs + rhs));
                }
                case ISub it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs - rhs));
                }
                case IMul it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs * rhs));
                }
                case IDiv it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs / rhs));
                }
                case IMod it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs % rhs));
                }
                case INeg it -> {
                    var lhs = getInt(it.lhs.value);
                    stackframe.put(it, intConst(-lhs));
                }
                case FAdd it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(lhs + rhs));
                }
                case FSub it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(lhs - rhs));
                }
                case FMul it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(lhs * rhs));
                }
                case FDiv it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(lhs / rhs));
                }
                case FMod it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(lhs % rhs));
                }
                case FNeg it -> {
                    var lhs = getFloat(it.lhs.value);
                    stackframe.put(it, floatConst(-lhs));
                }

                // 相等运算
                case IEq it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, lhs == rhs ? iTrue : iFalse);
                }
                case INe it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, lhs != rhs ? iTrue : iFalse);
                }
                case FEq it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, lhs == rhs ? iTrue : iFalse);
                }
                case FNe it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, lhs != rhs ? iTrue : iFalse);
                }

                // 比较运算
                case IGe it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, lhs >= rhs ? iTrue : iFalse);
                }
                case ILe it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, lhs <= rhs ? iTrue : iFalse);
                }
                case IGt it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, lhs > rhs ? iTrue : iFalse);
                }
                case ILt it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, lhs < rhs ? iTrue : iFalse);
                }
                case FGe it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, lhs >= rhs ? iTrue : iFalse);
                }
                case FLe it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, lhs <= rhs ? iTrue : iFalse);
                }
                case FGt it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, lhs > rhs ? iTrue : iFalse);
                }
                case FLt it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, lhs < rhs ? iTrue : iFalse);
                }

                // 转型运算
                case I2F it -> {
                    var value = getInt(it.value.value);
                    stackframe.put(it, floatConst(value));
                }
                case F2I it -> {
                    var value = getFloat(it.value.value);
                    stackframe.put(it, intConst((int) value));
                }
                case BitCastI2F it -> {
                    var value = getInt(it.value.value);
                    stackframe.put(it, floatConst(Float.intBitsToFloat(value)));
                }
                case BitCastF2I it -> {
                    var value = getFloat(it.value.value);
                    stackframe.put(it, intConst(Float.floatToIntBits(value)));
                }

                // 位运算
                case Shl it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs << rhs));
                }
                // 逻辑右移
                case Shr it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs >>> rhs));
                }
                // 算数右移
                case AShr it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs >> rhs));
                }

                // 逻辑运算
                case And it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs & rhs));
                }
                case Or it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs | rhs));
                }
                case Xor it -> {
                    var lhs = getInt(it.lhs.value);
                    var rhs = getInt(it.rhs.value);
                    stackframe.put(it, intConst(lhs ^ rhs));
                }
                case Not it -> {
                    var value = getInt(it.rhs.value);
                    stackframe.put(it, value == 0 ? iTrue : iFalse);
                }

                // intrinsic
                case FAbs it -> {
                    var lhs = getFloat(it.lhs.value);
                    stackframe.put(it, floatConst(Math.abs(lhs)));
                }
                case FMax it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(Math.max(lhs, rhs)));
                }
                case FMin it -> {
                    var lhs = getFloat(it.lhs.value);
                    var rhs = getFloat(it.rhs.value);
                    stackframe.put(it, floatConst(Math.min(lhs, rhs)));
                }
                case FSqrt it -> {
                    var lhs = getFloat(it.lhs.value);
                    stackframe.put(it, floatConst((float) Math.sqrt(lhs)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
