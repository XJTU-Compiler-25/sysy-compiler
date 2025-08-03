package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.symbol.BuiltinFunction;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;
import cn.edu.xjtu.sysy.util.Pair;

import static cn.edu.xjtu.sysy.util.Assertions.unsupported;
import static cn.edu.xjtu.sysy.util.Pair.pair;

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
        public final ArrayList<Pair<Use<BlockArgument>, Use>> params = new ArrayList<>();

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

        private Pair<Use<BlockArgument>, Use> getParamPair(BlockArgument arg) {
            for (var pair : params) if (pair.first().value == arg) return pair;
            return null;
        }

        public void putParam(BlockArgument arg, Value value) {
            var pair = getParamPair(arg);
            if (pair == null) params.add(pair(use(arg), use(value)));
            else pair.second().replaceValue(value);
        }

        @Override
        public void putParam(BasicBlock block, BlockArgument arg, Value value) {
            Assertions.requires(block == getTarget());
            putParam(arg, value);
        }

        @Override
        public Value getParam(BasicBlock block, BlockArgument arg) {
            Assertions.requires(block == getTarget());
            var pair = getParamPair(arg);
            return pair == null ? null : pair.second().value;
        }

        public void removeParam(BlockArgument arg) {
            for (var iter = params.iterator(); iter.hasNext(); ) {
                var pair = iter.next();
                if (pair.first().value == arg) {
                    iter.remove();
                    pair.first().dispose();
                    pair.second().dispose();
                }
            }
        }

        @Override
        public void removeParam(BasicBlock block, BlockArgument arg) {
            Assertions.requires(block == getTarget());
            removeParam(arg);
        }

        @Override
        public String toString() {
            return "jmp " + target.value.shortName() + "(" +
                    params.stream()
                            .map(pair -> pair.first().value.shortName()
                                    + "= " + pair.second().value.shortName())
                            .collect(Collectors.joining(", ")) +
                    ')';
        }
    }

    // 分支 branch
    public static final class Br extends Terminator {
        private final Use condition;
        private final Use<BasicBlock> trueTarget;
        private final Use<BasicBlock> falseTarget;
        public final ArrayList<Pair<Use<BlockArgument>, Use>> trueParams = new ArrayList<>();
        public final ArrayList<Pair<Use<BlockArgument>, Use>> falseParams = new ArrayList<>();

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

        private Pair<Use<BlockArgument>, Use> getTrueParamPair(BlockArgument arg) {
            for (var pair : trueParams) if (pair.first().value == arg) return pair;
            return null;
        }

        public void putTrueParam(BlockArgument arg, Value value) {
            var pair = getTrueParamPair(arg);
            if (pair == null) trueParams.add(pair(use(arg), use(value)));
            else pair.second().replaceValue(value);
        }

        private Pair<Use<BlockArgument>, Use> getFalseParamPair(BlockArgument arg) {
            for (var pair : falseParams) if (pair.first().value == arg) return pair;
            return null;
        }

        public void putFalseParam(BlockArgument arg, Value value) {
            var pair = getFalseParamPair(arg);
            if (pair == null) falseParams.add(pair(use(arg), use(value)));
            else pair.second().replaceValue(value);
        }

        @Override
        public void putParam(BasicBlock block, BlockArgument arg, Value value) {
            if (block == trueTarget.value) putTrueParam(arg, value);
            if (block == falseTarget.value) putFalseParam(arg, value);
        }

        @Override
        public Value getParam(BasicBlock block, BlockArgument arg) {
            if (block == trueTarget.value) {
                var pair = getTrueParamPair(arg);
                return pair == null ? null : pair.second().value;
            }
            if (block == falseTarget.value) {
                var pair = getFalseParamPair(arg);
                return pair == null ? null : pair.second().value;
            }
            return null; // 不在这两个分支中
        }

        @Override
        public void removeParam(BasicBlock block, BlockArgument arg) {
            if (block == getTrueTarget()) {
                for (var iter = trueParams.iterator(); iter.hasNext(); ) {
                    var pair = iter.next();
                    if (pair.first().value == arg) {
                        iter.remove();
                        pair.first().dispose();
                        pair.second().dispose();
                    }
                }
            }
            if (block == getFalseTarget()) {
                for (var iter = falseParams.iterator(); iter.hasNext(); ) {
                    var pair = iter.next();
                    if (pair.first().value == arg) {
                        iter.remove();
                        pair.first().dispose();
                        pair.second().dispose();
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "br " + condition.value.shortName() + ", " + trueTarget.value.shortName() + "(" +
                    trueParams.stream()
                            .map(pair -> pair.first().value.shortName()
                                    + "= " + pair.second().value.shortName())
                            .collect(Collectors.joining(", "))
                    + "), " + falseTarget.value.shortName() + "(" +
                    falseParams.stream()
                            .map(pair -> pair.first().value.shortName()
                                    + "= " + pair.second().value.shortName())
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    // 函数调用

    public static final class Call extends Instruction {
        private final Use<Function> function;
        public Use[] args;

        Call(BasicBlock block, Function function, Value... args) {
            super(block, function.funcType.returnType);
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
            return this.shortName() + " = call " + function + "(" +
                    Arrays.stream(args).map(v -> v.value.shortName()).collect(Collectors.joining(", ")) +
                    ")";
        }
    }

    public static final class CallExternal extends Instruction {
        public BuiltinFunction function;
        public Use[] args;

        CallExternal(BasicBlock block, String function, Value... args) {
            this(block, BuiltinFunction.of(function), args);
        }

        CallExternal(BasicBlock block, BuiltinFunction function, Value... args) {
            super(block, function.symbol.funcType.returnType);
            this.function = function;
            var argLen = args.length;
            this.args = new Use[argLen];
            for (int i = 0; i < argLen; i++) this.args[i] = use(args[i]);
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

}
