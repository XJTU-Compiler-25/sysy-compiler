package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public final class InstructionHelper {

    private InstructionHelper() { }

    // terminator instructions

    public static Instruction.Terminator.Ret ret(Value value) {
        return new Instruction.Terminator.Ret(value);
    }

    private static final Instruction.Terminator.RetV RetV = new Instruction.Terminator.RetV();
    public static Instruction.Terminator.RetV ret() {
        return RetV;
    }

    public static Instruction.Terminator.Jmp jmp(BasicBlock bb) {
        return new Instruction.Terminator.Jmp(bb);
    }

    public static Instruction.Terminator.Br br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
        return new Instruction.Terminator.Br(condition, trueTarget, falseTarget);
    }

    // common instructions

    public static Instruction.Call call(int index, Function func, Value... args) {
        return new Instruction.Call(index, func, args);
    }

    public static Instruction.Alloca alloca(int index, Type type) {
        return new Instruction.Alloca(index, type);
    }

    public static Instruction.Load load(int index, Value from) {
        return new Instruction.Load(index, from);
    }

    public static Instruction.Store store(Value target, Value value) {
        return new Instruction.Store(target, value);
    }

    public static Instruction.GetElemPtr getElementPtr(int index, Value ptr, Value... indices) {
        return new Instruction.GetElemPtr(index, ptr, indices);
    }

    public static Instruction.I2F i2f(int index, Value value) {
        Assertions.requires(value.type == Types.Int);
        return new Instruction.I2F(index, value);
    }

    public static Instruction.F2I f2i(int index, Value value) {
        Assertions.requires(value.type == Types.Float);
        return new Instruction.F2I(index, value);
    }

    public static Instruction.BitCastI2F bitCastI2F(int index, Value value) {
        Assertions.requires(value.type == Types.Int);
        return new Instruction.BitCastI2F(index, value);
    }

    public static Instruction.BitCastF2I bitCastF2I(int index, Value value) {
        Assertions.requires(value.type == Types.Float);
        return new Instruction.BitCastF2I(index, value);
    }

    public static Instruction add(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IAdd(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FAdd(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public static Instruction sub(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.ISub(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FSub(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public static Instruction mul(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMul(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FMul(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public static Instruction div(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IDiv(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FDiv(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public static Instruction mod(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMod(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FMod(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public static Instruction neg(int index, Value lhs) {
        return switch (lhs.type) {
            case Type.Int _ -> sub(index, ImmediateValues.iZero, lhs);
            case Type.Float _ -> new Instruction.FNeg(index, lhs);
            default -> Assertions.unsupported(lhs.type);
        };
    }

    public static Instruction shl(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Shl(index, lhs, rhs);
    }

    public static Instruction shr(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Shr(index, lhs, rhs);
    }

    public static Instruction ashr(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.AShr(index, lhs, rhs);
    }

    public static Instruction and(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.And(index, lhs, rhs);
    }

    public static Instruction or(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Or(index, lhs, rhs);
    }

    public static Instruction xor(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Xor(index, lhs, rhs);
    }

    // ~value = -1 ^ value
    public static Instruction not(int index, Value lhs) {
        Assertions.requires(lhs.type == Types.Int);
        return new Instruction.Xor(index, ImmediateValues.iNegOne, lhs);
    }

    // greater than
    public static Instruction eq(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IEq(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FEq(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater than
    public static Instruction ne(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.INe(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FNe(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater than
    public static Instruction gt(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IGt(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FGt(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // less than
    public static Instruction lt(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.ILt(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FLt(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater or equal
    public static Instruction ge(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IGe(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FGe(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // less or equal
    public static Instruction le(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.ILe(index, lhs, rhs);
            case Type.Float _ -> new Instruction.FLe(index, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // intrinsics

    public static Instruction fsqrt(int index, Value lhs) {
        Assertions.requires(lhs.type == Types.Float);
        return new Instruction.FSqrt(index, lhs);
    }

    public static Instruction fabs(int index, Value lhs) {
        Assertions.requires(lhs.type == Types.Float);
        return new Instruction.FAbs(index, lhs);
    }

    public static Instruction fmin(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FMin(index, lhs, rhs);
    }

    public static Instruction fmax(int index, Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FMax(index, lhs, rhs);
    }

}
