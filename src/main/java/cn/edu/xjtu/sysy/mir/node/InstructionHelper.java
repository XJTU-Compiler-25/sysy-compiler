package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public final class InstructionHelper {
    private BasicBlock block;

    public InstructionHelper() {}

    public InstructionHelper(BasicBlock block) {
        changeBlock(block);
    }

    public void changeBlock(BasicBlock block) {
        this.block = block;
    }

    private int getNewIndex() {
        return block.getFunction().incTempValueCounter();
    }

    public BasicBlock getBlock() {
        return block;
    }

    public boolean hasTerminator() {
        return block.terminator != null;
    }

    // terminator instructions

    public Instruction.Ret ret(Value value) {
        var instr = new Instruction.Ret(block, value);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.RetV ret() {
        var instr = new Instruction.RetV(block);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.Jmp jmp(BasicBlock bb) {
        var instr = new Instruction.Jmp(block, bb);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.Br br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.Br(block, condition, trueTarget, falseTarget);
        block.setTerminator(instr);
        return instr;
    }

    // common instructions

    public Instruction.Call call(Function func, Value... args) {
        var instr = new Instruction.Call(block, getNewIndex(), func, args);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.CallExternal callBuiltin(String name, Value... args) {
        var instr = new Instruction.CallExternal(block, getNewIndex(), name, args);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.Alloca alloca(Type type) {
        var instr = new Instruction.Alloca(block, getNewIndex(), type);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.Load load(Value from) {
        var instr = new Instruction.Load(block, getNewIndex(), from);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.Store store(Value target, Value value) {
        var ptrTy = (Type.Pointer) target.type;
        Assertions.requires(value.type == ptrTy.baseType,
                String.format("invalid type: typeof target(%s) = %s, value(%s) = %s", target, ptrTy, value, value.type));
        var instr = new Instruction.Store(block, target, value);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.GetElemPtr getElementPtr(Value ptr, Value... indices) {
        var instr = new Instruction.GetElemPtr(block, getNewIndex(), ptr, indices);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.I2F i2f(Value value) {
        Assertions.requires(value.type == Types.Int,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.I2F(block, getNewIndex(), value);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.F2I f2i(Value value) {
        Assertions.requires(value.type == Types.Float,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.F2I(block, getNewIndex(), value);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.BitCastI2F bitCastI2F(Value value) {
        Assertions.requires(value.type == Types.Int,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.BitCastI2F(block, getNewIndex(), value);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction.BitCastF2I bitCastF2I(Value value) {
        Assertions.requires(value.type == Types.Float,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.BitCastF2I(block, getNewIndex(), value);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction add(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IAdd(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FAdd(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    public Instruction sub(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.ISub(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FSub(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    public Instruction mul(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IMul(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FMul(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    public Instruction div(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IDiv(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FDiv(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    public Instruction mod(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IMod(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FMod(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    public Instruction neg(Value lhs) {
        var lType = lhs.type;
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.INeg(block, getNewIndex(), lhs);
            case Type.Float _ -> new Instruction.FNeg(block, getNewIndex(), lhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    public Instruction shl(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Shl(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction shr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Shr(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction ashr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.AShr(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction and(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.And(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction or(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Or(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction not(Value lhs) {
        var lType = lhs.type;
        Assertions.requires(lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lType));
        var instr = new Instruction.Not(block, getNewIndex(), lhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction xor(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Xor(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    // equal
    public Instruction eq(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IEq(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FEq(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    // not equal
    public Instruction ne(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.INe(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FNe(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    // greater than
    public Instruction gt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IGt(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FGt(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    // less than
    public Instruction lt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.ILt(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FLt(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    // greater or equal
    public Instruction ge(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IGe(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FGe(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    // less or equal
    public Instruction le(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.ILe(block, getNewIndex(), lhs, rhs);
            case Type.Float _ -> new Instruction.FLe(block, getNewIndex(), lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        block.addInstruction(instr);
        return instr;
    }

    // intrinsics

    public Instruction fsqrt(Value lhs) {
        Assertions.requires(lhs.type == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lhs.type));
        var instr = new Instruction.FSqrt(block, getNewIndex(), lhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction fabs(Value lhs) {
        Assertions.requires(lhs.type == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lhs.type));
        var instr = new Instruction.FAbs(block, getNewIndex(), lhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction fmin(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.FMin(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }

    public Instruction fmax(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.FMax(block, getNewIndex(), lhs, rhs);
        block.addInstruction(instr);
        return instr;
    }
}
