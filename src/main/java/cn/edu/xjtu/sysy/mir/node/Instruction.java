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

    public final void setBlock(BasicBlock block) {
        this.block = block;
    }

    public final BasicBlock getBlock() {
        return block;
    }

    public final boolean notProducingValue() {
        return type == Types.Void;
    }


    public final boolean producingValue() {
        return type != Types.Void;

    public final List<Value> getOperands() {
        return used.stream().map(it -> it.value).collect(Collectors.toList());
    }

    // 计算中间值应该都为 local value
    @Override
    public final String shortName() {
        if (position == null) return "%" + id;
        else return "%" + id + "[" + position.toString() + "]";
    }

    @Override
    public abstract String toString();

    public final void insertBefore(Instruction instr) {
        if (this instanceof Terminator) {
            block.instructions.add(instr);
            return;
        }
        int idx = block.instructions.indexOf(this);
        block.instructions.add(idx, instr);
    }

    public final void insertAfter(Instruction instr) {
        Assertions.requires(!(this instanceof Terminator));
        int idx = block.instructions.indexOf(this);
        block.instructions.add(idx + 1, instr);
    }

    public final void replaceWith(Instruction instr) {
        int idx = block.instructions.indexOf(this);
        block.instructions.set(idx, instr);
        replaceAllUsesWith(instr);
        dispose();
    }

    public abstract sealed static class AbstractUnary extends Instruction {
        public final Use operand;

        AbstractUnary(BasicBlock block, Type type, Value operand) {
            super(block, type);
            this.operand = use(operand);
        }

        public Value getOperand() {
            return operand.value;
        }

        public void setOperand(Value value) {
            this.operand.replaceValue(value);
        }

        protected final String toString(String name) {
            return String.format("%s = %s %s", this.shortName(), name, getOperand().shortName());
        }
    }

    public abstract sealed static class AbstractBinary extends Instruction {
        public final Use lhs;
        public final Use rhs;

        AbstractBinary(BasicBlock block, Type type, Value lhs, Value rhs) {
            super(block, type);
            this.lhs = use(lhs);
            this.rhs = use(rhs);
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
        }

        public void setLhs(Value value) {
            this.lhs.replaceValue(value);
        }

        public void setRhs(Value value) {
            this.rhs.replaceValue(value);
        }

        protected final String toString(String name) {
            return String.format("%s = %s %s, %s", this.shortName(), name, getLhs().shortName(), getRhs().shortName());
        }
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

        public void replaceParam(BasicBlock block, BlockArgument oldArg, BlockArgument newArg) { unsupported(this); }
    }

    // 带值返回 return
    public static final class Ret extends Terminator {
        private final Use retVal;

        Ret(BasicBlock block, Value retVal) {
            super(block);
            this.retVal = use(retVal);
        }
        
        public Value getRetVal() {
            return retVal.value;
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
        public void replaceParam(BasicBlock block, BlockArgument oldArg, BlockArgument newArg) {
            Assertions.requires(block == getTarget());
            var use = params.remove(oldArg);
            if (use != null) params.put(newArg, use);
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

        public void setTrueTarget(BasicBlock newTarget) {
            trueTarget.replaceValue(newTarget);
        }

        public void setFalseTarget(BasicBlock newTarget) {
            falseTarget.replaceValue(newTarget);
        }

        @Override
        public void replaceTarget(BasicBlock oldTarget, BasicBlock newTarget) {
            if (oldTarget == trueTarget.value) setTrueTarget(newTarget);
            if (oldTarget == falseTarget.value) setFalseTarget(newTarget);
        }

        public void putTrueParam(BlockArgument arg, Value value) {
            var use = trueParams.get(arg);
            if (use == null) trueParams.put(arg, use(value));
            else use.replaceValue(value);
        }

        public void putFalseParam(BlockArgument arg, Value value) {
            var use = falseParams.get(arg);
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

        @Override
        public void replaceParam(BasicBlock block, BlockArgument oldArg, BlockArgument newArg) {
            if (block == trueTarget.value) {
                var use = trueParams.remove(oldArg);
                if (use != null) trueParams.put(newArg, use);
            }
            if (block == falseTarget.value) {
                var use = falseParams.remove(oldArg);
                if (use != null) falseParams.put(newArg, use);
            }
        }

        protected final String toString(String prefix) {
            return prefix + " " + trueTarget.value.shortName() + "(" +
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
            return toString("br " + condition.value.shortName() + ",");
        }
    }

    // 函数调用

    public static sealed abstract class AbstractCall extends Instruction {
        public final ArrayList<Use> args = new ArrayList<>();

        AbstractCall(BasicBlock block, Type type, Value... args) {
            super(block, type);
            var argLen = args.length;
            for (var arg : args) this.args.add(use(arg));
        }

        public List<Value> getArgs() {
            return args.stream().map(it -> it.value).toList();
        }

        public Value getArg(int index) {
            return args.get(index).value;
        }

        public void setArg(int index, Value value) {
            args.get(index).replaceValue(value);
        }

        public void removeArg(int index) {
            args.remove(index);
        }

        public abstract String getLabel();
    }

    public static final class Call extends AbstractCall {
        private final Use<Function> callee;

        Call(BasicBlock block, Function callee, Value... args) {
            super(block, callee.funcType.returnType, args);
            this.callee = use(callee);
        }

        public void setCallee(Function newFunc) {
            callee.replaceValue(newFunc);
        }

        public Function getCallee() {
            return callee.value;
        }

        @Override
        public String toString() {
            return this.shortName() + " = call " + callee + "(" +
                    args.stream().map(v -> v.value.shortName()).collect(Collectors.joining(", ")) +
                    ")";
        }

        @Override
        public String getLabel() {
            return getCallee().name;
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
                    args.stream().map(v -> v.value.shortName()).collect(Collectors.joining(", ")) +
                    ")";
        }

        @Override
        public String getLabel() {
            return function.linkName;
        }
    }

    // 内存操作

    /**
     * stack allocate
     */
    public static final class Alloca extends Instruction {
        public Type allocatedType;
        public boolean zeroInit;
        Alloca(BasicBlock block, Type type) {
            this(block, type, false);
        }

        Alloca(BasicBlock block, Type type, boolean zeroInit) {
            super(block, Types.ptrOf(type));
            this.allocatedType = type;
            this.zeroInit = zeroInit;
        }

        @Override
        public String toString() {
            return String.format("%s = alloca %s", this.shortName(), allocatedType.toString());
        }
    }

    public static final class Load extends Instruction {
        private final Use address;

        Load(BasicBlock block, Value address) {
            super(block, ((Type.Pointer) address.type).baseType);
            this.address = use(address);
        }

        @Override
        public String toString() {
            return String.format("%s = load ptr %s", this.shortName(), address.value.shortName());
        }

        public Value getAddress() {
            return address.value;
        }

        public void setAddress(Value address) {
            this.address.replaceValue(address);
        }
    }

    public static final class Store extends Instruction {
        private final Use address;
        private final Use storeVal;

        Store(BasicBlock block, Value address, Value value) {
            super(block, Types.Void);
            this.address = use(address);
            this.storeVal = use(value);
        }

        @Override
        public String toString() {
            return String.format("store ptr %s, value %s", address.value.shortName(), storeVal.value.shortName());
        }

        public Value getAddress() {
            return address.value;
        }

        public void setAddress(Value address) {
            this.address.replaceValue(address);
        }

        public Value getStoreVal() {
            return storeVal.value;
        }

        public void setStoreVal(Value storeVal) {
            this.storeVal.replaceValue(storeVal);
        }
    }

    /**
     * 仅计算元素的指针而不访问
     * 举例：getelemptr [Any x 20 x i32], n = [20 x i32]
     */
    public static final class GetElemPtr extends Instruction {
        private final Use basePtr;
        private final ArrayList<Use> indices;

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
            this.indices = new ArrayList<>();
            for (var index : indices) addIndex(index);
        }

        @Override
        public String toString() {
            return shortName() + " = getelemptr base " + basePtr.value.shortName() + ", indices " +
                    indices.stream().map(v -> v.value.shortName()).collect(Collectors.joining(", "));
        }

        public Value getBasePtr() {
            return basePtr.value;
        }

        public void setBasePtr(Value basePtr) {
            this.basePtr.replaceValue(basePtr);
        }

        public List<Value> getIndices() {
            return indices.stream().map(v -> v.value).collect(Collectors.toList());
        }

        public Value getIndex(int idx) {
            return indices.get(idx).value;
        }

        public void addIndex(Value index) {
            indices.add(use(index));
        }

        public void addIndex(int idx, Value index) {
            indices.add(idx, use(index));
        }

        public void setIndex(int idx, Value index) {
            indices.get(idx).replaceValue(index);
        }

        public void removeIndex(int idx) {
            var use = indices.remove(idx);
            if (use != null) use.dispose();
        }

        public int getIndexCount() {
            return indices.size();
        }
    }

    // cast

    /**
     * 整数转浮点数
     */
    public static final class I2F extends AbstractUnary {
        I2F(BasicBlock block, Value value) {
            super(block, Types.Float, value);
        }

        @Override
        public String toString() {
            return toString("i2f");
        }
    }

    /**
     * 浮点数转整数
     */
    public static final class F2I extends AbstractUnary {
        F2I(BasicBlock block, Value value) {
            super(block, Types.Int, value);
        }

        @Override
        public String toString() {
            return toString("f2i");
        }
    }

    /**
     * 不实际转换地将某整数值当作浮点数
     */
    public static final class BitCastI2F extends AbstractUnary {
        BitCastI2F(BasicBlock block, Value value) {
            super(block, Types.Float, value);
        }

        @Override
        public String toString() {
            return toString("i2f bitcast");
        }
    }

    /**
     * 不实际转换地将某整数值当作浮点数
     */
    public static final class BitCastF2I extends AbstractUnary {
        BitCastF2I(BasicBlock block, Value value) {
            super(block, Types.Int, value);
        }

        @Override
        public String toString() {
            return toString("f2i bitcast");
        }
    }

    // 算数指令

    public static final class IAdd extends AbstractBinary {
        IAdd(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("iadd");
        }
    }

    public static final class ISub extends AbstractBinary {
        ISub(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("isub");
        }
    }

    public static final class IMul extends AbstractBinary {
        IMul(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("imul");
        }
    }

    public static final class IDiv extends AbstractBinary {
        IDiv(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("idiv");
        }
    }

    public static final class IMod extends AbstractBinary {
        IMod(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("imod");
        }
    }

    public static final class INeg extends AbstractUnary {
        INeg(BasicBlock block, Value lhs) {
            super(block, Types.Int, lhs);
        }

        @Override
        public String toString() {
            return toString("ineg");
        }
    }

    public static final class FAdd extends AbstractBinary {
        FAdd(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fadd");
        }
    }

    public static final class FSub extends AbstractBinary {
        FSub(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fsub");
        }
    }

    public static final class FMul extends AbstractBinary {
        FMul(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fmul");
        }
    }

    public static final class FDiv extends AbstractBinary {
        FDiv(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fdiv");
        }
    }

    public static final class FMod extends AbstractBinary {
        FMod(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fmod");
        }
    }

    public static final class FNeg extends AbstractUnary {
        FNeg(BasicBlock block, Value lhs) {
            super(block, Types.Float, lhs);
        }

        @Override
        public String toString() {
            return toString("fneg");
        }
    }

    // 位运算

    /**
     * 左移位 shift left
     */
    public static final class Shl extends AbstractBinary {
        Shl(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("shl");
        }
    }

    /**
     * 不处理符号！
     * 右移位 right shift
     */
    public static final class Shr extends AbstractBinary {
        Shr(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("shr");
        }
    }

    /**
     * 算数右移位 arithmetic shift right
     */
    public static final class AShr extends AbstractBinary {
        AShr(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("ashr");
        }
    }

    public static final class And extends AbstractBinary {
        And(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("and");
        }
    }

    public static final class Or extends AbstractBinary {
        Or(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("or");
        }
    }

    public static final class Xor extends AbstractBinary {
        Xor(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("xor");
        }
    }

    public static final class Not extends AbstractUnary {
        Not(BasicBlock block, Value rhs) {
            super(block, Types.Int, rhs);
        }

        @Override
        public String toString() {
            return toString("not");
        }
    }

    // 比较指令

    public static final class IEq extends AbstractBinary {
        IEq(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("icmp eq");
        }
    }

    public static final class INe extends AbstractBinary {
        INe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("icmp ne");
        }
    }

    public static final class IGt extends AbstractBinary {
        IGt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("icmp gt");
        }
    }

    public static final class ILt extends AbstractBinary {
        ILt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("icmp lt");
        }
    }

    public static final class IGe extends AbstractBinary {
        IGe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("icmp ge");
        }
    }

    public static final class ILe extends AbstractBinary {
        ILe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("icmp le");
        }
    }

    public static final class FEq extends AbstractBinary {
        FEq(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fcmp eq");
        }
    }

    public static final class FNe extends AbstractBinary {
        FNe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fcmp ne");
        }
    }

    public static final class FGt extends AbstractBinary {
        FGt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fcmp gt");
        }
    }

    public static final class FLt extends AbstractBinary {
        FLt(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fcmp lt");
        }
    }

    public static final class FGe extends AbstractBinary {
        FGe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fcmp ge");
        }
    }

    public static final class FLe extends AbstractBinary {
        FLe(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Int, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fcmp le");
        }
    }

    // intrinsic 指令

    public static final class FSqrt extends AbstractUnary {
        FSqrt(BasicBlock block, Value lhs) {
            super(block, Types.Float, lhs);
        }

        @Override
        public String toString() {
            return toString("fsqrt");
        }
    }

    public static final class FAbs extends AbstractUnary {
        FAbs(BasicBlock block, Value lhs) {
            super(block, Types.Float, lhs);
        }

        @Override
        public String toString() {
            return toString("fabs");
        }
    }

    public static final class FMin extends AbstractBinary {
        FMin(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fmin");
        }
    }

    public static final class FMax extends AbstractBinary {
        FMax(BasicBlock block, Value lhs, Value rhs) {
            super(block, Types.Float, lhs, rhs);
        }

        @Override
        public String toString() {
            return toString("fmax");
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
            this.dst = dst;
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("imv %s, %s", dst.shortName(), src.value.shortName());
        }

        public Value getSrc() {
            return src.value;
        }
    }

    public static final class FMv extends Instruction {
        public Value dst;
        public Use src;

        FMv(BasicBlock block, Value dst, Value src) {
            super(block, Types.Void);
            this.dst = dst;
            this.src = use(src);
        }

        @Override
        public String toString() {
            return String.format("fmv %s, %s", dst.shortName(), src.value.shortName());
        }

        public Value getSrc() {
            return src.value;
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
            return toString("beq " + lhs.value.shortName() + ", " + rhs.value.shortName() + ",");
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
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
            return toString("bne " + lhs.value.shortName() + ", " + rhs.value.shortName() + ",");
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
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
            return toString("blt " + lhs.value.shortName() + ", " + rhs.value.shortName() + ",");
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
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
            return toString("bge " + lhs.value.shortName() + ", " + rhs.value.shortName() + ",");
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
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
            return toString("ble " + lhs.value.shortName() + ", " + rhs.value.shortName() + ",");
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
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
            return toString("bgt " + lhs.value.shortName() + ", " + rhs.value.shortName() + ",");
        }

        public Value getLhs() {
            return lhs.value;
        }

        public Value getRhs() {
            return rhs.value;
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
            if (uses.length > 0) return shortName() + " = dummy use " + Arrays.stream(uses)
                                        .map(v -> v.value.shortName())
                                        .collect(Collectors.joining(", "));
            else return shortName() + " = dummy";
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


        public Value getLhs() {
            return lhs.value;
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
