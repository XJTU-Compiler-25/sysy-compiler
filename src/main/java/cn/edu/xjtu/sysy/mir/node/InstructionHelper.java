package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public final class InstructionHelper {
    private int labelCounter = 0;

    private int newLabel() {
        return labelCounter++;
    }

    // terminator instructions

    public Terminator.Ret ret(Value value) {
        return new Terminator.Ret(value);
    }

    private final Terminator.RetV RetV = new Terminator.RetV();
    public Terminator.RetV ret() {
        return RetV;
    }

    public Terminator.Jmp jmp(BasicBlock bb) {
        return new Terminator.Jmp(bb);
    }

    public Terminator.Br br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
        return new Terminator.Br(condition, trueTarget, falseTarget);
    }

    // common instructions

    public Instruction.Call call(Function func, Value... args) {
        return new Instruction.Call(newLabel(), func, args);
    }

    public Instruction.Alloca alloca(Type type) {
        return new Instruction.Alloca(newLabel(), type);
    }

    public Instruction.Load load(Value ptr) {
        return new Instruction.Load(newLabel(), ptr);
    }

    public Instruction.Store store(Value value, Value ptr) {
        return new Instruction.Store(value, ptr);
    }

    public Instruction.GetElemPtr getElementPtr(Type type, Value ptr, Value... indices) {
        return new Instruction.GetElemPtr(newLabel(), type, ptr, indices);
    }

    public Instruction.I2F i2f(Value value) {
        Assertions.requires(value.type == Types.Int);
        return new Instruction.I2F(newLabel(), value);
    }

    public Instruction.F2I f2i(Value value) {
        Assertions.requires(value.type == Types.Float);
        return new Instruction.F2I(newLabel(), value);
    }

    public Instruction.BitCastI2F bitCastI2F(Value value) {
        Assertions.requires(value.type == Types.Int);
        return new Instruction.BitCastI2F(newLabel(), value);
    }

    public Instruction.BitCastF2I bitCastF2I(Value value) {
        Assertions.requires(value.type == Types.Float);
        return new Instruction.BitCastF2I(newLabel(), value);
    }

    public Instruction add(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IAdd(newLabel(), lhs, rhs);
            case Type.Float _ -> new Instruction.FAdd(newLabel(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction sub(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.ISub(newLabel(), lhs, rhs);
            case Type.Float _ -> new Instruction.FSub(newLabel(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction mul(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMul(newLabel(), lhs, rhs);
            case Type.Float _ -> new Instruction.FMul(newLabel(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction div(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IDiv(newLabel(), lhs, rhs);
            case Type.Float _ -> new Instruction.FDiv(newLabel(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction mod(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMod(newLabel(), lhs, rhs);
            case Type.Float _ -> new Instruction.FMod(newLabel(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction neg(Value lhs) {
        return switch (lhs.type) {
            case Type.Int _ -> sub(Constants.iZero, lhs);
            case Type.Float _ -> new Instruction.FNeg(newLabel(), lhs);
            default -> Assertions.unsupported(lhs.type);
        };
    }

    public Instruction shl(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Shl(newLabel(), lhs, rhs);
    }

    public Instruction shr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Shr(newLabel(), lhs, rhs);
    }

    public Instruction ashr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.AShr(newLabel(), lhs, rhs);
    }

    public Instruction and(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.And(newLabel(), lhs, rhs);
    }

    public Instruction or(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Or(newLabel(), lhs, rhs);
    }

    public Instruction xor(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Xor(newLabel(), lhs, rhs);
    }

    // ~value = -1 ^ value
    public Instruction not(Value lhs) {
        Assertions.requires(lhs.type == Types.Int);
        return new Instruction.Xor(newLabel(), Constants.iNegOne, lhs);
    }

    public Instruction icmp(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.ICmp(newLabel(), lhs, rhs);
    }

    public Instruction fcmp(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FCmp(newLabel(), lhs, rhs);
    }

    // intrinsics

    public Instruction fsqrt(Value lhs) {
        Assertions.requires(lhs.type == Types.Float);
        return new Instruction.FSqrt(newLabel(), lhs);
    }

    public Instruction fabs(Value lhs) {
        Assertions.requires(lhs.type == Types.Float);
        return new Instruction.FAbs(newLabel(), lhs);
    }

    public Instruction fmin(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FMin(newLabel(), lhs, rhs);
    }

    public Instruction fmax(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FMax(newLabel(), lhs, rhs);
    }

}
