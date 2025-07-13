package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.BuiltinFunction;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

/**
 * 建议通过 {@link InstructionHelper} 构造指令
 * 请注意整数运算指令都是 signed 的
 */
@SuppressWarnings("rawtypes")
public abstract sealed class Instruction extends User {
    private final BasicBlock block;

    // local label
    public final int label;

    Instruction(BasicBlock block, int label, Type type) {
        super(type);
        this.block = block;
        this.label = label;
    }

    public BasicBlock getBlock() {
        return block;
    }

    // 计算中间值应该都为 local value
    @Override
    public String shortName() {
        //return "%" + label;
        return "%" + label + ": " + type;
    }

    @Override
    public abstract String toString();

    // 若label为-1表示没有定义
    public boolean hasNoDef() {
        return label == -1;
    }

    // 检查右边的表达式是否相同
    public boolean equalRVal(Instruction other) {
        return getClass() == other.getClass() && used.equals(other.used);
    }

    // 基本块结束指令
    public abstract sealed static class Terminator extends Instruction {
        Terminator(BasicBlock block) {
            super(block, -1, Types.Void);
        }
    }

    // 带值返回 return
    public static final class Ret extends Terminator {
        public Use retVal;

        Ret(BasicBlock block, Value retVal) {
            super(block);
            this.retVal = use(retVal);
        }

        @Override
        public String toString() {
            return "ret " + retVal.value.shortName();
        }
    }

    /**
     * 无值返回 return void
     */
    public static final class RetV extends Terminator {
        RetV(BasicBlock block) {
            super(block);
        }

        @Override
        public String toString() {
            return "ret void";
        }
    }

    // 无条件跳转 jump
    public static final class Jmp extends Terminator {
        public Use<BasicBlock> target;
        // 对下个块中 var 对应的最新的值进行更新，所以用 var 作为 key
        public HashMap<Var, Use> params = new HashMap<>();

        Jmp(BasicBlock block, BasicBlock target) {
            super(block);
            this.target = use(target);
        }

        public void putParam(Var var, Value value) {
            params.put(var, use(value));
        }

        @Override
        public String toString() {
            return "jmp " + target.value.label + "(" +
                    params.entrySet().stream()
                            .map(param -> param.getKey().name + ": " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", ")) +
                    ')';
        }
    }

    // 分支 branch
    public static final class Br extends Terminator {
        public Use condition;
        public Use<BasicBlock> trueTarget;
        public Use<BasicBlock> falseTarget;
        public HashMap<Var, Use> trueParams = new HashMap<>();
        public HashMap<Var, Use> falseParams = new HashMap<>();

        Br(BasicBlock block, Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block);
            this.condition = use(condition);
            this.trueTarget = use(trueTarget);
            this.falseTarget = use(falseTarget);
        }

        public void putParam(BasicBlock block, Var var, Value value) {
            if (block == trueTarget.value) trueParams.put(var, use(value));
            if (block == falseTarget.value) falseParams.put(var, use(value));
        }

        @Override
        public String toString() {
            return "br " + condition.value.shortName() + ", " + trueTarget.value.label + "(" +
                    trueParams.entrySet().stream()
                            .map(param -> param.getKey().name + ": " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.label + "(" +
                    falseParams.entrySet().stream()
                            .map(param -> param.getKey().name + ": " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    // 函数调用

    public static final class Call extends Instruction {
        public Function function;
        public Use[] args;

        Call(BasicBlock block, int label, Function function, Value... args) {
            super(block, label, function.funcType.returnType);
            this.function = function;
            var argLen = args.length;
            this.args = new Use[argLen];
            for (int i = 0; i < argLen; i++) this.args[i] = use(args[i]);
        }

        @Override
        public String toString() {
            return "%" + this.label + " = call " + function.name + "(" +
                    Arrays.stream(args).map(v -> v.value.shortName()).collect(Collectors.joining(", ")) +
                    ")";
        }
    }

    public static final class CallExternal extends Instruction {
        public BuiltinFunction function;
        public Use[] args;

        CallExternal(BasicBlock block, int label, String function, Value... args) {
            this(block, label, BuiltinFunction.of(function), args);
        }

        CallExternal(BasicBlock block, int label, BuiltinFunction function, Value... args) {
            super(block, label, function.symbol.funcType.returnType);
            this.function = function;
            var argLen = args.length;
            this.args = new Use[argLen];
            for (int i = 0; i < argLen; i++) this.args[i] = use(args[i]);
        }

        @Override
        public String toString() {
            return "%" + this.label + " = call external " + function.linkName + "(" +
                    Arrays.stream(args).map(v -> v.value.shortName()).collect(Collectors.joining(", ")) +
                    ")";
        }
    }

    // 内存操作

    /**
     * stack allocate
     */
    public static final class Alloca extends Instruction {
        public Type allocatedType;

        Alloca(BasicBlock block, int label, Type type) {
            super(block, label, Types.ptrOf(type));
            this.allocatedType = type;
        }

        @Override
        public String toString() {
            return String.format("%s = alloca %s", this.shortName(), allocatedType.toString());
        }
    }

    public static final class Load extends Instruction {
        public Use address;

        Load(BasicBlock block, int label, Value address) {
            super(block, label, ((Type.Pointer) address.type).baseType);
            this.address = use(address);
        }

        @Override
        public String toString() {
            return String.format("%s = load ptr %s", this.shortName(), address.value.shortName());
        }
    }

    public static final class Store extends Instruction {
        public Use address;
        public Use storeVal;

        Store(BasicBlock block, Value address, Value value) {
            super(block, -1, Types.Void);
            this.address = use(address);
            this.storeVal = use(value);
        }

        @Override
        public String toString() {
            return String.format("store ptr %s, value %s", address.value.shortName(), storeVal.value.shortName());
        }
    }

    /**
     * 仅计算元素的指针而不访问
     * 举例：getelemptr [Any x 20 x i32], n = [20 x i32]
     */
    public static final class GetElemPtr extends Instruction {
        public Use basePtr;
        public Use[] indices;

        GetElemPtr(BasicBlock block, int label, Value basePtr, Value[] indices) {
            super(block, label, switch (basePtr.type) {
                case Type.Pointer ptr -> {
                    if (indices.length == 1) yield ptr;
                    yield Types.ptrOf(ptr.getIndexElementType(indices.length));
                }
                case Type.Array array -> Types.ptrOf(array.getIndexElementType(indices.length));
                default -> unsupported(basePtr.type);
            });
            this.basePtr = use(basePtr);
            var indexCount = indices.length;
            this.indices = new Use[indexCount];
            for (int i = 0; i < indexCount; i++) this.indices[i] = use(indices[i]);
        }

        @Override
        public String toString() {
            return shortName() + " = getelemptr base " + basePtr.value.shortName() + ", indices " +
                    Arrays.stream(indices).map(v -> v.value.shortName()).collect(Collectors.joining(", "));
        }
    }

    // cast

    /**
     * 整数转浮点数
     */
    public static final class I2F extends Instruction {
        public Use value;

        I2F(BasicBlock block, int label, Value value) {
            super(block, label, Types.Float);
            this.value = use(value);
        }

        @Override
        public String toString() {
            return String.format("%s = i2f %s", this.shortName(), value.value.shortName());
        }
    }

    /**
     * 浮点数转整数
     */
    public static final class F2I extends Instruction {
        public Use value;

        F2I(BasicBlock block, int label, Value value) {
            super(block, label, Types.Int);
            this.value = use(value);
        }

        @Override
        public String toString() {
            return String.format("%s = f2i %s", this.shortName(), value.value.shortName());
        }
    }

    /**
     * 不实际转换地将某整数值当作浮点数
     */
    public static final class BitCastI2F extends Instruction {
        public Use value;

        BitCastI2F(BasicBlock block, int label, Value value) {
            super(block, label, Types.Float);
            this.value = use(value);
        }

        @Override
        public String toString() {
            return String.format("%s = i2f bitcast %s", this.shortName(), value.value.shortName());
        }
    }

    /**
     * 不实际转换地将某整数值当作浮点数
     */
    public static final class BitCastF2I extends Instruction {
        public Use value;

        BitCastF2I(BasicBlock block, int label, Value value) {
            super(block, label, Types.Int);
            this.value = use(value);
        }

        @Override
        public String toString() {
            return String.format("%s = f2i bitcast %s", this.shortName(), value.value.shortName());
        }
    }

    // 算数指令

    public static final class IAdd extends Instruction {
        public Use lhs;
        public Use rhs;

        IAdd(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = iadd %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class ISub extends Instruction {
        public Use lhs;
        public Use rhs;

        ISub(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = isub %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class IMul extends Instruction {
        public Use lhs;
        public Use rhs;

        IMul(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = imul %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class IDiv extends Instruction {
        public Use lhs;
        public Use rhs;

        IDiv(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = idiv %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class IMod extends Instruction {
        public Use lhs;
        public Use rhs;

        IMod(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = imod %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FAdd extends Instruction {
        public Use lhs;
        public Use rhs;

        FAdd(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fadd %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FSub extends Instruction {
        public Use lhs;
        public Use rhs;

        FSub(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fsub %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FMul extends Instruction {
        public Use lhs;
        public Use rhs;

        FMul(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fmul %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FDiv extends Instruction {
        public Use lhs;
        public Use rhs;

        FDiv(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fdiv %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FMod extends Instruction {
        public Use lhs;
        public Use rhs;

        FMod(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fmod %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FNeg extends Instruction {
        public Use lhs;

        FNeg(BasicBlock block, int label, Value lhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fneg %s", this.shortName(), lhs.value.shortName());
        }
    }

    // 位运算

    /**
     * 左移位 shift left
     */
    public static final class Shl extends Instruction {
        public Use lhs;
        public Use rhs;

        Shl(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = shl %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    /**
     * 不处理符号！
     * 右移位 right shift
     */
    public static final class Shr extends Instruction {
        public Use lhs;
        public Use rhs;

        Shr(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = shr %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    /**
     * 算数右移位 arithmetic shift right
     */
    public static final class AShr extends Instruction {
        public Use lhs;
        public Use rhs;

        AShr(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = ashr %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class And extends Instruction {
        public Use lhs;
        public Use rhs;

        And(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = and %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class Or extends Instruction {
        public Use lhs;
        public Use rhs;

        Or(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = or %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class Xor extends Instruction {
        public Use lhs;
        public Use rhs;

        Xor(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = xor %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    // 比较指令

    public static final class IEq extends Instruction {
        public Use lhs;
        public Use rhs;

        IEq(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = icmp eq %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class INe extends Instruction {
        public Use lhs;
        public Use rhs;

        INe(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = icmp ne %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class IGt extends Instruction {
        public Use lhs;
        public Use rhs;

        IGt(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = icmp gt %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class ILt extends Instruction {
        public Use lhs;
        public Use rhs;

        ILt(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = icmp lt %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class IGe extends Instruction {
        public Use lhs;
        public Use rhs;

        IGe(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = icmp ge %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class ILe extends Instruction {
        public Use lhs;
        public Use rhs;

        ILe(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = icmp le %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FEq extends Instruction {
        public Use lhs;
        public Use rhs;

        FEq(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp eq %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FNe extends Instruction {
        public Use lhs;
        public Use rhs;

        FNe(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp ne %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FGt extends Instruction {
        public Use lhs;
        public Use rhs;

        FGt(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp gt %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FLt extends Instruction {
        public Use lhs;
        public Use rhs;

        FLt(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp lt %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FGe extends Instruction {
        public Use lhs;
        public Use rhs;

        FGe(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp ge %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FLe extends Instruction {
        public Use lhs;
        public Use rhs;

        FLe(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp le %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    // intrinsic 指令

    public static final class FSqrt extends Instruction {
        public Use lhs;

        FSqrt(BasicBlock block, int label, Value lhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fsqrt %s", this.shortName(), lhs.value.shortName());
        }
    }

    public static final class FAbs extends Instruction {
        public Use lhs;

        FAbs(BasicBlock block, int label, Value lhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fabs %s", this.shortName(), lhs.value.shortName());
        }
    }

    public static final class FMin extends Instruction {
        public Use lhs;
        public Use rhs;

        FMin(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fmin %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

    public static final class FMax extends Instruction {
        public Use lhs;
        public Use rhs;

        FMax(BasicBlock block, int label, Value lhs, Value rhs) {
            super(block, label, Types.Float);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fmax %s, %s", this.shortName(), lhs.value.shortName(), rhs.value.shortName());
        }
    }

}
