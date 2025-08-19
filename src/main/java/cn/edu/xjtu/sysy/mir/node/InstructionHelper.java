package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.BuiltinFunction;
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
        moveTo(initialPos);
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

    public void moveTo(int insertPosition) {
        this.insertPosition = insertPosition;
    }

    public void moveToBefore(Instruction inst) {
        this.insertPosition = block.instructions.indexOf(inst);
    }

    public void moveToAfter(Instruction inst) {
        this.insertPosition = block.instructions.indexOf(inst) + 1;
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

    public Instruction.Ret insertRet(Value value) {
        var instr = ret(value);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.Ret ret(Value value) {
        return new Instruction.Ret(block, value);
    }

    public Instruction.RetV insertRetV() {
        var instr = retV();
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.RetV retV() {
        return new Instruction.RetV(block);
    }

    public Instruction.Jmp insertJmp(BasicBlock bb) {
        var instr = jmp(bb);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.Jmp jmp(BasicBlock bb) {
        return new Instruction.Jmp(block, bb);
    }

    public Instruction.Br insertBr(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = br(condition, trueTarget, falseTarget);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.Br br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
        return new Instruction.Br(block, condition, trueTarget, falseTarget);
    }

    // common instructions

    public Instruction.Call insertCall(Function func, Value... args) {
        var instr = call(func, args);
        insert(instr);
        return instr;
    }

    public Instruction.Call call(Function func, Value... args) {
        return new Instruction.Call(block, func, args);
    }

    public Instruction.CallExternal insertCallBuiltin(BuiltinFunction function, Value... args) {
        var instr = callBuiltin(function, args);
        insert(instr);
        return instr;
    }

    public Instruction.CallExternal insertCallBuiltin(String name, Value... args) {
        var instr = callBuiltin(name, args);
        insert(instr);
        return instr;
    }

    public Instruction.CallExternal callBuiltin(BuiltinFunction function, Value... args) {
        return new Instruction.CallExternal(block, function, args);
    }

    public Instruction.CallExternal callBuiltin(String name, Value... args) {
        return new Instruction.CallExternal(block, name, args);
    }

    public Instruction.Alloca insertAlloca(Type type) {
        var instr = alloca(type);
        insert(instr);
        return instr;
    }

    public Instruction.Alloca alloca(Type type) {
        return new Instruction.Alloca(block, type);
    }

    public Instruction.Alloca insertAlloca(Type type, boolean zeroInit) {
        var instr = alloca(type, zeroInit);
        insert(instr);
        return instr;
    }

    public Instruction.Alloca alloca(Type type, boolean zeroInit) {
        return new Instruction.Alloca(block, type, zeroInit);
    }

    public Instruction.Load insertLoad(Value from) {
        var instr = load(from);
        insert(instr);
        return instr;
    }

    public Instruction.Load load(Value from) {
        return new Instruction.Load(block, from);
    }

    public Instruction.Store insertStore(Value target, Value value) {
        var instr = store(target, value);
        insert(instr);
        return instr;
    }

    public Instruction.Store store(Value target, Value value) {
        var ptrTy = (Type.Pointer) target.type;
        Assertions.requires(value.type == ptrTy.baseType,
                String.format("invalid type: typeof target(%s) = %s, value(%s) = %s", target, ptrTy, value, value.type));
        return new Instruction.Store(block, target, value);
    }

    public Instruction.GetElemPtr insertGetElementPtr(Value ptr, Value... indices) {
        var instr = getElementPtr(ptr, indices);
        insert(instr);
        return instr;
    }

    public Instruction.GetElemPtr getElementPtr(Value ptr, Value... indices) {
        return new Instruction.GetElemPtr(block, ptr, indices);
    }

    public Instruction.I2F insertI2f(Value value) {
        var instr = i2f(value);
        insert(instr);
        return instr;
    }

    public Instruction.I2F i2f(Value value) {
        Assertions.requires(value.type == Types.Int,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        return new Instruction.I2F(block, value);
    }

    public Instruction.F2I insertF2i(Value value) {
        var instr = f2i(value);
        insert(instr);
        return instr;
    }

    public Instruction.F2I f2i(Value value) {
        Assertions.requires(value.type == Types.Float,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        return new Instruction.F2I(block, value);
    }

    public Instruction.BitCastI2F insertBitCastI2F(Value value) {
        var instr = bitCastI2F(value);
        insert(instr);
        return instr;
    }

    public Instruction.BitCastI2F bitCastI2F(Value value) {
        Assertions.requires(value.type == Types.Int,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        return new Instruction.BitCastI2F(block, value);
    }

    public Instruction.BitCastF2I insertBitCastF2I(Value value) {
        var instr = bitCastF2I(value);
        insert(instr);
        return instr;
    }

    public Instruction.BitCastF2I bitCastF2I(Value value) {
        Assertions.requires(value.type == Types.Float,
                String.format("invalid type: typeof value(%s) = %s", value, value.type));
        return new Instruction.BitCastF2I(block, value);
    }

    public Instruction insertAdd(Value lhs, Value rhs) {
        var instr = add(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction add(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IAdd(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FAdd(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction insertSub(Value lhs, Value rhs) {
        var instr = sub(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction sub(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.ISub(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FSub(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction insertMul(Value lhs, Value rhs) {
        var instr = mul(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction mul(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMul(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FMul(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction insertDiv(Value lhs, Value rhs) {
        var instr = div(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction div(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IDiv(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FDiv(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction insertMod(Value lhs, Value rhs) {
        var instr = mod(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction mod(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IMod(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FMod(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction insertNeg(Value lhs) {
        var instr = neg(lhs);
        insert(instr);
        return instr;
    }

    public Instruction neg(Value lhs) {
        var lType = lhs.type;
        return switch (lType) {
            case Type.Int _ -> new Instruction.INeg(block, lhs);
            case Type.Float _ -> new Instruction.FNeg(block, lhs);
            default -> Assertions.unsupported(lType);
        };
    }

    public Instruction insertShl(Value lhs, Value rhs) {
        var instr = shl(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction shl(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.Shl(block, lhs, rhs);
    }

    public Instruction insertShr(Value lhs, Value rhs) {
        var instr = shr(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction shr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.Shr(block, lhs, rhs);
    }

    public Instruction insertAshr(Value lhs, Value rhs) {
        var instr = ashr(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction ashr(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.AShr(block, lhs, rhs);
    }

    public Instruction insertAnd(Value lhs, Value rhs) {
        var instr = and(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction and(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.And(block, lhs, rhs);
    }

    public Instruction insertOr(Value lhs, Value rhs) {
        var instr = or(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction or(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.Or(block, lhs, rhs);
    }

    public Instruction insertNot(Value lhs) {
        var instr = not(lhs);
        insert(instr);
        return instr;
    }

    public Instruction not(Value lhs) {
        var lType = lhs.type;
        Assertions.requires(lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lType));
        return new Instruction.Not(block, lhs);
    }

    public Instruction insertXor(Value lhs, Value rhs) {
        var instr = xor(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction xor(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Int,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.Xor(block, lhs, rhs);
    }

    // equal
    public Instruction insertEq(Value lhs, Value rhs) {
        var instr = eq(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction eq(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IEq(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FEq(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // not equal
    public Instruction insertNe(Value lhs, Value rhs) {
        var instr = ne(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction ne(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.INe(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FNe(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater than
    public Instruction insertGt(Value lhs, Value rhs) {
        var instr = gt(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction gt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IGt(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FGt(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // less than
    public Instruction insertLt(Value lhs, Value rhs) {
        var instr = lt(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction lt(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.ILt(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FLt(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // greater or equal
    public Instruction insertGe(Value lhs, Value rhs) {
        var instr = ge(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction ge(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.IGe(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FGe(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // less or equal
    public Instruction insertLe(Value lhs, Value rhs) {
        var instr = le(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction le(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return switch (lType) {
            case Type.Int _ -> new Instruction.ILe(block, lhs, rhs);
            case Type.Float _ -> new Instruction.FLe(block, lhs, rhs);
            default -> Assertions.unsupported(lType);
        };
    }

    // intrinsics

    public Instruction insertFsqrt(Value lhs) {
        var instr = fsqrt(lhs);
        insert(instr);
        return instr;
    }

    public Instruction fsqrt(Value lhs) {
        Assertions.requires(lhs.type == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lhs.type));
        return new Instruction.FSqrt(block, lhs);
    }

    public Instruction insertFabs(Value lhs) {
        var instr = fabs(lhs);
        insert(instr);
        return instr;
    }

    public Instruction fabs(Value lhs) {
        Assertions.requires(lhs.type == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s", lhs, lhs.type));
        return new Instruction.FAbs(block, lhs);
    }

    public Instruction insertFmin(Value lhs, Value rhs) {
        var instr = fmin(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction fmin(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.FMin(block, lhs, rhs);
    }

    public Instruction insertFmax(Value lhs, Value rhs) {
        var instr = fmax(lhs, rhs);
        insert(instr);
        return instr;
    }

    public Instruction fmax(Value lhs, Value rhs) {
        var lType = lhs.type;
        Assertions.requires(lType == rhs.type && lType == Types.Float,
                String.format("invalid type: typeof lhs(%s) = %s, typeof rhs(%s) = %s", lhs, lhs.type, rhs, rhs.type));
        return new Instruction.FMax(block, lhs, rhs);
    }

}
