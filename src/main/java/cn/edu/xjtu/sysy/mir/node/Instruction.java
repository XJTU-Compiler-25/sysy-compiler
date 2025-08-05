package cn.edu.xjtu.sysy.mir.node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.symbol.BuiltinFunction;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;
import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

/**
 * 建议通过 {@link InstructionHelper} 构造指令
 * 请注意整数运算指令都是 signed 的
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
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
        return "%" + label;
        //return "%" + label + ": " + type;
    }

    @Override
    public abstract String toString();

    /** 若label为-1表示没有定义 */
    public boolean hasNoDef() {
        return label == -1;
    }

    public void frontInsert(Instruction instr) {
        if (this instanceof Terminator) {
            block.instructions.add(instr);
            return;
        }
        int idx = block.instructions.indexOf(this);
        block.instructions.add(idx, instr);
    }

    public void backInsert(Instruction instr) {
        Assertions.requires(!(this instanceof Terminator));
        int idx = block.instructions.indexOf(this);
        block.instructions.add(idx+1, instr);
    }

    // 基本块结束指令
    public abstract sealed static class Terminator extends Instruction {
        Terminator(BasicBlock block) {
            super(block, -1, Types.Void);
        }

        public void replaceTarget(BasicBlock oldTarget, BasicBlock newTarget) {
            unsupported(this);
        }

        public void putParam(BasicBlock block, Var var, Value value) {
            unsupported(this);
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
        private final Use<BasicBlock> target;
        // 对下个块中 var 对应的最新的值进行更新，所以用 var 作为 key
        public final HashMap<Var, Use> params = new HashMap<>();

        Jmp(BasicBlock block, BasicBlock target) {
            super(block);
            this.target = use(target);
        }

        public BasicBlock getTarget() {
            return target.value;
        }

        public void replaceTarget(BasicBlock newTarget) {
            target.replaceValue(newTarget);
        }

        @Override
        public void replaceTarget(BasicBlock oldTarget, BasicBlock newTarget) {
            Assertions.requires(oldTarget == getTarget());
            replaceTarget(newTarget);
        }

        public void putParam(Var var, Value value) {
            params.compute(var, (_, use) -> {
                if (use == null) return use(value);
                else {
                    use.replaceValue(value);
                    return use;
                }
            });
        }

        @Override
        public void putParam(BasicBlock block, Var var, Value value) {
            Assertions.requires(block == getTarget());
            putParam(var, value);
        }

        @Override
        public String toString() {
            return "jmp " + target.value.shortName() + "(" +
                    params.entrySet().stream()
                            .map(entry -> entry.getKey().name
                                    + "= " + entry.getValue().value.shortName())
                            .collect(Collectors.joining(", ")) +
                    ')';
        }
    }

    // 分支 branch
    public static final class Br extends Terminator {
        private final Use condition;
        private final Use<BasicBlock> trueTarget;
        private final Use<BasicBlock> falseTarget;
        public final HashMap<Var, Use> trueParams = new HashMap<>();
        public final HashMap<Var, Use> falseParams = new HashMap<>();

        Br(BasicBlock block, Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block);
            this.condition = use(condition);
            this.trueTarget = use(trueTarget);
            this.falseTarget = use(falseTarget);
        }

        public Value getCondition() {
            return condition.value;
        }

        public void replaceCondition(Value newCondition) {
            condition.replaceValue(newCondition);
        }

        public BasicBlock getTrueTarget() {
            return trueTarget.value;
        }

        public BasicBlock getFalseTarget() {
            return falseTarget.value;
        }

        public List<BasicBlock> getTargets() {
            return List.of(getTrueTarget(), getFalseTarget());
        }

        public void replaceTrueTarget(BasicBlock newTarget) {
            trueTarget.replaceValue(newTarget);
        }

        public void replaceFalseTarget(BasicBlock newTarget) {
            falseTarget.replaceValue(newTarget);
        }

        @Override
        public void replaceTarget(BasicBlock oldTarget, BasicBlock newTarget) {
            if (oldTarget == trueTarget.value) replaceTrueTarget(newTarget);
            if (oldTarget == falseTarget.value) replaceFalseTarget(newTarget);
        }

        public void putTrueParam(Var var, Value value) {
            trueParams.compute(var, (_, use) -> {
                if (use == null) return use(value);
                else {
                    use.replaceValue(value);
                    return use;
                }
            });
        }

        public void putFalseParam(Var var, Value value) {
            falseParams.compute(var, (_, use) -> {
                if (use == null) return use(value);
                else {
                    use.replaceValue(value);
                    return use;
                }
            });
        }

        @Override
        public void putParam(BasicBlock block, Var var, Value value) {
            if (block == trueTarget.value) putTrueParam(var, value);
            if (block == falseTarget.value) putFalseParam(var, value);
        }

        @Override
        public String toString() {
            return "br " + condition.value.shortName() + ", " + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(param -> param.getKey().name
                                    + "= " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(param -> param.getKey().name
                                    + "= " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    // 函数调用

    public static final class Call extends Instruction {
        private final Use<Function> function;
        public Use[] args;

        Call(BasicBlock block, int label, Function function, Value... args) {
            super(block, label, function.funcType.returnType);
            this.function = use(function);
            var argLen = args.length;
            this.args = new Use[argLen];
            for (int i = 0; i < argLen; i++) this.args[i] = use(args[i]);
        }

        public void setFunction(Function newFunc) {
            function.replaceValue(newFunc);
        }

        public Function getFunction() {
            return function.value;
        }

        @Override
        public String toString() {
            return "%" + this.label + " = call " + function.value.name + "(" +
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

        public I2F(BasicBlock block, int label, Value value) {
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

    public static final class INeg extends Instruction {
        public Use lhs;

        INeg(BasicBlock block, int label, Value lhs) {
            super(block, label, Types.Int);
            this.lhs = use(lhs);
        }

        @Override
        public String toString() {
            return String.format("%s = ineg %s", this.shortName(), lhs.value.shortName());
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

    public static final class Not extends Instruction {
        public Use rhs;

        Not(BasicBlock block, int label, Value rhs) {
            super(block, label, Types.Int);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return String.format("%s = not %s", this.shortName(), rhs.value.shortName());
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

    public static final class ILi extends Instruction {
        public int imm;

        public ILi(BasicBlock block, int label, int imm) {
            super(block, label, Types.Int);
            this.imm = imm;
        }

        @Override
        public String toString() {
            return String.format("%s = ili %d", this.shortName(), imm);
        }
    }

    public static final class FLi extends Instruction {
        public float imm;

        public FLi(BasicBlock block, int label, float imm) {
            super(block, label, Types.Int); // 需要先把浮点数按位赋值给整数寄存器
            this.imm = imm;
        }

        @Override
        public String toString() {
            return String.format("%s = fli %f", this.shortName(), imm);
        }
    }

    public static final class IMv extends Instruction {
        public Value dst;
        public Use src;

        IMv(BasicBlock block, Value dst, Value src) {
            super(block, -1, Types.Void);
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("imv %s, %s", dst.shortName(), src.value.shortName());
        }
    }

    public static final class FMv extends Instruction {
        public Value dst;
        public Use src;

        FMv(BasicBlock block, Value dst, Value src) {
            super(block, -1, Types.Void);
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("fmv %s, %s", dst.shortName(), src.value.shortName());
        }
    }

    public static final class FMulAdd extends Instruction {
        public enum Op {
            FMADD("fmadd.s"),
            FMSUB("fmsub.s"),
            FNMSUB("fnmsub.s"),
            FNMADD("fnmadd.s");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        Op op;
        Use rs1;
        Use rs2;
        Use rs3;

        FMulAdd(BasicBlock block, int label, Op op, Value rs1, Value rs2, Value rs3) {
            super(block, label, Types.Float);
            this.rs1 = use(rs1);
            this.rs2 = use(rs2);
            this.rs3 = use(rs3);
        }

        @Override
        public String toString() {
            return String.format("%s = %s %s, %s, %s", shortName(), op, rs1, rs2, rs3);
        }
    }

    public static final class BrBinary extends Terminator {
        public enum Op {
            BEQ("beq"),
            BGE("bge"),
            BGEU("bgeu"),
            BLT("blt"),
            BLTU("bltu"),
            BNE("bne"),
            BGTU("bgtu"),
            BLE("ble"),
            BLEU("bleu"),
            BGT("bgt");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        public Op op;
        public Use lhs;
        public Use rhs;
        private final Use<BasicBlock> trueTarget;
        private final Use<BasicBlock> falseTarget;
        public final HashMap<Var, Use> trueParams = new HashMap<>();
        public final HashMap<Var, Use> falseParams = new HashMap<>();

        public BrBinary(BasicBlock block, Op op, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block);
            this.op = op;
            this.lhs = use(lhs);
            this.rhs = use(rhs);
            this.trueTarget = use(trueTarget);
            this.falseTarget = use(falseTarget);
        }

        public void putAllTrueParams(HashMap<Var, Use> trueParams) {
            this.trueParams.putAll(trueParams);
        }
        
        public void putAllFalseParams(HashMap<Var, Use> falseParams) {
            this.falseParams.putAll(falseParams);
        }

        public BasicBlock getTrueTarget() {
            return trueTarget.value;
        }

        public BasicBlock getFalseTarget() {
            return falseTarget.value;
        }

        public List<BasicBlock> getTargets() {
            return List.of(getTrueTarget(), getFalseTarget());
        }

        public void replaceTrueTarget(BasicBlock newTarget) {
            trueTarget.replaceValue(newTarget);
        }

        public void replaceFalseTarget(BasicBlock newTarget) {
            falseTarget.replaceValue(newTarget);
        }

        @Override
        public void replaceTarget(BasicBlock oldTarget, BasicBlock newTarget) {
            if (oldTarget == trueTarget.value) replaceTrueTarget(newTarget);
            if (oldTarget == falseTarget.value) replaceFalseTarget(newTarget);
        }

        public void putTrueParam(Var var, Value value) {
            trueParams.compute(var, (_, use) -> {
                if (use == null) return use(value);
                else {
                    use.replaceValue(value);
                    return use;
                }
            });
        }

        public void putFalseParam(Var var, Value value) {
            falseParams.compute(var, (_, use) -> {
                if (use == null) return use(value);
                else {
                    use.replaceValue(value);
                    return use;
                }
            });
        }

        @Override
        public void putParam(BasicBlock block, Var var, Value value) {
            if (block == trueTarget.value) putTrueParam(var, value);
            if (block == falseTarget.value) putFalseParam(var, value);
        }

        @Override
        public String toString() {
            return op + " " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", " 
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(param -> param.getKey().name
                                    + "= " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), "
                    + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(param -> param.getKey().name
                                    + "= " + param.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class Dummy extends Instruction {
        Use[] uses;
        Dummy(BasicBlock block, int label, Type type, Value... uses) {
            super(block, label, type);
            this.uses = new Use[uses.length];
            for (int i = 0; i < uses.length; i++) {
                this.uses[i] = use(uses[i]);
            } 
        }

        Dummy(BasicBlock block, Value... uses) {
            super(block, -1, Types.Void);
            this.uses = new Use[uses.length];
            for (int i = 0; i < uses.length; i++) {
                this.uses[i] = use(uses[i]);
            } 
        }

        @Override
        public String toString() {
            return (hasNoDef() ? "" : shortName() + " = ") + "dummy " + 
                    Arrays.stream(uses).map(v -> v.value.shortName()).collect(Collectors.joining(", "));
        }
    }

    public static final class Imm extends Instruction {
        public Op op;
        public Use lhs;
        public int imm;
        public Imm(BasicBlock block, int label, Op op, Value lhs, int imm) {
            super(block, label, lhs.type);
            this.op = op;
            this.lhs = use(lhs);
            this.imm = imm;
        }

        public enum Op {
            ADDI("addi"),
            ANDI("andi"),
            ORI("ori"),
            XORI("xori"),
            SLLI("slli"),
            SRLI("srli"),
            SRAI("srai"),
            SLTI("slti");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }
        

        @Override
        public String toString() {
            return String.format("%s %s, %s, %d", op, shortName(), lhs.value.shortName(), imm);
        }
    }
}
