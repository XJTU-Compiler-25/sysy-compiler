package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

/**
 * 非终结指令
 * 建议通过 {@link InstructionHelper} 构造指令
 *
 * 请注意整数运算指令都是 signed 的
 */
public abstract sealed class Instruction extends Value  {
    // local label
    public final int label;

    Instruction(int label, Type type) {
        super(type);
        this.label = label;
    }

    // 计算中间值应该都为 local value
    @Override
    public String shallowToString() {
        return "%" + label;
    }

    @Override
    public abstract String toString();

    // 函数调用

    public static final class Call extends Instruction {
        public Function function;
        public Value[] args;

        Call(int label, Function function, Value... args) {
            super(label, function.returnType);
            this.function = function;
            this.args = args;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            sb.append(this.label).append(" = call ").append(function.name).append("(");
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i].shallowToString());
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
            return String.format("%s = alloca %s", this.shallowToString(), allocatedType.toString());
        }
    }

    public static final class Load extends Instruction {
        public Value address;

        Load(int label, Value address) {
            super(label, ((Type.Pointer) address.type).baseType);
            this.address = address;
        }

        @Override
        public String toString() {
            return String.format("%s = load ptr %s", this.shallowToString(), address.shallowToString());
        }
    }

    public static final class Store extends Instruction {
        public Value address;
        public Value value;

        Store(Value address, Value value) {
            super(0, Types.Void);
            this.address = address;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("store ptr %s, value %s", address.shallowToString(), value.shallowToString());
        }
    }

    /**
     * 仅计算元素的指针而不访问
     */
    public static final class GetElemPtr extends Instruction {
        public Value basePtr;
        public Value[] indices;

        GetElemPtr(int label, Type type, Value basePtr, Value[] indices) {
            super(label, type);
            this.basePtr = basePtr;
            this.indices = indices;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder(String.format("%s = getelemptr base %s", this.shallowToString(), basePtr.shallowToString()));
            for (var index : indices) sb.append(", ").append(index.shallowToString());
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
            return String.format("%s = i2f %s", this.shallowToString(), value.shallowToString());
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
            return String.format("%s = f2i %s", this.shallowToString(), value.shallowToString());
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
            return String.format("%s = i2f bitcast %s", this.shallowToString(), value.shallowToString());
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
            return String.format("%s = f2i bitcast %s", this.shallowToString(), value.shallowToString());
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
            return String.format("%s = iadd %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = isub %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = imul %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = idiv %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = imod %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fadd %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fsub %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fmul %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fdiv %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fmod %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fneg %s", this.shallowToString(), lhs.shallowToString());
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
            return String.format("%s = shl %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = shr %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = ashr %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = and %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = or %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = xor %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
        }
    }

    // 比较指令

    public static final class ICmp extends Instruction {
        public Value lhs;
        public Value rhs;

        ICmp(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = icmp %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
        }
    }

    public static final class FCmp extends Instruction {
        public Value lhs;
        public Value rhs;

        FCmp(int label, Value lhs, Value rhs) {
            super(label, Types.Int);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return String.format("%s = fcmp %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fsqrt %s", this.shallowToString(), lhs.shallowToString());
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
            return String.format("%s = fabs %s", this.shallowToString(), lhs.shallowToString());
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
            return String.format("%s = fmin %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
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
            return String.format("%s = fmax %s, %s", this.shallowToString(), lhs.shallowToString(), rhs.shallowToString());
        }
    }

}
