package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public final class LIRInstrHelper {
    private BasicBlock block;

    public LIRInstrHelper() {}

    public LIRInstrHelper(BasicBlock block) {
        changeBlock(block);
    }

    public void changeBlock(BasicBlock block) {
        this.block = block;
    }

    public BasicBlock getBlock() {
        return block;
    }

    public boolean hasTerminator() {
        return block.terminator != null;
    }

    public Dummy dummyUse(Value... values) {
        return new Dummy(block, values);
    }

    public Dummy dummyDef(Type type, Value... values) {
        return new Dummy(block, type, values);
    }

    public Instruction.Jmp jmp(BasicBlock bb) {
        var instr = new Instruction.Jmp(block, bb);
        return instr;
    }

    public Instruction.BEq beq(Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.BEq(block, lhs, rhs, trueTarget, falseTarget);
        return instr;
    }

    public Instruction.BNe bne(Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.BNe(block, lhs, rhs, trueTarget, falseTarget);
        return instr;
    }

    public Instruction.BLe ble(Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.BLe(block, lhs, rhs, trueTarget, falseTarget);
        return instr;
    }

    public Instruction.BLt blt(Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.BLt(block, lhs, rhs, trueTarget, falseTarget);
        return instr;
    }

    public Instruction.BGt bgt(Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.BGt(block, lhs, rhs, trueTarget, falseTarget);
        return instr;
    }

    public Instruction.BGe bge(Value lhs, Value rhs, BasicBlock trueTarget, BasicBlock falseTarget) {
        var instr = new Instruction.BGe(block, lhs, rhs, trueTarget, falseTarget);
        return instr;
    }

    public Instruction.ILi ili(int i) {
        var instr = new Instruction.ILi(block, i);
        return instr;
    }

    public Instruction.FLi fli(float i) {
        var instr = new Instruction.FLi(block, i);
        return instr;
    }

    public Instruction.IMv imv(Value dst, Value src) {
        //Assertions.requires(src.type.equals(Types.Int) && src.type.equals(dst.type));
        var instr = new Instruction.IMv(block, dst, src);
        return instr;
    }
    
    public Instruction.FMv fmv(Value dst, Value src) {
        //Assertions.requires(src.type.equals(Types.Float) && src.type.equals(dst.type));
        var instr = new Instruction.FMv(block, dst, src);
        return instr;
    }

    public Instruction.ICpy icpy(Value src) {
        Assertions.requires(!src.type.equals(Types.Float));
        var instr = new Instruction.ICpy(block, src);
        return instr;
    }
    
    public Instruction.FCpy fcpy(Value src) {
        Assertions.requires(src.type.equals(Types.Float));
        var instr = new Instruction.FCpy(block, src);
        return instr;
    }

    public Instruction.BitCastI2F i2f(Value lhs) {
        Assertions.requires(lhs.type.equals(Types.Int));
        var instr = new Instruction.BitCastI2F(block, lhs);
        return instr;
    }

    public Instruction.Ret ret(Value value) {
        var instr = new Instruction.Ret(block, value);
        return instr;
    }

    public Instruction.RetV ret() {
        var instr = new Instruction.RetV(block);
        return instr;
    }

    public Instruction.Addi addi(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in addi");
        return new Instruction.Addi(block, lhs, imm);
    }

    public Instruction.Andi andi(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in andi");
        return new Instruction.Andi(block, lhs, imm);
    }

    public Instruction.Ori ori(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in ori");
        return new Instruction.Ori(block, lhs, imm);
    }

    public Instruction.Xori xori(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in xori");
        return new Instruction.Xori(block, lhs, imm);
    }

    public Instruction.Slli slli(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(0 <= imm && imm < 32, "Immediate incompatible in slli");
        return new Instruction.Slli(block, lhs, imm);
    }

    public Instruction.Srli srli(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(0 <= imm && imm < 32, "Immediate incompatible in srli");
        return new Instruction.Srli(block, lhs, imm);
    }

    public Instruction.Srai srai(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(0 <= imm && imm < 32, "Immediate incompatible in srai");
        return new Instruction.Srai(block, lhs, imm);
    }

    public Instruction.Slti slti(Value lhs, int imm) {
        Assertions.requires(!lhs.type.equals(Types.Float), "lhs type incompatible: %s".formatted(lhs.type));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in slti");
        return new Instruction.Slti(block, lhs, imm);
    }

    public Instruction.GetElemPtr getElemPtr(Value basePtr, Value indice) {
        return new Instruction.GetElemPtr(block, basePtr, new Value[]{indice});
    }
}
