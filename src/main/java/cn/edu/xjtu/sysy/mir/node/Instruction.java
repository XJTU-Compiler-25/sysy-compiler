package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

import java.util.List;

import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

/**
 * 建议通过 {@link InstructionHelper} 构造指令
 * 请注意整数运算指令都是 signed 的
 */
public abstract sealed class Instruction extends User {
    // local label
    public final int label;

    Instruction(int label, Type type) {
        super(type);
        this.label = label;
    }

    // 计算中间值应该都为 local value
    @Override
    public String shortName() {
        return "%" + label;
    }

    @Override
    public abstract String toString();

    // 基本块结束指令
    public abstract sealed static class Terminator extends Instruction {
        Terminator() {
            super(-1, Types.Void);
        }
    }

    // 带值返回 return
    public static final class Ret extends Terminator {
        public Use retVal;

        Ret(Value retVal) {
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
        @Override
        public String toString() {
            return "ret void";
        }
    }

    // 无条件跳转 jump
    public static final class Jmp extends Terminator {
        public BasicBlock target;
        public List<Use> params;

        Jmp(BasicBlock target) {
            this.target = target;
        }

        @Override
        public String toString() {
            return "jmp " + target.label;
        }
    }

    // 分支 branch
    public static final class Br extends Terminator {
        public Use condition;
        public BasicBlock trueTarget;
        public BasicBlock falseTarget;
        public List<Use> trueParams;
        public List<Use> falseParams;

        Br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
            this.condition = use(condition);
            this.trueTarget = trueTarget;
            this.falseTarget = falseTarget;
        }

        @Override
        public String toString() {
            return "br " + condition.value.shortName() + ", " + trueTarget.label + ", " + falseTarget.label;
        }
    }

    // 函数调用

    public static final class Call extends Instruction {
        public Function function;
        public Use[] args;

        Call(int label, Function function, Value... args) {
            super(label, function.returnType);
            this.function = function;
            var argLen = args.length;
            this.args = new Use[argLen];
            for (int i = 0; i < argLen; i++) this.args[i] = use(args[i]);
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            sb.append('%').append(this.label).append(" = call ").append(function.name).append("(");
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i].value.shortName());
                if (i != args.length - 1) sb.append(", ");
            }
            sb.append(')');
            return sb.toString();
        }
    }
    // 内存操作

    /**
     * stack allocate
     */
    public static final class Alloca extends Instruction {
        public Type allocatedType;

        Alloca(int label, Type type) {
            super(label, Types.ptrOf(type));
            this.allocatedType = type;
        }

        @Override
        public String toString() {
            return String.format("%s = alloca %s", this.shortName(), allocatedType.toString());
        }
    }

    public static final class Load extends Instruction {
        public Use address;

        Load(int label, Value address) {
            super(label, ((Type.Pointer) address.type).baseType);
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

        Store(Value address, Value value) {
            super(-1, Types.Void);
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

        GetElemPtr(int label, Value basePtr, Value[] indices) {
            super(label, ((Type.Pointer) basePtr.type).baseType);
            this.basePtr = use(basePtr);
            var indexCount = indices.length;
            this.indices = new Use[indexCount];
            for (int i = 0; i < indexCount; i++) this.indices[i] = use(indices[i]);
        }

        @Override
        public String toString() {
            var sb = new StringBuilder(String.format("%s = getelemptr base %s", this.shortName(), basePtr.value.shortName()));
            for (var index : indices) sb.append(", ").append(index.value.shortName());
            return sb.toString();
        }
    }

    // cast

    /**
     * 整数转浮点数
     */
    public static final class I2F extends Instruction {
        public Value value;

        I2F(int label, Value value) {
            super(label, Types.Float);
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s = i2f %s", this.shortName(), value.shortName());
        }
    }

    /**
     * 浮点数转整数
     */
    public static final class F2I extends Instruction {
        public Value value;

        F2I(int label, Value value) {
            super(label, Types.Int);
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s = f2i %s", this.shortName(), value.shortName());
        }
    }

    /**
     * 不实际转换地将某整数值当作浮点数
     */
    public static final class BitCastI2F extends Instruction {
        public Value value;

        BitCastI2F(int label, Value value) {
            super(label, Types.Float);
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s = i2f bitcast %s", this.shortName(), value.shortName());
        }
    }

    /**
     * 不实际转换地将某整数值当作浮点数
     */
    public static final class BitCastF2I extends Instruction {
        public Value value;

        BitCastF2I(int label, Value value) {
            super(label, Types.Int);
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s = f2i bitcast %s", this.shortName(), value.shortName());
        }
    }

    // 算数指令

    public static final class IAdd extends Instruction {
        public Value lhs;
        public Value rhs;

        IAdd(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = iadd %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class ISub extends Instruction {
        public Value lhs;
        public Value rhs;

        ISub(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = isub %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class IMul extends Instruction {
        public Value lhs;
        public Value rhs;

        IMul(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = imul %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class IDiv extends Instruction {
        public Value lhs;
        public Value rhs;

        IDiv(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = idiv %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class IMod extends Instruction {
        public Value lhs;
        public Value rhs;

        IMod(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = imod %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FAdd extends Instruction {
        public Value lhs;
        public Value rhs;

        FAdd(int label, Value lhs, Value rhs) {
            super(label, Types.Float);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fadd %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FSub extends Instruction {
        public Value lhs;
        public Value rhs;

        FSub(int label, Value lhs, Value rhs) {
            super(label, Types.Float);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fsub %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FMul extends Instruction {
        public Value lhs;
        public Value rhs;

        FMul(int label, Value lhs, Value rhs) {
            super(label, Types.Float);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fmul %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FDiv extends Instruction {
        public Value lhs;
        public Value rhs;

        FDiv(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fdiv %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FMod extends Instruction {
        public Value lhs;
        public Value rhs;

        FMod(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fmod %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FNeg extends Instruction {
        public Value lhs;

        FNeg(int label, Value lhs) {
            super(label, Types.Float);
            this.lhs = lhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fneg %s", this.shortName(), lhs.shortName());
        }
    }

    // 位运算

    /**
     * 左移位 shift left
     */
    public static final class Shl extends Instruction {
        public Value lhs;
        public Value rhs;

        Shl(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = shl %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    /**
     * 不处理符号！
     * 右移位 right shift
     */
    public static final class Shr extends Instruction {
        public Value lhs;
        public Value rhs;

        Shr(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = shr %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    /**
     * 算数右移位 arithmetic shift right
     */
    public static final class AShr extends Instruction {
        public Value lhs;
        public Value rhs;

        AShr(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = ashr %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class And extends Instruction {
        public Value lhs;
        public Value rhs;

        And(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = and %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class Or extends Instruction {
        public Value lhs;
        public Value rhs;

        Or(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = or %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class Xor extends Instruction {
        public Value lhs;
        public Value rhs;

        Xor(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = xor %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    // 比较指令

    public static final class IEq extends Instruction {
        public Value lhs;
        public Value rhs;

        IEq(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp eq %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class INe extends Instruction {
        public Value lhs;
        public Value rhs;

        INe(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp ne %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class IGt extends Instruction {
        public Value lhs;
        public Value rhs;

        IGt(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp gt %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class ILt extends Instruction {
        public Value lhs;
        public Value rhs;

        ILt(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp lt %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class IGe extends Instruction {
        public Value lhs;
        public Value rhs;

        IGe(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp ge %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class ILe extends Instruction {
        public Value lhs;
        public Value rhs;

        ILe(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp le %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FEq extends Instruction {
        public Value lhs;
        public Value rhs;

        FEq(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp eq %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FNe extends Instruction {
        public Value lhs;
        public Value rhs;

        FNe(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp ne %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FGt extends Instruction {
        public Value lhs;
        public Value rhs;

        FGt(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp gt %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FLt extends Instruction {
        public Value lhs;
        public Value rhs;

        FLt(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp lt %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FGe extends Instruction {
        public Value lhs;
        public Value rhs;

        FGe(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp ge %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FLe extends Instruction {
        public Value lhs;
        public Value rhs;

        FLe(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp le %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    // intrinsic 指令

    public static final class FSqrt extends Instruction {
        public Value lhs;

        FSqrt(int label, Value lhs) {
            super(label, Types.Float);
            this.lhs = lhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fsqrt %s", this.shortName(), lhs.shortName());
        }
    }

    public static final class FAbs extends Instruction {
        public Value lhs;

        FAbs(int label, Value lhs) {
            super(label, Types.Float);
            this.lhs = lhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fabs %s", this.shortName(), lhs.shortName());
        }
    }

    public static final class FMin extends Instruction {
        public Value lhs;
        public Value rhs;

        FMin(int label, Value lhs, Value rhs) {
            super(label, Types.Float);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fmin %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

    public static final class FMax extends Instruction {
        public Value lhs;
        public Value rhs;

        FMax(int label, Value lhs, Value rhs) {
            super(label, Types.Float);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fmax %s, %s", this.shortName(), lhs.shortName(), rhs.shortName());
        }
    }

}
