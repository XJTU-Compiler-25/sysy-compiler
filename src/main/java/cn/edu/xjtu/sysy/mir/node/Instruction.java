package cn.edu.xjtu.sysy.mir.node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.riscv.ValuePosition;
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
    private BasicBlock block;

    Instruction(BasicBlock block, Type type) {
        super(type);
        this.block = block;
    }

    public void setBlock(BasicBlock block) {
        this.block = block;
    }

    public BasicBlock getBlock() {
        return block;
    }

    // 计算中间值应该都为 local value
    @Override
    public String shortName() {
        return "%" + id;
    }

    @Override
    public abstract String toString();

    public void insertBefore(Instruction instr) {
        if (this instanceof Terminator) {
            block.instructions.add(instr);
            return;
        }
        int idx = block.instructions.indexOf(this);
        block.instructions.add(idx, instr);
    }

    public void insertAfter(Instruction instr) {
        Assertions.requires(!(this instanceof Terminator));
        int idx = block.instructions.indexOf(this);
        block.instructions.add(idx + 1, instr);
    }

    public void replaceWith(Instruction instr) {
        int idx = block.instructions.indexOf(this);
        block.instructions.set(idx, instr);
        replaceAllUsesWith(instr);
        dispose();
    }

    // 基本块结束指令
    public abstract sealed static class Terminator extends Instruction {
        Terminator(BasicBlock block) {
            super(block, Types.Void);
        }

        public void replaceTarget(BasicBlock oldTarget, BasicBlock newTarget) {
            unsupported(this);
        }

        public void putParam(BasicBlock block, BlockArgument arg, Value value) {
            unsupported(this);
        }

        public Value getParam(BasicBlock block, BlockArgument arg) { return unsupported(this); }

        public void removeParam(BasicBlock block, BlockArgument arg) { unsupported(this); }

        public BlockArgument findParamByArg(BasicBlock block, Value value) { return unsupported(this); }
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
        public final HashMap<BlockArgument, Use> params = new HashMap<>();

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

        public void putParam(BlockArgument arg, Value value) {
            var use = params.get(arg);
            if (use == null) params.put(arg, use(value));
            else use.replaceValue(value);
        }

        @Override
        public void putParam(BasicBlock block, BlockArgument arg, Value value) {
            Assertions.requires(block == getTarget());
            putParam(arg, value);
        }

        @Override
        public Value getParam(BasicBlock block, BlockArgument arg) {
            Assertions.requires(block == getTarget());
            var use = params.get(arg);
            return use == null ? null : use.value;
        }

        public void removeParam(BlockArgument arg) {
            var use = params.remove(arg);
            if (use != null) use.dispose();
        }

        @Override
        public void removeParam(BasicBlock block, BlockArgument arg) {
            Assertions.requires(block == getTarget());
            removeParam(arg);
        }

        @Override
        public BlockArgument findParamByArg(BasicBlock block, Value value) {
            Assertions.requires(block == getTarget());
            for (var entry : params.entrySet()) {
                if (entry.getValue().value == value) return entry.getKey();
            }
            return null;
        }

        @Override
        public String toString() {
            return "jmp " + target.value.shortName() + "(" +
                    params.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", ")) +
                    ')';
        }
    }

    public abstract sealed static class AbstractBr extends Terminator {
        protected final Use<BasicBlock> trueTarget;
        protected final Use<BasicBlock> falseTarget;
        public final HashMap<BlockArgument, Use> trueParams = new HashMap<>();
        public final HashMap<BlockArgument, Use> falseParams = new HashMap<>();

        AbstractBr(BasicBlock block, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block);
            this.trueTarget = use(trueTarget);
            this.falseTarget = use(falseTarget);
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

        public void putTrueParam(BlockArgument arg, Value value) {
            var use = trueParams.get(arg);
            if (use == null) trueParams.put(arg, use(value));
            else use.replaceValue(value);
        }

        public void putFalseParam(BlockArgument arg, Value value) {
            var use = trueParams.get(arg);
            if (use == null) falseParams.put(arg, use(value));
            else use.replaceValue(value);
        }

        @Override
        public void putParam(BasicBlock block, BlockArgument arg, Value value) {
            if (block == trueTarget.value) putTrueParam(arg, value);
            if (block == falseTarget.value) putFalseParam(arg, value);
        }

        @Override
        public Value getParam(BasicBlock block, BlockArgument arg) {
            if (block == trueTarget.value) {
                var use = trueParams.get(arg);
                return use == null ? null : use.value;
            }
            if (block == falseTarget.value) {
                var use = falseParams.get(arg);
                return use == null ? null : use.value;
            }
            return null; // 不在这两个分支中
        }

        @Override
        public void removeParam(BasicBlock block, BlockArgument arg) {
            if (block == getTrueTarget()) {
                var use = trueParams.remove(arg);
                if (use != null) use.dispose();
            }
            if (block == getFalseTarget()) {
                var use = falseParams.remove(arg);
                if (use != null) use.dispose();
            }
        }

        @Override
        public BlockArgument findParamByArg(BasicBlock block, Value value) {
            if (block == trueTarget.value) {
                for (var entry : trueParams.entrySet()) {
                    if (entry.getValue().value == value) return entry.getKey();
                }
            }
            if (block == falseTarget.value) {
                for (var entry : falseParams.entrySet()) {
                    if (entry.getValue().value == value) return entry.getKey();
                }
            }
            return null; // 不在这两个分支中
        }
    }

    // 分支 branch
    public static final class Br extends AbstractBr {
        private final Use condition;

        Br(BasicBlock block, Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.condition = use(condition);
        }

        public Value getCondition() {
            return condition.value;
        }

        public void replaceCondition(Value newCondition) {
            condition.replaceValue(newCondition);
        }

        @Override
        public String toString() {
            return "br " + condition.value.shortName() + ", " + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    // 函数调用

    public static sealed abstract class AbstractCall extends Instruction {
        public Use[] args;

        AbstractCall(BasicBlock block, Type type, Value... args) {
            super(block, type);
            var argLen = args.length;
            this.args = new Use[argLen];
            for (int i = 0; i < argLen; i++) this.args[i] = use(args[i]);
        }
    }

    public static final class Call extends AbstractCall {
        private final Use<Function> function;

        Call(BasicBlock block, Function function, Value... args) {
            super(block, function.funcType.returnType, args);
            this.function = use(function);
        }

        public void setFunction(Function newFunc) {
            function.replaceValue(newFunc);
        }

        public Function getFunction() {
            return function.value;
        }

        @Override
        public String toString() {
            return this.shortName() + " = call " + function + "(" +
                    Arrays.stream(args).map(v -> v.value.shortName()).collect(Collectors.joining(", ")) +
                    ")";
        }
    }

    public static final class CallExternal extends AbstractCall {
        public BuiltinFunction function;

        CallExternal(BasicBlock block, String function, Value... args) {
            this(block, BuiltinFunction.of(function), args);
        }

        CallExternal(BasicBlock block, BuiltinFunction function, Value... args) {
            super(block, function.symbol.funcType.returnType, args);
            this.function = function;
        }

        @Override
        public String toString() {
            return this.shortName() + " = call external " + function.linkName + "(" +
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

        Alloca(BasicBlock block, Type type) {
            super(block, Types.ptrOf(type));
            this.allocatedType = type;
        }

        @Override
        public String toString() {
            return String.format("%s = alloca %s", this.shortName(), allocatedType.toString());
        }
    }

    public static final class Load extends Instruction {
        public Use address;

        Load(BasicBlock block, Value address) {
            super(block, ((Type.Pointer) address.type).baseType);
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
            super(block, Types.Void);
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

        GetElemPtr(BasicBlock block, Value basePtr, Value[] indices) {
            super(block, switch (basePtr.type) {
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

        I2F(BasicBlock block, Value value) {
            super(block, Types.Float);
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

        F2I(BasicBlock block, Value value) {
            super(block, Types.Int);
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

        BitCastI2F(BasicBlock block, Value value) {
            super(block, Types.Float);
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

        BitCastF2I(BasicBlock block, Value value) {
            super(block, Types.Int);
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

        IAdd(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        ISub(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        IMul(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        IDiv(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        IMod(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        INeg(BasicBlock block, Value lhs) {
            super(block, Types.Int);
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

        FAdd(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        FSub(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        FMul(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        FDiv(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        FMod(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        FNeg(BasicBlock block, Value lhs) {
            super(block, Types.Float);
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

        Shl(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        Shr(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        AShr(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        And(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        Or(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        Xor(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        Not(BasicBlock block, Value rhs) {
            super(block, Types.Int);
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

        IEq(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        INe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        IGt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        ILt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        IGe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        ILe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FEq(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FNe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FGt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FLt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FGe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FLe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int);
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

        FSqrt(BasicBlock block, Value lhs) {
            super(block, Types.Float);
            this.lhs = use(lhs);
        }

        @Override
        public String toString() {
            return String.format("%s = fsqrt %s", this.shortName(), lhs.value.shortName());
        }
    }

    public static final class FAbs extends Instruction {
        public Use lhs;

        FAbs(BasicBlock block, Value lhs) {
            super(block, Types.Float);
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

        FMin(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        FMax(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float);
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

        public ILi(BasicBlock block, int imm) {
            super(block, Types.Int);
            this.imm = imm;
        }

        @Override
        public String toString() {
            return String.format("%s = ili %d", this.shortName(), imm);
        }
    }

    public static final class FLi extends Instruction {
        public float imm;

        public FLi(BasicBlock block, float imm) {
            super(block, Types.Int); // 需要先把浮点数按位赋值给整数寄存器
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
            super(block, Types.Void);
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
            super(block, Types.Void);
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("fmv %s, %s", dst.shortName(), src.value.shortName());
        }
    }

    public static final class ICpy extends Instruction {
        public Use src;

        ICpy(BasicBlock block, Value src) {
            super(block, src.type);
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("%s = icpy %s", shortName(), src.value.shortName());
        }
    }

    public static final class FCpy extends Instruction {
        public Use src;

        FCpy(BasicBlock block, Value src) {
            super(block, src.type);
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("%s = fcpy %s", shortName(), src.value.shortName());
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

        FMulAdd(BasicBlock block, Op op, Value rs1, Value rs2, Value rs3) {
            super(block, Types.Float);
            this.rs1 = use(rs1);
            this.rs2 = use(rs2);
            this.rs3 = use(rs3);
        }

        @Override
        public String toString() {
            return String.format("%s = %s %s, %s, %s", shortName(), op, rs1, rs2, rs3);
        }
    }

    public static final class BEq extends AbstractBr {
        public Use lhs;
        public Use rhs;

        BEq(BasicBlock block, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {

            return "beq " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", "
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class BNe extends AbstractBr {
        public Use lhs;
        public Use rhs;

        BNe(BasicBlock block, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return "bne " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", " 
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class BLt extends AbstractBr {
        public Use lhs;
        public Use rhs;

        BLt(BasicBlock block, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return "blt " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", " 
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class BGe extends AbstractBr {
        public Use lhs;
        public Use rhs;

        BGe(BasicBlock block, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return "bge " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", " 
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class BLe extends AbstractBr {
        public Use lhs;
        public Use rhs;

        BLe(BasicBlock block, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return "ble " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", " 
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class BGt extends AbstractBr {
        public Use lhs;
        public Use rhs;

        BGt(BasicBlock block, Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
            super(block, trueTarget, falseTarget);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        @Override
        public String toString() {
            return "bgt " + lhs.value.shortName() + ", " + rhs.value.shortName() + ", " 
                    + trueTarget.value.shortName() + "(" +
                    trueParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.entrySet().stream()
                            .map(pair -> pair.getKey().shortName()
                                    + "= " + pair.getValue().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    public static final class Dummy extends Instruction {
        Use[] uses;

        Dummy(BasicBlock block, Type type, Value... uses) {
            super(block, type);
            this.uses = new Use[uses.length];
            for (int i = 0; i < uses.length; i++) this.uses[i] = use(uses[i]);
        }

        Dummy(BasicBlock block, Value... uses) {
            this(block, Types.Void, uses);
        }

        @Override
        public String toString() {
            return "dummy use " + Arrays.stream(uses)
                                        .map(v -> v.value.shortName())
                                        .collect(Collectors.joining(", "));
        }
    }

    /** 对应RV中的I类型指令，需要保证imm的值在范围内 */
    public abstract sealed static class Imm extends Instruction {
        public Use lhs;
        public int imm;
        public Imm(BasicBlock block, Value lhs, int imm) {
            super(block, lhs.type);
            this.lhs = use(lhs);
            this.imm = imm;
        }
    }

    public static final class Addi extends Imm {
        Addi(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = addi %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Andi extends Imm {
        Andi(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = andi %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Ori extends Imm {
        Ori(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = ori %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Xori extends Imm {
        Xori(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = xori %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Slli extends Imm {
        Slli(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = slli %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Srli extends Imm {
        Srli(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = srli %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Srai extends Imm {
        Srai(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = srai %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

    public static final class Slti extends Imm {
        Slti(BasicBlock block, Value lhs, int imm) {
            super(block, lhs, imm);
        }

        @Override
        public String toString() {
            return String.format("%s = slti %s, %d", shortName(), lhs.value.shortName(), imm);
        }
    }

}
