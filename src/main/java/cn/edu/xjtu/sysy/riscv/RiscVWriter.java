package cn.edu.xjtu.sysy.riscv;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

import cn.edu.xjtu.sysy.riscv.Directives.FuncSize;
import cn.edu.xjtu.sysy.riscv.Directives.Word;
import cn.edu.xjtu.sysy.riscv.Directives.Zero;
import cn.edu.xjtu.sysy.riscv.Global.Obj;
import cn.edu.xjtu.sysy.riscv.Instr.Auipc;
import cn.edu.xjtu.sysy.riscv.Instr.Branch;
import cn.edu.xjtu.sysy.riscv.Instr.BranchZ;
import cn.edu.xjtu.sysy.riscv.Instr.Call;
import cn.edu.xjtu.sysy.riscv.Instr.Ecall;
import cn.edu.xjtu.sysy.riscv.Instr.FBinary;
import cn.edu.xjtu.sysy.riscv.Instr.FComp;
import cn.edu.xjtu.sysy.riscv.Instr.FMulAdd;
import cn.edu.xjtu.sysy.riscv.Instr.FUnary;
import cn.edu.xjtu.sysy.riscv.Instr.Fclass;
import cn.edu.xjtu.sysy.riscv.Instr.FloatCvt;
import cn.edu.xjtu.sysy.riscv.Instr.Flw;
import cn.edu.xjtu.sysy.riscv.Instr.Fsw;
import cn.edu.xjtu.sysy.riscv.Instr.Imm;
import cn.edu.xjtu.sysy.riscv.Instr.IntCvt;
import cn.edu.xjtu.sysy.riscv.Instr.J;
import cn.edu.xjtu.sysy.riscv.Instr.Jal;
import cn.edu.xjtu.sysy.riscv.Instr.Jalr;
import cn.edu.xjtu.sysy.riscv.Instr.Jr;
import cn.edu.xjtu.sysy.riscv.Instr.La;
import cn.edu.xjtu.sysy.riscv.Instr.Li;
import cn.edu.xjtu.sysy.riscv.Instr.Li_globl;
import cn.edu.xjtu.sysy.riscv.Instr.Load;
import cn.edu.xjtu.sysy.riscv.Instr.LocalLabel;
import cn.edu.xjtu.sysy.riscv.Instr.Lui;
import cn.edu.xjtu.sysy.riscv.Instr.Reg;
import cn.edu.xjtu.sysy.riscv.Instr.RegZ;
import cn.edu.xjtu.sysy.riscv.Instr.Ret;
import cn.edu.xjtu.sysy.riscv.Instr.Store;
import cn.edu.xjtu.sysy.riscv.Register.Int;
import cn.edu.xjtu.sysy.riscv.Label;
import cn.edu.xjtu.sysy.symbol.Symbol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.xjtu.sysy.symbol.Type;

public class RiscVWriter {

    protected final StringWriter asm = new StringWriter();

    private final PrintWriter out = new PrintWriter(asm);

    private List<Directives> directives = new ArrayList<>();

    private List<Directives> values = new ArrayList<>();

    private List<Instr> instrs = new ArrayList<>();

    private final List<Global.Obj> objDefs = new ArrayList<>();

    private final List<Global.Func> funcDefs = new ArrayList<>();

    @Override
    public String toString() {
        return asm.toString();
    }

    public void emit(String str) {
        out.println(str);
    }

    protected void emit(Label label) {
        emit(String.format("%s:", label));
    }

    protected void emit(Directives dir) {
        emit(String.format("  %s", dir));
    }

    protected void emit(Instr insn) {
        if (insn instanceof LocalLabel label) {
            emit(String.format("%s:", label));
        } else {
            emit(String.format("  %s", insn));
        }
    }

    public void emitAll() {
        for (var obj : objDefs) {
            obj.directives.forEach(this::emit);
            emit(obj.label);
            obj.value.forEach(this::emit);
        }
        for (var func : funcDefs) {
            func.directives.forEach(this::emit);
            emit(func.label);
            func.instrs.forEach(this::emit);
            emit(func.size);
        }
        instrs.forEach(this::emit);
    }

    private RiscVWriter global(Label label) {
        directives.add(new Directives.Global(label));
        return this;
    }

    private RiscVWriter bss() {
        directives.add(new Directives.Bss());
        return this;
    }

    private RiscVWriter data() {
        directives.add(new Directives.Data());
        return this;
    }

    private RiscVWriter text() {
        directives.add(new Directives.Text());
        return this;
    }

    private RiscVWriter align(Type type) {
        int pow = switch (type) {
            case Type.Int _, Type.Float _ -> 2;
            default -> 3;
        };
        directives.add(new Directives.Align(pow));
        return this;
    }

    private RiscVWriter align(int pow) {
        directives.add(new Directives.Align(pow));
        return this;
    }

    private RiscVWriter type(Symbol sym) {
        switch (sym) {
            case Symbol.VarSymbol it ->
                    directives.add(new Directives.Type(it.label, Directives.Type.SymType.Object));
            case Symbol.FuncSymbol it ->
                    directives.add(new Directives.Type(it.label, Directives.Type.SymType.Function));
        }
        return this;
    }

    private RiscVWriter size(Label label, int size) {
        directives.add(new Directives.VarSize(label, size));
        return this;
    }

    public void defVarData(Symbol.VarSymbol sym) {
        this.global(sym.label)
            .data()
            .align(sym.type)
            .type(sym)
            .size(sym.label, sym.type.size);
        objDefs.add(new Obj(sym.label, directives, values));
        directives = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void defVarBss(Symbol.VarSymbol sym) {
        this.global(sym.label)
            .bss()
            .align(sym.type)
            .type(sym)
            .size(sym.label, sym.type.size);
        objDefs.add(new Obj(sym.label, directives, values));
        directives = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void defFunc(Symbol.FuncSymbol sym) {
        this.align(1)
            .global(sym.label)
            .text()
            .type(sym);
        var size = new FuncSize(sym.label);
        funcDefs.add(new Global.Func(sym.label, directives, instrs, size));
        directives = new ArrayList<>();
        instrs = new ArrayList<>();
    }

    public RiscVWriter word(int val) {
        values.add(new Word(val));
        return this;
    }

    public RiscVWriter word(float val) {
        values.add(new Word(Float.floatToRawIntBits(val)));
        return this;
    }

    public RiscVWriter word(Number val) {
        return switch (val) {
            case Integer i -> word(i.intValue());
            case Float f -> word(f.floatValue());
            default -> unreachable();
        };
    }

    public RiscVWriter zero(int size) {
        values.add(new Zero(size));
        return this;
    }

    private RiscVWriter reg(Reg.Op op, Register.Int rd, Register.Int rs1, Register.Int rs2) {
        instrs.add(new Reg(op, rd, rs1, rs2));
        return this;
    }

    public RiscVWriter add(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.ADD, rd, rs1, rs2);
    }

    public RiscVWriter addw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.ADDW, rd, rs1, rs2);
    }

    public RiscVWriter sub(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SUB, rd, rs1, rs2);
    }

    public RiscVWriter subw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SUBW, rd, rs1, rs2);
    }

    public RiscVWriter and(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.AND, rd, rs1, rs2);
    }

    public RiscVWriter or(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.OR, rd, rs1, rs2);
    }

    public RiscVWriter xor(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.XOR, rd, rs1, rs2);
    }

    public RiscVWriter sll(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SLL, rd, rs1, rs2);
    }

    public RiscVWriter sllw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SLLW, rd, rs1, rs2);
    }

    public RiscVWriter srl(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SRL, rd, rs1, rs2);
    }

    public RiscVWriter srlw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SRLW, rd, rs1, rs2);
    }

    public RiscVWriter sra(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SRA, rd, rs1, rs2);
    }

    public RiscVWriter sraw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SRAW, rd, rs1, rs2);
    }

    public RiscVWriter slt(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SLT, rd, rs1, rs2);
    }

    public RiscVWriter sltu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.SLTU, rd, rs1, rs2);
    }

    public RiscVWriter mul(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.MUL, rd, rs1, rs2);
    }

    public RiscVWriter mulw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.MULW, rd, rs1, rs2);
    }

    public RiscVWriter mulh(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.MULH, rd, rs1, rs2);
    }

    public RiscVWriter mulhu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.MULHU, rd, rs1, rs2);
    }

    public RiscVWriter mulhsu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.MULHSU, rd, rs1, rs2);
    }

    public RiscVWriter div(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.DIV, rd, rs1, rs2);
    }

    public RiscVWriter divu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.DIVU, rd, rs1, rs2);
    }

    public RiscVWriter divw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.DIVW, rd, rs1, rs2);
    }

    public RiscVWriter divuw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.DIVUW, rd, rs1, rs2);
    }

    public RiscVWriter rem(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.REM, rd, rs1, rs2);
    }

    public RiscVWriter remu(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.REMU, rd, rs1, rs2);
    }

    public RiscVWriter remw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.REMW, rd, rs1, rs2);
    }

    public RiscVWriter remuw(Register.Int rd, Register.Int rs1, Register.Int rs2) {
        return reg(Reg.Op.REMUW, rd, rs1, rs2);
    }

    private RiscVWriter regz(RegZ.Op op, Register.Int rd, Register.Int rs1) {
        instrs.add(new RegZ(op, rd, rs1));
        return this;
    }

    public RiscVWriter mv(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.MV, rd, rs1);
    }

    public RiscVWriter seqz(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SEQZ, rd, rs1);
    }

    public RiscVWriter snez(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SNEZ, rd, rs1);
    }

    public RiscVWriter sltz(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SLTZ, rd, rs1);
    }

    public RiscVWriter sgtz(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.SGTZ, rd, rs1);
    }

    public RiscVWriter neg(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.NEG, rd, rs1);
    }

    public RiscVWriter negw(Register.Int rd, Register.Int rs1) {
        return regz(RegZ.Op.NEGW, rd, rs1);
    }

    private RiscVWriter funary(FUnary.Op op, Register.Float rd, Register.Float rs1) {
        instrs.add(new FUnary(op, rd, rs1));
        return this;
    }

    public RiscVWriter fmv(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FMV, rd, rs1);
    }

    public RiscVWriter fneg(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FNEG, rd, rs1);
    }

    public RiscVWriter fabs(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FABS, rd, rs1);
    }

    public RiscVWriter fsqrt(Register.Float rd, Register.Float rs1) {
        return funary(FUnary.Op.FSQRT, rd, rs1);
    }

    public RiscVWriter fclass(Register.Int rd, Register.Float rs1) {
        instrs.add(new Fclass(rd, rs1));
        return this;
    }

    private RiscVWriter fbinary(
            FBinary.Op op, Register.Float rd, Register.Float rs1, Register.Float rs2) {
        instrs.add(new FBinary(op, rd, rs1, rs2));
        return this;
    }

    public RiscVWriter fadd(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FADD, rd, rs1, rs2);
    }

    public RiscVWriter fsub(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSUB, rd, rs1, rs2);
    }

    public RiscVWriter fmul(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMUL, rd, rs1, rs2);
    }

    public RiscVWriter fdiv(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FDIV, rd, rs1, rs2);
    }

    public RiscVWriter fmin(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMIN, rd, rs1, rs2);
    }

    public RiscVWriter fmax(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FMAX, rd, rs1, rs2);
    }

    public RiscVWriter fsgnj(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJ, rd, rs1, rs2);
    }

    public RiscVWriter fsgnjn(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJN, rd, rs1, rs2);
    }

    public RiscVWriter fsgnjx(Register.Float rd, Register.Float rs1, Register.Float rs2) {
        return fbinary(FBinary.Op.FSGNJX, rd, rs1, rs2);
    }

    private RiscVWriter fcomp(
            FComp.Op op, Register.Int rd, Register.Float rs1, Register.Float rs2) {
        instrs.add(new FComp(op, rd, rs1, rs2));
        return this;
    }

    public RiscVWriter feq(Register.Int rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FEQ, rd, rs1, rs2);
    }

    public RiscVWriter flt(Register.Int rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FLT, rd, rs1, rs2);
    }

    public RiscVWriter fle(Register.Int rd, Register.Float rs1, Register.Float rs2) {
        return fcomp(FComp.Op.FLE, rd, rs1, rs2);
    }

    private RiscVWriter fma(
            FMulAdd.Op op,
            Register.Float rd,
            Register.Float rs1,
            Register.Float rs2,
            Register.Float rs3) {
        instrs.add(new FMulAdd(op, rd, rs1, rs2, rs3));
        return this;
    }

    public RiscVWriter fmadd(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FMADD, rd, rs1, rs2, rs3);
    }

    public RiscVWriter fnmadd(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FNMADD, rd, rs1, rs2, rs3);
    }

    public RiscVWriter fmsub(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FMSUB, rd, rs1, rs2, rs3);
    }

    public RiscVWriter fnmsub(
            Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3) {
        return fma(FMulAdd.Op.FNMSUB, rd, rs1, rs2, rs3);
    }

    private RiscVWriter fcvt(FloatCvt.Op op, Register.Int rd, Register.Float rs) {
        instrs.add(new FloatCvt(op, rd, rs));
        return this;
    }

    public RiscVWriter fcvt_w_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_W_S, rd, rs);
    }

    public RiscVWriter fcvt_l_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_L_S, rd, rs);
    }

    public RiscVWriter fcvt_wu_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_WU_S, rd, rs);
    }

    public RiscVWriter fcvt_lu_s(Register.Int rd, Register.Float rs) {
        return fcvt(FloatCvt.Op.FCVT_LU_S, rd, rs);
    }

    public RiscVWriter fmv_x_w(Register.Int rd, Register.Float rs) {
        instrs.add(new Instr.FloatIntMv(rd, rs));
        return this;
    }

    private RiscVWriter icvt(IntCvt.Op op, Register.Float rd, Register.Int rs) {
        instrs.add(new IntCvt(op, rd, rs));
        return this;
    }

    public RiscVWriter fcvt_s_w(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_W, rd, rs);
    }

    public RiscVWriter fcvt_s_l(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_L, rd, rs);
    }

    public RiscVWriter fcvt_s_wu(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_WU, rd, rs);
    }

    public RiscVWriter fcvt_s_lu(Register.Float rd, Register.Int rs) {
        return icvt(IntCvt.Op.FCVT_S_LU, rd, rs);
    }

    public RiscVWriter fmv_w_x(Register.Float rd, Register.Int rs) {
        instrs.add(new Instr.IntFloatMv(rd, rs));
        return this;
    }

    private RiscVWriter imm(Imm.Op op, Register.Int rd, Register.Int rs, int imm) {
        instrs.add(new Imm(op, rd, rs, imm));
        return this;
    }

    public RiscVWriter addi(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ADDI, rd, rs, imm);
    }

    public RiscVWriter addi(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            return li(tmp, imm).add(rd, rs, tmp);
        }
        return imm(Imm.Op.ADDI, rd, rs, imm);
    }

    public RiscVWriter addiw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ADDIW, rd, rs, imm);
    }

    public RiscVWriter andi(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ANDI, rd, rs, imm);
    }

    public RiscVWriter ori(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.ORI, rd, rs, imm);
    }

    public RiscVWriter xori(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.XORI, rd, rs, imm);
    }

    public RiscVWriter slli(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLLI, rd, rs, imm);
    }

    public RiscVWriter srli(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRLI, rd, rs, imm);
    }

    public RiscVWriter srai(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRAI, rd, rs, imm);
    }

    public RiscVWriter slliw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLLIW, rd, rs, imm);
    }

    public RiscVWriter srliw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRLIW, rd, rs, imm);
    }

    public RiscVWriter sraiw(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SRAIW, rd, rs, imm);
    }

    public RiscVWriter slti(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLTI, rd, rs, imm);
    }

    public RiscVWriter sltiu(Register.Int rd, Register.Int rs, int imm) {
        return imm(Imm.Op.SLTIU, rd, rs, imm);
    }

    private RiscVWriter load(Load.Op op, Register.Int rd, Register.Int rs, int imm) {
        instrs.add(new Load(op, rd, rs, imm));
        return this;
    }

    private RiscVWriter load(
            Load.Op op, Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            li(tmp, imm).add(tmp, rs, tmp);
            instrs.add(new Load(op, rd, tmp, 0));
        } else {
            instrs.add(new Load(op, rd, rs, imm));
        }
        return this;
    }

    public RiscVWriter lb(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LB, rd, rs, imm);
    }

    public RiscVWriter lbu(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LBU, rd, rs, imm);
    }

    public RiscVWriter lh(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LH, rd, rs, imm);
    }

    public RiscVWriter lhu(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LHU, rd, rs, imm);
    }

    public RiscVWriter lw(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LW, rd, rs, imm);
    }

    public RiscVWriter lwu(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LWU, rd, rs, imm);
    }

    public RiscVWriter ld(Register.Int rd, Register.Int rs, int imm) {
        return load(Load.Op.LD, rd, rs, imm);
    }

    public RiscVWriter lb(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LB, rd, rs, imm, tmp);
    }

    public RiscVWriter lbu(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LBU, rd, rs, imm, tmp);
    }

    public RiscVWriter lh(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LH, rd, rs, imm, tmp);
    }

    public RiscVWriter lhu(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LHU, rd, rs, imm, tmp);
    }

    public RiscVWriter lw(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LW, rd, rs, imm, tmp);
    }

    public RiscVWriter lwu(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LWU, rd, rs, imm, tmp);
    }

    public RiscVWriter ld(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return load(Load.Op.LD, rd, rs, imm, tmp);
    }

    public RiscVWriter flw(Register.Float rd, Register.Int rs, int imm) {
        instrs.add(new Flw(rd, rs, imm));
        return this;
    }

    public RiscVWriter flw(Register.Float rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            return li(tmp, imm).add(tmp, rs, tmp).flw(rd, tmp, 0);
        } else {
            return flw(rd, rs, imm);
        }
    }

    private RiscVWriter store(Store.Op op, Register.Int rd, Register.Int rs, int imm) {
        instrs.add(new Store(op, rd, rs, imm));
        return this;
    }

    private RiscVWriter store(
            Store.Op op, Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            li(tmp, imm).add(tmp, rs, tmp);
            instrs.add(new Store(op, rd, tmp, 0));
        } else {
            instrs.add(new Store(op, rd, rs, imm));
        }
        return this;
    }

    public RiscVWriter sb(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SB, rd, rs, imm);
    }

    public RiscVWriter sh(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SH, rd, rs, imm);
    }

    public RiscVWriter sw(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SW, rd, rs, imm);
    }

    public RiscVWriter sb(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SB, rd, rs, imm, tmp);
    }

    public RiscVWriter sh(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SH, rd, rs, imm, tmp);
    }

    public RiscVWriter sw(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SW, rd, rs, imm, tmp);
    }

    public RiscVWriter sw(Register.Int rd, Label label, Register.Int tmp) {
        instrs.add(new Instr.Sw_global(rd, label, tmp));
        return this;
    }

    public RiscVWriter sd(Register.Int rd, Register.Int rs, int imm) {
        return store(Store.Op.SD, rd, rs, imm);
    }

    public RiscVWriter sd(Register.Int rd, Register.Int rs, int imm, Register.Int tmp) {
        return store(Store.Op.SD, rd, rs, imm, tmp);
    }

    public RiscVWriter fsw(Register.Float rd, Register.Int rs, int imm) {
        instrs.add(new Fsw(rd, rs, imm));
        return this;
    }

    public RiscVWriter fsw(Register.Float rd, Register.Int rs, int imm, Register.Int tmp) {
        if (imm < -2048 || imm >= 2048) {
            return li(tmp, imm).add(tmp, rs, tmp).fsw(rd, tmp, 0);
        } else {
            return fsw(rd, rs, imm);
        }
    }

    public RiscVWriter fsw(Register.Float rd, Label label, Register.Int tmp) {
        instrs.add(new Instr.Fsw_global(rd, label, tmp));
        return this;
    }

    private RiscVWriter branch(Branch.Op op, Register.Int rs1, Register.Int rs2, Label label) {
        instrs.add(new Branch(op, rs1, rs2, label));
        return this;
    }

    public RiscVWriter beq(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BEQ, rs1, rs2, label);
    }

    public RiscVWriter bge(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BGE, rs1, rs2, label);
    }

    public RiscVWriter bgeu(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BGEU, rs1, rs2, label);
    }

    public RiscVWriter blt(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BLT, rs1, rs2, label);
    }

    public RiscVWriter bltu(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BLTU, rs1, rs2, label);
    }

    public RiscVWriter bne(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BNE, rs1, rs2, label);
    }

    public RiscVWriter bgtu(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BGTU, rs1, rs2, label);
    }

    public RiscVWriter ble(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BLE, rs1, rs2, label);
    }

    public RiscVWriter bleu(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BLEU, rs1, rs2, label);
    }

    public RiscVWriter bgt(Register.Int rs1, Register.Int rs2, Label label) {
        return branch(Branch.Op.BGT, rs1, rs2, label);
    }

    private RiscVWriter branchz(BranchZ.Op op, Register.Int rs1, Label label) {
        instrs.add(new BranchZ(op, rs1, label));
        return this;
    }

    public RiscVWriter beqz(Register.Int rs1, Label label) {
        return branchz(BranchZ.Op.BEQZ, rs1, label);
    }

    public RiscVWriter bgez(Register.Int rs1, Label label) {
        return branchz(BranchZ.Op.BGEZ, rs1, label);
    }

    public RiscVWriter bgtz(Register.Int rs1, Label label) {
        return branchz(BranchZ.Op.BGTZ, rs1, label);
    }

    public RiscVWriter bltz(Register.Int rs1, Label label) {
        return branchz(BranchZ.Op.BLTZ, rs1, label);
    }

    public RiscVWriter blez(Register.Int rs1, Label label) {
        return branchz(BranchZ.Op.BLEZ, rs1, label);
    }

    public RiscVWriter bnez(Register.Int rs1, Label label) {
        return branchz(BranchZ.Op.BNEZ, rs1, label);
    }

    public RiscVWriter jal(Register.Int rd, Label label) {
        instrs.add(new Jal(rd, label));
        return this;
    }

    public RiscVWriter call(Label label) {
        instrs.add(new Call(label));
        return this;
    }

    public RiscVWriter label(Label label) {
        instrs.add(new LocalLabel(label));
        return this;
    }

    public RiscVWriter auipc(Register.Int rd, int immu) {
        instrs.add(new Auipc(rd, immu));
        return this;
    }

    public RiscVWriter lui(Register.Int rd, int immu) {
        instrs.add(new Lui(rd, immu));
        return this;
    }

    public RiscVWriter jalr(Register.Int rd, Register.Int rs1, int imm) {
        instrs.add(new Jalr(rd, rs1, imm));
        return this;
    }

    public RiscVWriter j(Label label) {
        instrs.add(new J(label));
        return this;
    }

    public RiscVWriter jr(Register.Int rs) {
        instrs.add(new Jr(rs));
        return this;
    }

    public RiscVWriter ret() {
        instrs.add(new Ret());
        return this;
    }

    public RiscVWriter ecall() {
        instrs.add(new Ecall());
        return this;
    }

    public RiscVWriter la(Register.Int rd, Label label) {
        instrs.add(new La(rd, label));
        return this;
    }

    public RiscVWriter li(Register.Int rd, Number imm) {
        return switch (imm) {
            case Integer i -> li(rd, i.intValue());
            case Float f -> li(rd, f.floatValue());
            default -> unreachable();
        };
    }

    public RiscVWriter li(Register.Int rd, int imm) {
        instrs.add(new Li(rd, imm));
        return this;
    }

    public RiscVWriter li(Register.Int rd, float imm) {
        instrs.add(new Li(rd, Float.floatToRawIntBits(imm)));
        return this;
    }

    public RiscVWriter li(Register.Int rd, Label imm) {
        instrs.add(new Li_globl(rd, imm));
        return this;
    }

    public RiscVWriter setzero(int start, int size, Register.Int tmp) {
        while (size > 0) {
            sw(Int.ZERO, Int.FP, -(start - size + 4), tmp);
            size -= 4;
        }
        return this;
    }
}
