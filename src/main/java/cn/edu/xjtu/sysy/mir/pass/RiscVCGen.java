package cn.edu.xjtu.sysy.mir.pass;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.riscv.Label;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.Register.Int;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.riscv.node.AsmWriter;
import cn.edu.xjtu.sysy.riscv.Register.Float;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

public class RiscVCGen extends ModulePass<Void> {

    public RiscVCGen() {
        asm = new AsmWriter();
    }

    public AsmWriter asm;
    
    private Register.Int useInt(Value val) {
        switch (val) {
            case ImmediateValue.IntConst it -> {
                asm.li(Register.Int.T0, it.value);
                return Register.Int.T0;
            }
            case ImmediateValue.FloatConst it -> {
                asm.li(Register.Int.T0, it.value);
                return Register.Int.T0;
            }
            case GlobalVar it -> {
                asm.la(Register.Int.T0, genLabel(it.name));
                return Register.Int.T0;
            }
            default -> {
                return switch(val.position) {
                    case Register.Int r -> r;
                    case StackPosition(int offset) -> {
                        Assertions.requires(!val.type.equals(Types.Float));
                        if (val.type == Types.Int) asm.lw(Int.T0, Int.FP, offset);
                        else asm.ld(Int.T0, Int.FP, offset);
                        yield Int.T0;
                    }
                    default -> unreachable();
                };
            }
        }
    }

    private Register.Float useFloat(Value val) {
        return switch(val.position) {
            case Register.Float r -> r;
            case StackPosition(int offset) -> {
                if (val.type == Types.Float) {
                    asm.flw(Float.FT0, Int.FP, offset);
                    yield Float.FT0;
                }
                yield unreachable();
            }
            default -> unreachable();
        };
    }

    @Override
    public void visit(Function func) {
        asm.label(new Label(func.shortName()));
        func.blocks.forEach(this::visit);
    }

    private Label genLabel(String name) {
        return new Label(name);
    }

    @Override
    public void visit(BasicBlock block) {
        asm.label(genLabel(block.shortName()));
        block.instructions.forEach(this::visit);
    }

    @Override
    public void visit(Instruction instr) {
        switch(instr) {
            case Instruction.BEq it -> {
                asm.beq(useInt(it.lhs.value), useInt(it.rhs.value),
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.BNe it -> {
                asm.bne(useInt(it.lhs.value), useInt(it.rhs.value),
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.BGe it -> {
                asm.bge(useInt(it.lhs.value), useInt(it.rhs.value),
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.BGt it -> {
                asm.bgt(useInt(it.lhs.value), useInt(it.rhs.value),
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.BLe it -> {
                asm.ble(useInt(it.lhs.value), useInt(it.rhs.value),
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.BLt it -> {
                asm.blt(useInt(it.lhs.value), useInt(it.rhs.value),
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.Br it -> {
                asm.bnez(useInt(it.getCondition()), 
                            genLabel(it.getTrueTarget().shortName()));
            }
            case Instruction.Jmp it -> {
                asm.j(genLabel(it.getTarget().shortName()));
            }
            case Instruction.Ret it -> {
                if (it.getRetVal().type.equals(Types.Float)) {
                    asm.fmv(Register.Float.FA0, useFloat(it.getRetVal()));
                } else {
                    asm.mv(Register.Int.A0, useInt(it.getRetVal()));
                }
                asm.ret();
            }
            case Instruction.RetV it -> {
                asm.ret();
            }
            case Instruction.Call it -> {
                var func = it.getCallee();
                asm.call(genLabel(func.shortName()));
                var type = func.funcType.returnType;
                if (type.equals(Types.Void)) return;
                if (type.equals(Types.Float)) {
                    asm.fmv(it, Register.Float.FA0);
                } else {
                    asm.mv(it, Register.Int.A0);
                }
            }
            case Instruction.CallExternal it -> {
                var func = it.function;
                asm.call(genLabel(func.linkName));
                var type = func.symbol.funcType.returnType;
                if (type.equals(Types.Void)) return;
                if (type.equals(Types.Float)) {
                    asm.fmv(it, Register.Float.FA0);
                } else {
                    asm.mv(it, Register.Int.A0);
                }
            }
            case Instruction.Alloca it -> {
                var size = Types.sizeOf(it.allocatedType);
                asm.addi(it, Register.Int.FP, -size);
            }
            case Instruction.Load it -> {
                asm.lw(it, useInt(it.getAddress()), 0);
            }
            case Instruction.Store it -> {
                asm.sw(useInt(it.getStoreVal()), useInt(it.getAddress()), 0);
            }
            case Instruction.GetElemPtr it -> {
                // TODO
            }
            case Instruction.I2F it -> {
                asm.fcvt_s_w(it, useInt(it.getOperand()));
            }
            case Instruction.F2I it -> {
                asm.fcvt_w_s(it, useFloat(it.getOperand()));
            }
            case Instruction.BitCastI2F it -> {
                asm.fmv_w_x(it, useInt(it.getOperand()));
            }
            case Instruction.BitCastF2I it -> {
                asm.fmv_x_w(it, useFloat(it.getOperand()));
            }
            case Instruction.IAdd it -> {
                asm.addw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.ISub it -> {
                asm.subw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.IMul it -> {
                asm.mulw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.IDiv it -> {
                asm.divw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.IMod it -> {
                asm.remw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.INeg it -> {
                asm.negw(it, useInt(it.getOperand()));
            }
            case Instruction.FAdd it -> {
                asm.fadd(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FSub it -> {
                asm.fsub(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FMul it -> {
                asm.fmul(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FDiv it -> {
                asm.fdiv(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FMod it -> {
                //TODO
            }
            case Instruction.FNeg it -> {
                asm.fneg(it, useFloat(it.getOperand()));
            }
            case Instruction.Shl it -> {
                asm.sllw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.Shr it -> {
                asm.srlw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.AShr it -> {
                asm.sraw(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.And it -> {
                asm.and(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.Or it -> {
                asm.or(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.Xor it -> {
                asm.xor(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.Not it -> {
                asm.seqz(it, useInt(it.getOperand()));
            }
            case Instruction.IEq it -> {
                asm .xor(it, useInt(it.lhs.value), useInt(it.rhs.value))
                    .seqz(it, useInt(it));
            }
            case Instruction.INe it -> {
                asm .xor(it, useInt(it.lhs.value), useInt(it.rhs.value))
                    .snez(it, useInt(it));
            }
            case Instruction.IGt it -> {
                asm.slt(it, useInt(it.rhs.value), useInt(it.lhs.value));
            }
            case Instruction.ILt it -> {
                asm.slt(it, useInt(it.lhs.value), useInt(it.rhs.value));
            }
            case Instruction.IGe it -> {
                asm .slt(it, useInt(it.lhs.value), useInt(it.rhs.value))
                    .snez(it, useInt(it));
            }
            case Instruction.ILe it -> {
                asm .slt(it, useInt(it.rhs.value), useInt(it.lhs.value))
                    .snez(it, useInt(it));
            }
            case Instruction.FEq it -> {
                asm.feq(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FNe it -> {
                asm .feq(it, useFloat(it.lhs.value), useFloat(it.rhs.value))
                    .snez(it, useInt(it));
            }
            case Instruction.FGt it -> {
                asm .flt(it, useFloat(it.rhs.value), useFloat(it.lhs.value));
            }
            case Instruction.FLt it -> {
                asm .flt(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FGe it -> {
                asm .fle(it, useFloat(it.rhs.value), useFloat(it.lhs.value));
            }
            case Instruction.FLe it -> {
                asm .fle(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FSqrt it -> {
                asm.fsqrt(it, useFloat(it.getOperand()));
            }
            case Instruction.FAbs it -> {
                asm.fabs(it, useFloat(it.getOperand()));
            }
            case Instruction.FMin it -> {
                asm.fmin(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.FMax it -> {
                asm.fmax(it, useFloat(it.lhs.value), useFloat(it.rhs.value));
            }
            case Instruction.ILi it -> {
                asm.li(it, it.imm);
            }
            case Instruction.FLi it -> {
                asm.li(it, it.imm);
            }
            case Instruction.IMv it -> {
                asm.mv(it.dst, useInt(it.src.value));
            }
            case Instruction.FMv it -> {
                asm.fmv(it.dst, useFloat(it.src.value));
            }
            case Instruction.ICpy it -> {
                asm.mv(it, useInt(it.src.value));
            }
            case Instruction.FCpy it -> {
                asm.fmv(it, useFloat(it.src.value));
            }
            case Instruction.FMulAdd it -> {
                // TODO
            }
            case Instruction.Dummy it -> {
                // DO NOTHING
            }
            case Instruction.Addi it -> {
                asm.addi(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Andi it -> {
                asm.andi(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Ori it -> {
                asm.ori(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Xori it -> {
                asm.xori(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Slli it -> {
                asm.slli(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Srli it -> {
                asm.srli(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Srai it -> {
                asm.srai(it, useInt(it.lhs.value), it.imm);
            }
            case Instruction.Slti it -> {
                asm.slti(it, useInt(it.lhs.value), it.imm);
            }
        }
    }
}
