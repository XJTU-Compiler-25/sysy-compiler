package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.mir.node.Instruction.Dummy;
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

    private int getNewIndex() {
        return block.getFunction().incTempValueCounter();
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
        return new Dummy(block, getNewIndex(), type, values);
    }

    public Instruction.Jmp jmp(BasicBlock bb) {
        var instr = new Instruction.Jmp(block, bb);
        block.setTerminator(instr);
        return instr;
    }

    public Instruction.ILi ili(int i) {
        var instr = new Instruction.ILi(block, getNewIndex(), i);
        return instr;
    }

    public Instruction.FLi fli(float i) {
        var instr = new Instruction.FLi(block, getNewIndex(), i);
        return instr;
    }

    public Instruction.IMv imv(Value dst, Value src) {
        Assertions.requires(src.type.equals(Types.Int) && src.type.equals(dst.type));
        var instr = new Instruction.IMv(block, dst, src);
        return instr;
    }
    
    public Instruction.FMv fmv(Value dst, Value src) {
        Assertions.requires(src.type.equals(Types.Float) && src.type.equals(dst.type));
        var instr = new Instruction.FMv(block, dst, src);
        return instr;
    }

    public Instruction.I2F i2f(Value lhs) {
        Assertions.requires(lhs.type.equals(Types.Int));
        var instr = new Instruction.I2F(block, getNewIndex(), lhs);
        return instr;
    }

    public Instruction.IAdd iadd(Value lhs, Value rhs) {
        var instr = new Instruction.IAdd(block, getNewIndex(), lhs, rhs);
        return instr;
    }

    public Instruction.IMul imul(Value lhs, Value rhs) {
        var instr = new Instruction.IMul(block, getNewIndex(), lhs, rhs);
        return instr;
    }

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

    public Instruction.Imm addi(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in addi");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.ADDI, lhs, imm);
    }

    public Instruction.Imm andi(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in andi");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.ANDI, lhs, imm);
    }

    public Instruction.Imm ori(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in ori");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.ORI, lhs, imm);
    }

    public Instruction.Imm xori(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in xori");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.XORI, lhs, imm);
    }

    public Instruction.Imm slli(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(0 <= imm && imm < 64, "Immediate incompatible in slli");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.SLLI, lhs, imm);
    }

    public Instruction.Imm srli(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(0 <= imm && imm < 64, "Immediate incompatible in srli");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.SRLI, lhs, imm);
    }

    public Instruction.Imm srai(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(0 <= imm && imm < 64, "Immediate incompatible in srai");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.SRAI, lhs, imm);
    }

    public Instruction.Imm slti(Value lhs, int imm) {
        Assertions.requires(lhs.type.equals(Types.Int));
        Assertions.requires(0 <= imm && imm < 64, "Immediate incompatible in slti");
        return new Instruction.Imm(block, getNewIndex(), Instruction.Imm.Op.SLTI, lhs, imm);
    }
}
