package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public final class InstructionHelper {
    private final BasicBlock block;

    public InstructionHelper(BasicBlock block) {
        this.block = block;
    }

    private int getNewIndex() {
        return block.getFunction().incTempValueCounter();
    }

    // terminator instructions

    public Instruction.Ret ret(Value value) {
        return new Instruction.Ret(block, value);
    }

    public Instruction.RetV ret() {
        return new Instruction.RetV(block);
    }

    public Instruction.Jmp jmp(BasicBlock bb) {
        return new Instruction.Jmp(block, bb);
    }

    public Instruction.Br br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
        return new Instruction.Br(block, condition, trueTarget, falseTarget);
    }

    // common instructions

    public Instruction.Call call(Function func, Value... args) {
        return new Instruction.Call(block, getNewIndex(), func, args);
    }

    public Instruction.Call callExternal(Type retType, Value... args) {
        return new Instruction.Call(block, getNewIndex(), retType, args);
    }

    public Instruction.Alloca alloca(Type type) {
        return new Instruction.Alloca(block, getNewIndex(), type);
    }

    public Instruction.Load load(Value from) {
        return new Instruction.Load(block, getNewIndex(), from);
    }

    public Instruction.Store store(Value target, Value value) {
        return new Instruction.Store(block, target, value);
    }

    public Instruction.GetElemPtr getElementPtr(Value ptr, Value... indices) {
        return new Instruction.GetElemPtr(block, getNewIndex(), ptr, indices);
    }

    public Instruction.I2F i2f(Value value) {
        Assertions.requires(value.type == Types.Int);
        return new Instruction.I2F(block, getNewIndex(), value);
    }

    public Instruction.F2I f2i(Value value) {
        Assertions.requires(value.type == Types.Float);
        return new Instruction.F2I(block, getNewIndex(), value);
    }

    public Instruction.BitCastI2F bitCastI2F(Value value) {
        Assertions.requires(value.type == Types.Int);
        return new Instruction.BitCastI2F(block, getNewIndex(), value);
    }

    public Instruction.BitCastF2I bitCastF2I(Value value) {
        Assertions.requires(value.type == Types.Float);
        return new Instruction.BitCastF2I(block, getNewIndex(), value);
    }

    public Instruction add(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type, String.format("invalid type: %s %s", lhs, rhs));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IAdd(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FAdd(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction sub(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type, String.format("invalid type: %s %s", lhs.type, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.ISub(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FSub(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction mul(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type, String.format("invalid type: %s %s", lhs, rhs));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMul(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FMul(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction div(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type, String.format("invalid type: %s %s", lhs.type, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IDiv(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FDiv(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction mod(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type, String.format("invalid type: %s %s", lhs.type, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMod(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FMod(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction neg(Value lhs) {
        return switch (lhs.type) {
            case Type.Int _ -> sub(ImmediateValues.iZero, lhs);
            case Type.Float _ -> new Instruction.FNeg(block, getNewIndex(), lhs);
            default -> Assertions.unsupported(lhs.type);
        };
    }

    public Instruction shl(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Shl(block, getNewIndex(), lhs, rhs);
    }

    public Instruction shr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Shr(block, getNewIndex(), lhs, rhs);
    }

    public Instruction ashr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.AShr(block, getNewIndex(), lhs, rhs);
    }

    public Instruction and(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.And(block, getNewIndex(), lhs, rhs);
    }

    public Instruction or(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Or(block, getNewIndex(), lhs, rhs);
    }

    public Instruction xor(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int);
        return new Instruction.Xor(block, getNewIndex(), lhs, rhs);
    }

    // ~value = -1 ^ value
    public Instruction not(Value lhs) {
        Assertions.requires(lhs.type == Types.Int);
        return new Instruction.Xor(block, getNewIndex(), ImmediateValues.iNegOne, lhs);
    }

    // equal
    public Instruction eq(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IEq(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FEq(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // not equal
    public Instruction ne(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.INe(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FNe(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater than
    public Instruction gt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IGt(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FGt(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // less than
    public Instruction lt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.ILt(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FLt(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater or equal
    public Instruction ge(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.IGe(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FGe(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // less or equal
    public Instruction le(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type);
        return switch (lType) {
            case Type.Int _ -> new Instruction.ILe(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FLe(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // intrinsics

    public Instruction fsqrt(Value lhs) {
        Assertions.requires(lhs.type == Types.Float);
        return new Instruction.FSqrt(block, getNewIndex(), lhs);
    }

    public Instruction fabs(Value lhs) {
        Assertions.requires(lhs.type == Types.Float);
        return new Instruction.FAbs(block, getNewIndex(), lhs);
    }

    public Instruction fmin(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FMin(block, getNewIndex(), lhs, rhs);
    }

    public Instruction fmax(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float);
        return new Instruction.FMax(block, getNewIndex(), lhs, rhs);
    }

}
