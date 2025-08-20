package cn.edu.xjtu.sysy.riscv.node;

import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.riscv.ValueUtils;
import cn.edu.xjtu.sysy.riscv.node.Instr.Auipc;
import cn.edu.xjtu.sysy.riscv.node.Instr.Branch;
import cn.edu.xjtu.sysy.riscv.node.Instr.BranchZ;
import cn.edu.xjtu.sysy.riscv.node.Instr.Call;
import cn.edu.xjtu.sysy.riscv.node.Instr.Ecall;
import cn.edu.xjtu.sysy.riscv.node.Instr.FBinary;
import cn.edu.xjtu.sysy.riscv.node.Instr.FComp;
import cn.edu.xjtu.sysy.riscv.node.Instr.FMulAdd;
import cn.edu.xjtu.sysy.riscv.node.Instr.FUnary;
import cn.edu.xjtu.sysy.riscv.node.Instr.Fclass;
import cn.edu.xjtu.sysy.riscv.node.Instr.FloatCvt;
import cn.edu.xjtu.sysy.riscv.node.Instr.FloatIntMv;
import cn.edu.xjtu.sysy.riscv.node.Instr.Flw;
import cn.edu.xjtu.sysy.riscv.node.Instr.Fsw;
import cn.edu.xjtu.sysy.riscv.node.Instr.Imm;
import cn.edu.xjtu.sysy.riscv.node.Instr.IntCvt;
import cn.edu.xjtu.sysy.riscv.node.Instr.IntFloatMv;
import cn.edu.xjtu.sysy.riscv.node.Instr.J;
import cn.edu.xjtu.sysy.riscv.node.Instr.Jal;
import cn.edu.xjtu.sysy.riscv.node.Instr.Jalr;
import cn.edu.xjtu.sysy.riscv.node.Instr.Jr;
import cn.edu.xjtu.sysy.riscv.node.Instr.La;
import cn.edu.xjtu.sysy.riscv.node.Instr.Li;
import cn.edu.xjtu.sysy.riscv.node.Instr.Load;
import cn.edu.xjtu.sysy.riscv.node.Instr.Lui;
import cn.edu.xjtu.sysy.riscv.node.Instr.Reg;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.ADD;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.ADDW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.AND;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.DIV;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.DIVU;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.DIVUW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.DIVW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.MUL;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.MULH;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.MULHSU;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.MULHU;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.MULW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.OR;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.REM;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.REMU;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.REMUW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.REMW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SLL;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SLLW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SLT;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SLTU;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SRA;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SRAW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SRL;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SRLW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SUB;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.SUBW;
import static cn.edu.xjtu.sysy.riscv.node.Instr.Reg.Op.XOR;
import cn.edu.xjtu.sysy.riscv.node.Instr.RegZ;
import cn.edu.xjtu.sysy.riscv.node.Instr.Ret;
import cn.edu.xjtu.sysy.riscv.node.Instr.Store;
import cn.edu.xjtu.sysy.symbol.Types;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

@SuppressWarnings({"unused"})
public final class AsmWriter {

    private MachineBasicBlock instrs;

    public void changeBlock(MachineBasicBlock block) {
        instrs = block;
    }

    public MachineBasicBlock getBlock() {
        return instrs;
    }

    public Register.Int getFP(boolean isArgument) {
        return isArgument ? Register.Int.SP : Register.Int.FP;
    }

    private AsmWriter reg(Reg.Op op, Register.Int rd, Register.Int rs1, Register.Int rs2) {
        instrs.add(new Reg(op, rd, rs1, rs2));
        return this;
    }

    public AsmWriter add(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(ADD, rd, rs1, rs2);
    }

    public AsmWriter addw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(ADDW, rd, rs1, rs2);
    }

    public AsmWriter sub(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SUB, rd, rs1, rs2);
    }

    public AsmWriter subw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SUBW, rd, rs1, rs2);
    }

    public AsmWriter and(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(AND, rd, rs1, rs2);
    }

    public AsmWriter or(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(OR, rd, rs1, rs2);
    }

    public AsmWriter xor(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(XOR, rd, rs1, rs2);
    }

    public AsmWriter sll(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLL, rd, rs1, rs2);
    }

    public AsmWriter sllw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLLW, rd, rs1, rs2);
    }

    public AsmWriter srl(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SRL, rd, rs1, rs2);
    }

    public AsmWriter srlw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SRLW, rd, rs1, rs2);
    }

    public AsmWriter sra(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SRA, rd, rs1, rs2);
    }

    public AsmWriter sraw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SRAW, rd, rs1, rs2);
    }

    public AsmWriter slt(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLT, rd, rs1, rs2);
    }

    public AsmWriter sltu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLTU, rd, rs1, rs2);
    }

    public AsmWriter mul(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(MUL, rd, rs1, rs2);
    }

    public AsmWriter mulw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULW, rd, rs1, rs2);
    }

    public AsmWriter mulh(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULH, rd, rs1, rs2);
    }

    public AsmWriter mulhu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULHU, rd, rs1, rs2);
    }

    public AsmWriter mulhsu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULHSU, rd, rs1, rs2);
    }

    public AsmWriter div(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(DIV, rd, rs1, rs2);
    }

    public AsmWriter divu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(DIVU, rd, rs1, rs2);
    }

    public AsmWriter divw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(DIVW, rd, rs1, rs2);
    }

    public AsmWriter divuw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(DIVUW, rd, rs1, rs2);
    }

    public AsmWriter rem(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(REM, rd, rs1, rs2);
    }

    public AsmWriter remu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(REMU, rd, rs1, rs2);
    }

    public AsmWriter remw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(REMW, rd, rs1, rs2);
    }

    public AsmWriter remuw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(REMUW, rd, rs1, rs2);
    }

    private AsmWriter regz(RegZ.Op op, Register.Int rd, Register.Int rs1) {
        instrs.add(new RegZ(op, rd, rs1));
        return this;
    }

    public AsmWriter mv(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.MV, rd, rs1);
    }

    public AsmWriter seqz(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SEQZ, rd, rs1);
    }

    public AsmWriter snez(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SNEZ, rd, rs1);
    }

    public AsmWriter sltz(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SLTZ, rd, rs1);
    }

    public AsmWriter sgtz(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SGTZ, rd, rs1);
    }

    public AsmWriter neg(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.NEG, rd, rs1);
    }

    public AsmWriter negw(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.NEGW, rd, rs1);
    }

    private AsmWriter funary(FUnary.Op op, Register.Float rd, Register.Float rs1) {
        instrs.add(new FUnary(op, rd, rs1));
        return this;
    }

    public AsmWriter fmv(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FMV, rd, rs1);
    }

    public AsmWriter fneg(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FNEG, rd, rs1);
    }

    public AsmWriter fabs(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FABS, rd, rs1);
    }

    public AsmWriter fsqrt(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FSQRT, rd, rs1);
    }

    public AsmWriter fclass(Register.Int rd, Register.Float rs1) {
        instrs.add(new Fclass(rd, rs1));
        return this;
    }

    private AsmWriter fbinary(
            FBinary.Op op, Register.Float rd, Register.Float rs1, Register.Float rs2) {
        instrs.add(new FBinary(op, rd, rs1, rs2));
        return this;
    }

    public AsmWriter fadd(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FADD, rd, rs1, rs2);
    }

    public AsmWriter fsub(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSUB, rd, rs1, rs2);
    }

    public AsmWriter fmul(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMUL, rd, rs1, rs2);
    }

    public AsmWriter fdiv(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FDIV, rd, rs1, rs2);
    }

    public AsmWriter fmin(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMIN, rd, rs1, rs2);
    }

    public AsmWriter fmax(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMAX, rd, rs1, rs2);
    }

    public AsmWriter fsgnj(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJ, rd, rs1, rs2);
    }

    public AsmWriter fsgnjn(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJN, rd, rs1, rs2);
    }

    public AsmWriter fsgnjx(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJX, rd, rs1, rs2);
    }

    private AsmWriter fcomp(
            FComp.Op op, Register.Int rd, Register.Float rs1, Register.Float rs2) {
        instrs.add(new FComp(op, rd, rs1, rs2));
        return this;
    }

    public AsmWriter feq(Register.Int rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FEQ, rd, rs1, rs2);
    }

    public AsmWriter flt(Register.Int rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FLT, rd, rs1, rs2);
    }

    public AsmWriter fle(Register.Int rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FLE, rd, rs1, rs2);
    }

    private AsmWriter fma(
            FMulAdd.Op op,
            Register.Float rd,
            Register.Float rs1,
            Register.Float rs2,
            Register.Float rs3) {
        instrs.add(new FMulAdd(op, rd, rs1, rs2, rs3));
        return this;
    }

    public AsmWriter fmadd(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FMADD, rd, rs1, rs2, rs3);
    }

    public AsmWriter fnmadd(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FNMADD, rd, rs1, rs2, rs3);
    }

    public AsmWriter fmsub(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FMSUB, rd, rs1, rs2, rs3);
    }

    public AsmWriter fnmsub(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FNMSUB, rd, rs1, rs2, rs3);
    }

    private AsmWriter fcvt(FloatCvt.Op op, Register.Int rd, Register.Float rs) {
        instrs.add(new FloatCvt(op, rd, rs));
        return this;
    }

    public AsmWriter fcvt_w_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_W_S, rd, rs);
    }

    public AsmWriter fcvt_l_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_L_S, rd, rs);
    }

    public AsmWriter fcvt_wu_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_WU_S, rd, rs);
    }

    public AsmWriter fcvt_lu_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_LU_S, rd, rs);
    }

    public AsmWriter fmv_x_w(Register.Int rd, Register.Float rs) {
        instrs.add(new Instr.FloatIntMv(rd, rs));
        return this;
    }

    private AsmWriter icvt(IntCvt.Op op, Register.Float rd, Register.Int rs) {
        instrs.add(new IntCvt(op, rd, rs));
        return this;
    }

    public AsmWriter fcvt_s_w(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_W, rd, rs);
    }

    public AsmWriter fcvt_s_l(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_L, rd, rs);
    }

    public AsmWriter fcvt_s_wu(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_WU, rd, rs);
    }

    public AsmWriter fcvt_s_lu(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_LU, rd, rs);
    }

    public AsmWriter fmv_w_x(Register.Float rd, Register.Int rs) {
        instrs.add(new Instr.IntFloatMv(rd, rs));
        return this;
    }

    private AsmWriter imm(Imm.Op op, Register.Int rd, Register.Int rs, int imm) {
        instrs.add(new Imm(op, rd, rs, imm));
        return this;
    }

    public AsmWriter addi(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ADDI, rd, rs, imm);
    }

    public AsmWriter addi(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            return li(tmp, imm).add(rd, rs, tmp);
        }
        return imm(Imm.Op.ADDI, rd, rs, imm);
    }

    public AsmWriter addiw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ADDIW, rd, rs, imm);
    }

    public AsmWriter andi(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ANDI, rd, rs, imm);
    }

    public AsmWriter ori(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ORI, rd, rs, imm);
    }

    public AsmWriter xori(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.XORI, rd, rs, imm);
    }

    public AsmWriter slli(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLLI, rd, rs, imm);
    }

    public AsmWriter srli(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRLI, rd, rs, imm);
    }

    public AsmWriter srai(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRAI, rd, rs, imm);
    }

    public AsmWriter slliw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLLIW, rd, rs, imm);
    }

    public AsmWriter srliw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRLIW, rd, rs, imm);
    }

    public AsmWriter sraiw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRAIW, rd, rs, imm);
    }

    public AsmWriter slti(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLTI, rd, rs, imm);
    }

    public AsmWriter sltiu(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLTIU, rd, rs, imm);
    }

    private AsmWriter load(Load.Op op, Register.Int rd, Register.Int rs, int imm) {
        instrs.add(new Load(op, rd, rs, imm));
        return this;
    }

    private AsmWriter load(
            Load.Op op, Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            li(tmp, imm).add(tmp, rs, tmp);
            instrs.add(new Load(op, rd, tmp, 0));
        } else {
            instrs.add(new Load(op, rd, rs, imm));
        }
        return this;
    }

    public AsmWriter lb(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LB, rd, rs, imm);
    }

    public AsmWriter lbu(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LBU, rd, rs, imm);
    }

    public AsmWriter lh(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LH, rd, rs, imm);
    }

    public AsmWriter lhu(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LHU, rd, rs, imm);
    }

    public AsmWriter lw(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LW, rd, rs, imm);
    }

    public AsmWriter lwu(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LWU, rd, rs, imm);
    }

    public AsmWriter ld(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LD, rd, rs, imm);
    }

    public AsmWriter lb(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LB, rd, rs, imm, tmp);
    }

    public AsmWriter lbu(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LBU, rd, rs, imm, tmp);
    }

    public AsmWriter lh(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LH, rd, rs, imm, tmp);
    }

    public AsmWriter lhu(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LHU, rd, rs, imm, tmp);
    }

    public AsmWriter lw(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LW, rd, rs, imm, tmp);
    }

    public AsmWriter lwu(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LWU, rd, rs, imm, tmp);
    }

    public AsmWriter ld(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LD, rd, rs, imm, tmp);
    }

    public AsmWriter flw(Register.Float rd, Register.Int rs, int imm) {
        instrs.add(new Flw(rd, rs, imm));
        return this;
    }

    public AsmWriter flw(Register.Float rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            return li(tmp, imm).add(tmp, rs, tmp).flw(rd, tmp, 0);
        } else {
            return flw(rd, rs, imm);
        }
    }

    private AsmWriter regw(Reg.Op op, Value dst, Register.Int rs1, Register.Int rs2) {
        var position = dst.position;
        switch(position) {
            case Register.Int rd -> { 
                instrs.add(new Reg(op, rd, rs1, rs2));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new Reg(op, ValueUtils.spillIntReg, rs1, rs2));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    private AsmWriter reg(Reg.Op op, Value dst, Register.Int rs1, Register.Int rs2) {
        var position = dst.position;
        switch(position) {
            case Register.Int rd -> { 
                instrs.add(new Reg(op, rd, rs1, rs2));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new Reg(op, ValueUtils.spillIntReg, rs1, rs2));
                sd(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter add(Value dst, Register.Int rs1, Register.Int rs2) {
        return reg(ADD, dst, rs1, rs2);   
    }

    public AsmWriter addw(Value dst, Register.Int rs1, Register.Int rs2) {
        return regw(ADDW, dst, rs1, rs2);
    }

    public AsmWriter sub(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(SUB, rd, rs1, rs2);
    }

    public AsmWriter subw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(SUBW, rd, rs1, rs2);
    }

    public AsmWriter and(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(AND, rd, rs1, rs2);
    }

    public AsmWriter or(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(OR, rd, rs1, rs2);
    }

    public AsmWriter xor(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(XOR, rd, rs1, rs2);
    }

    public AsmWriter sll(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLL, rd, rs1, rs2);
    }

    public AsmWriter sllw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(SLLW, rd, rs1, rs2);
    }

    public AsmWriter srl(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(SRL, rd, rs1, rs2);
    }

    public AsmWriter srlw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(SRLW, rd, rs1, rs2);
    }

    public AsmWriter sra(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(SRA, rd, rs1, rs2);
    }

    public AsmWriter sraw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(SRAW, rd, rs1, rs2);
    }

    public AsmWriter slt(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLT, rd, rs1, rs2);
    }

    public AsmWriter sltu(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(SLTU, rd, rs1, rs2);
    }

    public AsmWriter mul(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(MUL, rd, rs1, rs2);
    }

    public AsmWriter mulw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(MULW, rd, rs1, rs2);
    }

    public AsmWriter mulh(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULH, rd, rs1, rs2);
    }

    public AsmWriter mulhu(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULHU, rd, rs1, rs2);
    }

    public AsmWriter mulhsu(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(MULHSU, rd, rs1, rs2);
    }

    public AsmWriter div(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(DIV, rd, rs1, rs2);
    }

    public AsmWriter divu(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(DIVU, rd, rs1, rs2);
    }

    public AsmWriter divw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(DIVW, rd, rs1, rs2);
    }

    public AsmWriter divuw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(DIVUW, rd, rs1, rs2);
    }

    public AsmWriter rem(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(REM, rd, rs1, rs2);
    }

    public AsmWriter remu(Value rd, Register.Int rs1, Register.Int rs2) {
        return reg(REMU, rd, rs1, rs2);
    }

    public AsmWriter remw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(REMW, rd, rs1, rs2);
    }

    public AsmWriter remuw(Value rd, Register.Int rs1, Register.Int rs2) {
        return regw(REMUW, rd, rs1, rs2);
    }

    private AsmWriter regz(RegZ.Op op, Value dst, Register.Int rs1) {
        var position = dst.position;
        switch(position) {
            case Register.Int rd -> { 
                instrs.add(new RegZ(op, rd, rs1));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new RegZ(op, ValueUtils.spillIntReg, rs1));
                if (dst.type == Types.Int)
                    sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
                else
                    sd(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter mv(Value rd, Register.Int rs1) {
        if (rd.position instanceof Register.Int dst && dst == rs1) return this;
        return regz(RegZ.Op.MV, rd, rs1);
    }

    public AsmWriter seqz(Value rd, Register.Int rs1) {
        return regz(RegZ.Op.SEQZ, rd, rs1);
    }

    public AsmWriter snez(Value rd, Register.Int rs1) {
        return regz(RegZ.Op.SNEZ, rd, rs1);
    }

    public AsmWriter sltz(Value rd, Register.Int rs1) {
        return regz(RegZ.Op.SLTZ, rd, rs1);
    }

    public AsmWriter sgtz(Value rd, Register.Int rs1) {
        return regz(RegZ.Op.SGTZ, rd, rs1);
    }

    public AsmWriter neg(Value rd, Register.Int rs1) {
        return regz(RegZ.Op.NEG, rd, rs1);
    }

    public AsmWriter negw(Value rd, Register.Int rs1) {
        return regz(RegZ.Op.NEGW, rd, rs1);
    }

    private AsmWriter funary(FUnary.Op op, Value rd, Register.Float rs1) {
        var position = rd.position;
        switch(position) {
            case Register.Float dst -> { 
                instrs.add(new FUnary(op, dst, rs1));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new FUnary(op, Register.Float.FT0, rs1));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter fmv(Value rd, Register.Float rs1) {
        if (rd.position instanceof Register.Float dst && dst == rs1) return this;
        return funary(FUnary.Op.FMV, rd, rs1);
    }

    public AsmWriter fneg(Value rd, Register.Float rs1) {
        return funary(FUnary.Op.FNEG, rd, rs1);
    }

    public AsmWriter fabs(Value rd, Register.Float rs1) {
        return funary(FUnary.Op.FABS, rd, rs1);
    }

    public AsmWriter fsqrt(Value rd, Register.Float rs1) {
        return funary(FUnary.Op.FSQRT, rd, rs1);
    }

    public AsmWriter fclass(Value rd, Register.Float rs1) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                instrs.add(new Fclass(dst, rs1));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new Fclass(ValueUtils.spillIntReg, rs1));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    private AsmWriter fbinary(
            FBinary.Op op, Value rd, Register.Float rs1, Register.Float rs2) {
        var position = rd.position;
        switch(position) {
            case Register.Float dst -> { 
                instrs.add(new FBinary(op, dst, rs1, rs2));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new FBinary(op, Register.Float.FT0, rs1, rs2));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter fadd(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FADD, rd, rs1, rs2);
    }

    public AsmWriter fsub(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSUB, rd, rs1, rs2);
    }

    public AsmWriter fmul(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMUL, rd, rs1, rs2);
    }

    public AsmWriter fdiv(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FDIV, rd, rs1, rs2);
    }

    public AsmWriter fmin(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMIN, rd, rs1, rs2);
    }

    public AsmWriter fmax(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMAX, rd, rs1, rs2);
    }

    public AsmWriter fsgnj(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJ, rd, rs1, rs2);
    }

    public AsmWriter fsgnjn(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJN, rd, rs1, rs2);
    }

    public AsmWriter fsgnjx(Value rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJX, rd, rs1, rs2);
    }

    private AsmWriter fcomp(
            FComp.Op op, Value rd, Register.Float rs1, Register.Float rs2) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                instrs.add(new FComp(op, dst, rs1, rs2));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new FComp(op, ValueUtils.spillIntReg, rs1, rs2));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter feq(Value rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FEQ, rd, rs1, rs2);
    }

    public AsmWriter flt(Value rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FLT, rd, rs1, rs2);
    }

    public AsmWriter fle(Value rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FLE, rd, rs1, rs2);
    }

    private AsmWriter fma(
            FMulAdd.Op op,
            Value rd,
            Register.Float rs1,
            Register.Float rs2,
            Register.Float rs3) {
        var position = rd.position;
        switch(position) {
            case Register.Float dst -> { 
                instrs.add(new FMulAdd(op, dst, rs1, rs2, rs3));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new FMulAdd(op, Register.Float.FT0, rs1, rs2, rs3));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter fmadd(
            Value rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FMADD, rd, rs1, rs2, rs3);
    }

    public AsmWriter fnmadd(
            Value rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FNMADD, rd, rs1, rs2, rs3);
    }

    public AsmWriter fmsub(
            Value rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FMSUB, rd, rs1, rs2, rs3);
    }

    public AsmWriter fnmsub(
            Value rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FNMSUB, rd, rs1, rs2, rs3);
    }

    private AsmWriter fcvt(FloatCvt.Op op, Value rd, Register.Float rs) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                instrs.add(new FloatCvt(op, dst, rs));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new FloatCvt(op, ValueUtils.spillIntReg, rs));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter fcvt_w_s(Value rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_W_S, rd, rs);
    }

    public AsmWriter fcvt_l_s(Value rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_L_S, rd, rs);
    }

    public AsmWriter fcvt_wu_s(Value rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_WU_S, rd, rs);
    }

    public AsmWriter fcvt_lu_s(Value rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_LU_S, rd, rs);
    }

    public AsmWriter fmv_x_w(Value rd, Register.Float rs) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                instrs.add(new FloatIntMv(dst, rs));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new FloatIntMv(ValueUtils.spillIntReg, rs));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    private AsmWriter icvt(IntCvt.Op op, Value rd, Register.Int rs) {
        var position = rd.position;
        switch(position) {
            case Register.Float dst -> { 
                instrs.add(new IntCvt(op, dst, rs));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new IntCvt(op, Register.Float.FT0, rs));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter fcvt_s_w(Value rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_W, rd, rs);
    }

    public AsmWriter fcvt_s_l(Value rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_L, rd, rs);
    }

    public AsmWriter fcvt_s_wu(Value rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_WU, rd, rs);
    }

    public AsmWriter fcvt_s_lu(Value rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_LU, rd, rs);
    }

    public AsmWriter fmv_w_x(Value rd, Register.Int rs) {
        var position = rd.position;
        switch(position) {
            case Register.Float dst -> { 
                instrs.add(new IntFloatMv(dst, rs));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new IntFloatMv(Register.Float.FT0, rs));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    private AsmWriter imm(Imm.Op op, Value rd, Register.Int rs, int imm) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                instrs.add(new Imm(op, dst, rs, imm));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new Imm(op, ValueUtils.spillIntReg, rs, imm));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter addi(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ADDI, rd, rs, imm);
    }

    public AsmWriter addiw(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ADDIW, rd, rs, imm);
    }

    public AsmWriter andi(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ANDI, rd, rs, imm);
    }

    public AsmWriter ori(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ORI, rd, rs, imm);
    }

    public AsmWriter xori(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.XORI, rd, rs, imm);
    }

    public AsmWriter slli(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLLI, rd, rs, imm);
    }

    public AsmWriter srli(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRLI, rd, rs, imm);
    }

    public AsmWriter srai(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRAI, rd, rs, imm);
    }

    public AsmWriter slliw(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLLIW, rd, rs, imm);
    }

    public AsmWriter srliw(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRLIW, rd, rs, imm);
    }

    public AsmWriter sraiw(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRAIW, rd, rs, imm);
    }

    public AsmWriter slti(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLTI, rd, rs, imm);
    }

    public AsmWriter sltiu(Value rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLTIU, rd, rs, imm);
    }

    private AsmWriter load(Load.Op op, Value rd, Register.Int rs, int imm) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                instrs.add(new Load(op, dst, rs, imm));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new Load(op, ValueUtils.spillIntReg, rs, imm));
                sw(ValueUtils.spillIntReg, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }


    public AsmWriter lb(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LB, rd, rs, imm);
    }

    public AsmWriter lbu(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LBU, rd, rs, imm);
    }

    public AsmWriter lh(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LH, rd, rs, imm);
    }

    public AsmWriter lhu(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LHU, rd, rs, imm);
    }

    public AsmWriter lw(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LW, rd, rs, imm);
    }

    public AsmWriter lwu(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LWU, rd, rs, imm);
    }

    public AsmWriter ld(Value rd, Register.Int rs, int imm) {
        return load(Load.Op.LD, rd, rs, imm);
    }
/* 
    public AsmWriter lb(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LB, rd, rs, imm, tmp);
    }

    public AsmWriter lbu(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LBU, rd, rs, imm, tmp);
    }

    public AsmWriter lh(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LH, rd, rs, imm, tmp);
    }

    public AsmWriter lhu(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LHU, rd, rs, imm, tmp);
    }

    public AsmWriter lw(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LW, rd, rs, imm, tmp);
    }

    public AsmWriter lwu(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LWU, rd, rs, imm, tmp);
    }

    public AsmWriter ld(Value rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LD, rd, rs, imm, tmp);
    }
*/
    public AsmWriter flw(Value rd, Register.Int rs, int imm) {
        var position = rd.position;
        switch(position) {
            case Register.Float dst -> { 
                instrs.add(new Flw(dst, rs, imm));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                instrs.add(new Flw(Register.Float.FT0, rs, imm));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    private AsmWriter store(Store.Op op, Register.Int rd, Register.Int rs, int imm) {
        instrs.add(new Store(op, rd, rs, imm));
        return this;
    }

    private AsmWriter store(
            Store.Op op, Register.Int src, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            instrs.add(new Li(tmp, imm));
            instrs.add(new Reg(ADD, tmp, rs, tmp));
            instrs.add(new Store(op, src, tmp, 0));
        } else {
            instrs.add(new Store(op, src, rs, imm));
        }
        return this;
    }

    public AsmWriter sb(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SB, rd, rs, imm);
    }

    public AsmWriter sh(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SH, rd, rs, imm);
    }

    public AsmWriter sw(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SW, rd, rs, imm);
    }

    public AsmWriter sb(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SB, rd, rs, imm, tmp);
    }

    public AsmWriter sh(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SH, rd, rs, imm, tmp);
    }

    public AsmWriter sw(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SW, rd, rs, imm, tmp);
    }

    public AsmWriter sw(Register.Int rd, String label, Register.Int tmp) {
        instrs.add(new Instr.Sw_global(rd, label, tmp));
        return this;
    }

    public AsmWriter sd(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SD, rd, rs, imm);
    }

    public AsmWriter sd(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SD, rd, rs, imm, tmp);
    }

    public AsmWriter fsw(Register.Float rd, Register.Int rs, int imm) {
        instrs.add(new Fsw(rd, rs, imm));
        return this;
    }

    public AsmWriter fsw(Register.Float rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            instrs.add(new Li(tmp, imm));
            instrs.add(new Reg(ADD, tmp, rs, tmp));
            instrs.add(new Fsw(rd, tmp, 0));
        } else {
            instrs.add(new Fsw(rd, rs, imm));
        }
        return this;
    }

    public AsmWriter fsw(Register.Float rd, String label, Register.Int tmp) {
        instrs.add(new Instr.Fsw_global(rd, label, tmp));
        return this;
    }

    private AsmWriter branch(Branch.Op op, Register.Int rs1, Register.Int rs2, String label) {
        instrs.add(new Branch(op, rs1, rs2, label));
        return this;
    }

    public AsmWriter beq(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BEQ, rs1, rs2, label);
    }

    public AsmWriter bge(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BGE, rs1, rs2, label);
    }

    public AsmWriter bgeu(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BGEU, rs1, rs2, label);
    }

    public AsmWriter blt(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BLT, rs1, rs2, label);
    }

    public AsmWriter bltu(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BLTU, rs1, rs2, label);
    }

    public AsmWriter bne(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BNE, rs1, rs2, label);
    }

    public AsmWriter bgtu(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BGTU, rs1, rs2, label);
    }

    public AsmWriter ble(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BLE, rs1, rs2, label);
    }

    public AsmWriter bleu(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BLEU, rs1, rs2, label);
    }

    public AsmWriter bgt(Register.Int rs1, Register.Int rs2, String label) {
        return branch(Branch.Op.BGT, rs1, rs2, label);
    }

    private AsmWriter branchz(BranchZ.Op op, Register.Int rs1, String label) {
        instrs.add(new BranchZ(op, rs1, label));
        return this;
    }

    public AsmWriter beqz(Register.Int rs1, String label) {
        return branchz(BranchZ.Op.BEQZ, rs1, label);
    }

    public AsmWriter bgez(Register.Int rs1, String label) {
        return branchz(BranchZ.Op.BGEZ, rs1, label);
    }

    public AsmWriter bgtz(Register.Int rs1, String label) {
        return branchz(BranchZ.Op.BGTZ, rs1, label);
    }

    public AsmWriter bltz(Register.Int rs1, String label) {
        return branchz(BranchZ.Op.BLTZ, rs1, label);
    }

    public AsmWriter blez(Register.Int rs1, String label) {
        return branchz(BranchZ.Op.BLEZ, rs1, label);
    }

    public AsmWriter bnez(Register.Int rs1, String label) {
        return branchz(BranchZ.Op.BNEZ, rs1, label);
    }

    public AsmWriter jal(Register.Int rd, String label) {
        instrs.add(new Jal(rd, label));
        return this;
    }

    public AsmWriter call(String label) {
        instrs.add(new Call(label));
        return this;
    }

    public AsmWriter auipc(Register.Int rd, int immu) {
        instrs.add(new Auipc(rd, immu));
        return this;
    }

    public AsmWriter lui(Register.Int rd, int immu) {
        instrs.add(new Lui(rd, immu));
        return this;
    }

    public AsmWriter jalr(Register.Int rd, Register.Int rs1, int imm) {
        instrs.add(new Jalr(rd, rs1, imm));
        return this;
    }

    public AsmWriter j(String label) {
        instrs.add(new J(label));
        return this;
    }

    public AsmWriter jr(Register.Int rs) {
        instrs.add(new Jr(rs));
        return this;
    }

    public AsmWriter ret() {
        instrs.add(new Ret());
        return this;
    }

    public AsmWriter ecall() {
        instrs.add(new Ecall());
        return this;
    }

    public AsmWriter la(Register.Int rd, String label) {
        instrs.add(new La(rd, label));
        return this;
    }
    
    public AsmWriter li(Value rd, Number imm) {
        return switch (imm) {
            case Integer i -> li(rd, i.intValue());
            case Float f -> li(rd, f.floatValue());
            default -> unreachable();
        };
    }

    public AsmWriter li(Value rd, int imm) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                li(dst, imm);
            }
            case StackPosition(int offset, boolean isArgument) -> {
                li(ValueUtils.spillIntReg, imm);
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter li(Value rd, float imm) {
        var position = rd.position;
        switch(position) {
            case Register.Int dst -> { 
                li(dst, Float.floatToRawIntBits(imm));
            }
            case StackPosition(int offset, boolean isArgument) -> {
                li(ValueUtils.spillIntReg, Float.floatToRawIntBits(imm));
                fsw(Register.Float.FT0, getFP(isArgument), -offset, ValueUtils.intScratchReg);
            }
            default -> unreachable();
        }
        return this;
    }

    public AsmWriter li(Register.Int rd, Number imm) {
        return switch (imm) {
            case Integer i -> li(rd, i.intValue());
            case Float f -> li(rd, f.floatValue());
            default -> unreachable();
        };
    }

    public AsmWriter li(Register.Int rd, int imm) {
        instrs.add(new Li(rd, imm));
        return this;
    }

    public AsmWriter li(Register.Int rd, float imm) {
        instrs.add(new Li(rd, Float.floatToRawIntBits(imm)));
        return this;
    }

    public AsmWriter setzero(int start, int size, Register.Int tmp) {
        while (size > 0) {
            sw(Register.Int.ZERO, Register.Int.FP, -(start - size + 4), tmp);
            size -= 4;
        }
        return this;
    }
}
