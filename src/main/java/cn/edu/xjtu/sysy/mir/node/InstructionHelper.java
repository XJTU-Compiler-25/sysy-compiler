package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public final class InstructionHelper {
    private int insertPosition;
    private BasicBlock block;

    public InstructionHelper() {}

    public InstructionHelper(BasicBlock block) {
        changeBlock(block);
    }

    public InstructionHelper(BasicBlock block, int initialPos) {
        changeBlock(block);
        moveCursor(initialPos);
    }

    public void changeBlock(BasicBlock block) {
        this.block = block;
        moveToTail();
    }

    public void changeBlockToNull() {
        block = null;
        insertPosition = -1;
    }

    public BasicBlock getBlock() {
        return block;
    }

    public boolean hasTerminator() {
        return block.terminator != null;
    }

    public int getCursor() {
        return insertPosition;
    }

    public void moveCursor(int insertPosition) {
        this.insertPosition = insertPosition;
    }

    public void moveToHead() {
        insertPosition = 0;
    }

    public void moveToTail() {
        insertPosition = block.instructions.size();
    }

    public void insert(Instruction instruction) {
        block.instructions.add(insertPosition, instruction);
        instruction.setBlock(block);
        insertPosition++;
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
        var instr = new Instruction.Call(block, func, args);
        insert(instr);
        return instr;
    }

    public Instruction.CallExternal callBuiltin(String name, Value... args) {
        var instr = new Instruction.CallExternal(block, name, args);
        insert(instr);
        return instr;
    }

    public Instruction.Alloca alloca(Type type) {
        var instr = new Instruction.Alloca(block, type);
        insert(instr);
        return instr;
    }

    public Instruction.Load load(Value from) {
        var instr = new Instruction.Load(block, from);
        insert(instr);
        return instr;
    }

    public Instruction.Store store(Value target, Value value) {
        var ptrTy = (Type.Pointer) target.type;
        Assertions.requires(value.type == ptrTy.baseType,
                String.format("invalid type: typeof target(%s) = %s, value(%s) = %s", target, ptrTy, value, value.type));
        var instr = new Instruction.Store(block, target, value);
        insert(instr);
        return instr;
    }

    public Instruction.GetElemPtr getElementPtr(Value ptr, Value... indices) {
        var instr = new Instruction.GetElemPtr(block, ptr, indices);
        insert(instr);
        return instr;
    }

    public Instruction.I2F i2f(Value value) {
        Assertions.requires(value.type == Types.Int,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.I2F(block, value);
        insert(instr);
        return instr;
    }

    public Instruction.F2I f2i(Value value) {
        Assertions.requires(value.type == Types.Float,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.F2I(block, value);
        insert(instr);
        return instr;
    }

    public Instruction.BitCastI2F bitCastI2F(Value value) {
        Assertions.requires(value.type == Types.Int,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.BitCastI2F(block, value);
        insert(instr);
        return instr;
    }

    public Instruction.BitCastF2I bitCastF2I(Value value) {
        Assertions.requires(value.type == Types.Float,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        var instr = new Instruction.BitCastF2I(block, value);
        insert(instr);
        return instr;
    }

    public Instruction add(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IAdd(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FAdd(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    public Instruction sub(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.ISub(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FSub(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    public Instruction mul(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IMul(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FMul(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    public Instruction div(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IDiv(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FDiv(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    public Instruction mod(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IMod(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FMod(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    public Instruction neg(Value lhs) {
        var lType = lhs.type;
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.INeg(block, lhs);
            case Type.Float _ -> new Instruction.FNeg(block, lhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    public Instruction shl(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Shl(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction shr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Shr(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction ashr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.AShr(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction and(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.And(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction or(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Or(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction not(Value lhs) {
        var lType = lhs.type;
        Assertions.requires(lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lType));
        var instr = new Instruction.Not(block, lhs);
        insert(instr);
        return instr;
    }

    public Instruction xor(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.Xor(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    // equal
    public Instruction eq(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IEq(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FEq(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    // not equal
    public Instruction ne(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.INe(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FNe(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    // greater than
    public Instruction gt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IGt(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FGt(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    // less than
    public Instruction lt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.ILt(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FLt(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    // greater or equal
    public Instruction ge(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.IGe(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FGe(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    // less or equal
    public Instruction le(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        Instruction instr = switch (lType) {
            case Type.Int _ -> new Instruction.ILe(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FLe(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
        insert(instr);
        return instr;
    }

    // intrinsics

    public Instruction fsqrt(Value lhs) {
        Assertions.requires(lhs.type == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lhs.type));
        var instr = new Instruction.FSqrt(block, lhs);
        insert(instr);
        return instr;
    }

    public Instruction fabs(Value lhs) {
        Assertions.requires(lhs.type == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lhs.type));
        var instr = new Instruction.FAbs(block, lhs);
        insert(instr);
        return instr;
    }

    public Instruction fmin(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.FMin(block, lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction fmax(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        var instr = new Instruction.FMax(block, lhs, rhs);
        insert(instr);
        return instr;
    }
}
