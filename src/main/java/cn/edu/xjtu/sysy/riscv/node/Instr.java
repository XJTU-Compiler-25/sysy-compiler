package cn.edu.xjtu.sysy.riscv.node;

import java.util.Arrays;
import java.util.stream.Stream;

import cn.edu.xjtu.sysy.riscv.Label;
import cn.edu.xjtu.sysy.util.Assertions;

public sealed interface Instr {

    public abstract Stream<Register> getDef();
    public abstract Stream<Register> getUse();
    private static Stream<Register> genStream(Register... registers) {
        return Arrays.stream(registers);
    } 

    record Reg(Op op, Register.Int rd, Register.Int rs1, Register.Int rs2)
            implements Instr {
        public enum Op {
            ADD("add"),
            ADDW("addw"),
            SUB("sub"),
            SUBW("subw"),
            AND("and"),
            OR("or"),
            XOR("xor"),
            SLL("sll"),
            SLLW("sllw"),
            SRL("srl"),
            SRLW("srlw"),
            SRA("sra"),
            SRAW("sraw"),
            SLT("slt"),
            SLTU("sltu"),
            MUL("mul"),
            MULW("mulw"),
            MULH("mulh"),
            MULHU("mulhu"),
            MULHSU("mulhsu"),
            DIV("div"),
            DIVU("divu"),
            DIVW("divw"),
            DIVUW("divuw"),
            REM("rem"),
            REMU("remu"),
            REMW("remw"),
            REMUW("remuw");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, %s", op, rd, rs1, rs2);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2);
        }

    }

    record RegZ(Op op, Register.Int rd, Register.Int rs1)
            implements Instr {
        public enum Op {
            MV("mv"),
            SEQZ("seqz"),
            SNEZ("snez"),
            SLTZ("sltz"),
            SGTZ("sgtz"),
            NEG("neg"),
            NEGW("negw");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s", op, rd, rs1);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }
    }

    record FUnary(Op op, Register.Float rd, Register.Float rs1)
            implements Instr {
        public enum Op {
            FMV("fmv.s"),
            FNEG("fneg.s"),
            FABS("fabs.s"),
            FSQRT("fsqrt.s");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s", op, rd, rs1);
        }
    }

    record Fclass(Register.Int rd, Register.Float rs) implements Instr {
        @Override
        public String toString() {
            return String.format("fclass.s %s, %s", rd, rs);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record FBinary(Op op, Register.Float rd, Register.Float rs1, Register.Float rs2)
            implements Instr {
        public enum Op {
            FADD("fadd.s"),
            FSUB("fsub.s"),
            FMUL("fmul.s"),
            FDIV("fdiv.s"),
            FMIN("fmin.s"),
            FMAX("fmax.s"),
            FSGNJ("fsgnj.s"),
            FSGNJN("fsgnjn.s"),
            FSGNJX("fsgnjx.s");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, %s", op, rd, rs1, rs2);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2);
        }
    }

    record FComp(Op op, Register.Int rd, Register.Float rs1, Register.Float rs2)
            implements Instr {
        public enum Op {
            FEQ("feq.s"),
            FLT("flt.s"),
            FLE("fle.s");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, %s", op, rd, rs1, rs2);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2);
        }
    }

    record FMulAdd(Op op, Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3)
            implements Instr {
        public enum Op {
            FMADD("fmadd.s"),
            FMSUB("fmsub.s"),
            FNMSUB("fnmsub.s"),
            FNMADD("fnmadd.s");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, %s, %s", op, rd, rs1, rs2, rs3);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2, rs3);
        }
    }

    record FloatCvt(Op op, Register.Int rd, Register.Float rs)
            implements Instr {
        public enum Op {
            FCVT_W_S("fcvt.w.s"),
            FCVT_L_S("fcvt.l.s"),
            FCVT_LU_S("fcvt.lu.s"),
            FCVT_WU_S("fcvt.wu.s");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, rtz", op, rd, rs);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record FloatIntMv(Register.Int rd, Register.Float rs)
            implements Instr {

        @Override
        public String toString() {
            return String.format("fmv.x.w %s, %s", rd, rs);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record IntCvt(Op op, Register.Float rd, Register.Int rs)
            implements Instr {
        public enum Op {
            FCVT_S_W("fcvt.s.w"),
            FCVT_S_L("fcvt.s.l"),
            FCVT_S_LU("fcvt.s.lu"),
            FCVT_S_WU("fcvt.s.wu");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s", op, rd, rs);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record IntFloatMv(Register.Float rd, Register.Int rs)
            implements Instr {

        @Override
        public String toString() {
            return String.format("fmv.w.x %s, %s", rd, rs);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record Imm(Op op, Register.Int rd, Register.Int rs1, int imm)
            implements Instr {
        public Imm {
            switch (op) {
                case SLLI, SRLI, SRAI -> {
                    Assertions.requires(0 <= imm && imm < 64, "Immediate incompatible in %s".formatted(op));
                }
                case SLLIW, SRLIW, SRAIW -> {
                    Assertions.requires(0 <= imm && imm < 32, "Immediate incompatible in %s".formatted(op));
                }
                default -> {
                    Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in %s".formatted(op));
                }
            }
        }

        public enum Op {
            ADDI("addi"),
            ADDIW("addiw"),
            ANDI("andi"),
            ORI("ori"),
            XORI("xori"),
            SLLI("slli"),
            SRLI("srli"),
            SRAI("srai"),
            SLLIW("slliw"),
            SRLIW("srliw"),
            SRAIW("sraiw"),
            SLTI("slti"),
            SLTIU("sltiu");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, %d", op, rd, rs1, imm);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }
    }

    record Load(Op op, Register.Int rd, Register.Int rs1, int imm)
            implements Instr {
        public Load {
            Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in %s".formatted(op));
        }

        public enum Op {
            LB("lb"),
            LBU("lbu"),
            LH("lh"),
            LHU("lhu"),
            LW("lw"),
            LWU("lwu"),
            LD("ld");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %d(%s)", op, rd, imm, rs1);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }
    }

    record Flw(Register.Float rd, Register.Int rs1, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("flw %s, %d(%s)", rd, imm, rs1);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }
    }

    record Store(Op op, Register.Int rs2, Register.Int rs1, int imm)
            implements Instr {
        public Store {
            Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in %s".formatted(op));
        }

        public enum Op {
            SB("sb"),
            SH("sh"),
            SW("sw"),
            SD("sd");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %d(%s)", op, rs2, imm, rs1);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2);
        }
    }

    record Fsw(Register.Float rs2, Register.Int rs1, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("fsw %s, %d(%s)", rs2, imm, rs1);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2);
        }
    }

    record SwGlobal(Register.Int rs, Label label, Register.Int tmp) implements Instr {
        @Override
        public String toString() {
            return String.format("sw %s, %s, %s", rs, label, tmp);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record FswGlobal(Register.Float rs, Label label, Register.Int tmp) implements Instr {
        @Override
        public String toString() {
            return String.format("fsw %s, %s, %s", rs, label, tmp);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record Branch(Op op, Register.Int rs1, Register.Int rs2, Label label)
            implements Instr {
        public enum Op {
            BEQ("beq"),
            BGE("bge"),
            BGEU("bgeu"),
            BLT("blt"),
            BLTU("bltu"),
            BNE("bne"),
            BGTU("bgtu"),
            BLE("ble"),
            BLEU("bleu"),
            BGT("bgt");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s, %s", op, rs1, rs2, label);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1, rs2);
        }
    }

    record BranchZ(Op op, Register.Int rs1, Label label)
            implements Instr {
        public enum Op {
            BEQZ("beqz"),
            BGEZ("bgez"),
            BGTZ("bgtz"),
            BLTZ("bltz"),
            BLEZ("blez"),
            BNEZ("bnez");

            private final String op;

            Op(String op) {
                this.op = op;
            }

            @Override
            public String toString() {
                return op;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %s, %s", op, rs1, label);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }
    }

    record Jal(Register.Int rd, Label label) implements Instr {

        @Override
        public String toString() {
            return String.format("jal %s %s", rd, label);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record Call(Label label) implements Instr {

        @Override
        public String toString() {
            return String.format("call %s", label);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(PhysicalRegisters.RA);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record Jalr(Register.Int rd, Register.Int rs1, int imm) implements Instr {

        public Jalr {
            Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in JALR");
        }

        @Override
        public String toString() {
            return String.format("jalr %s %d(%s)", rd, imm, rs1);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs1);
        }
    }

    record Auipc(Register.Int rd, int immu) implements Instr {
        @Override
        public String toString() {
            return String.format("auipc %s, %d", rd, immu);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record Lui(Register.Int rd, int immu) implements Instr {
        @Override
        public String toString() {
            return String.format("lui %s, %d", rd, immu);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record Ecall() implements Instr {
        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record Li(Register.Int rd, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("li %s, %d", rd, imm);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record La(Register.Int rd, Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("la %s, %s", rd, label);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream(rd);
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record J(Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("j %s", label);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream();
        }
    }

    record Jr(Register.Int rs) implements Instr {
        @Override
        public String toString() {
            return String.format("jr %s", rs);
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(rs);
        }
    }

    record Ret() implements Instr {
        @Override
        public String toString() {
            return "ret";
        }

        @Override
        public Stream<Register> getDef() {
            return genStream();
        }

        @Override
        public Stream<Register> getUse() {
            return genStream(PhysicalRegisters.RA);
        }
    } 
}
