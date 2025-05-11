package cn.edu.xjtu.sysy.riscv;

import cn.edu.xjtu.sysy.util.Assertions;

public sealed interface Instr {
    public record Reg(Op op, Register.Int rd, Register.Int rs1, Register.Int rs2)
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

            protected final String op;

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
    }

    public record RegZ(Op op, Register.Int rd, Register.Int rs1)
            implements Instr {
        public enum Op {
            MV("mv"),
            SEQZ("seqz"),
            SNEZ("snez"),
            SLTZ("sltz"),
            SGTZ("sgtz"),
            NEG("neg"),
            NEGW("negw");

            protected final String op;

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
    }

    public record FUnary(Op op, Register.Float rd, Register.Float rs1)
            implements Instr {
        public enum Op {
            FMV("fmv.s"),
            FNEG("fneg.s"),
            FABS("fabs.s"),
            FSQRT("fsqrt.s");

            protected final String op;

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
    }

    public record Fclass(Register.Int rd, Register.Float rs) implements Instr {
        @Override
        public String toString() {
            return String.format("fclass.s %s, %s", rd, rs);
        }
    }

    public record FBinary(Op op, Register.Float rd, Register.Float rs1, Register.Float rs2)
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

            protected final String op;

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
    }

    public record FComp(Op op, Register.Int rd, Register.Float rs1, Register.Float rs2)
            implements Instr {
        public enum Op {
            FEQ("feq.s"),
            FLT("flt.s"),
            FLE("fle.s");

            protected final String op;

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
    }

    public record FMulAdd(Op op, Register.Float rd, Register.Float rs1, Register.Float rs2, Register.Float rs3)
            implements Instr {
        public enum Op {
            FMADD("fmadd.s"),
            FMSUB("fmsub.s"),
            FNMSUB("fnmsub.s"),
            FNMADD("fnmadd.s");

            protected final String op;

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
    }

    public record FloatCvt(Op op, Register.Int rd, Register.Float rs)
            implements Instr {
        public enum Op {
            FCVT_W_S("fcvt.w.s"),
            FCVT_L_S("fcvt.l.s"),
            FCVT_LU_S("fcvt.lu.s"),
            FCVT_WU_S("fcvt.wu.s");

            protected final String op;

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
    }

    public record FloatIntMv(Register.Int rd, Register.Float rs)
            implements Instr {

        @Override
        public String toString() {
            return String.format("fmv.x.w %s, %s", rd, rs);
        }
    }

    public record IntCvt(Op op, Register.Float rd, Register.Int rs)
            implements Instr {
        public enum Op {
            FCVT_S_W("fcvt.s.w"),
            FCVT_S_L("fcvt.s.l"),
            FCVT_S_LU("fcvt.s.lu"),
            FCVT_S_WU("fcvt.s.wu");

            protected final String op;

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
    }

    public record IntFloatMv(Register.Float rd, Register.Int rs)
            implements Instr {

        @Override
        public String toString() {
            return String.format("fmv.w.x %s, %s", rd, rs);
        }
    }

    public record Imm(Op op, Register.Int rd, Register.Int rs1, int imm)
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

            protected final String op;

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
    }

    public record Load(Op op, Register.Int rd, Register.Int rs1, int imm)
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

            protected final String op;

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
    }

    public record Flw(Register.Float rd, Register.Int rs1, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("flw %s, %d(%s)", rd, imm, rs1);
        }
    }

    public record Store(Op op, Register.Int rs2, Register.Int rs1, int imm)
            implements Instr {
        public Store {
            Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in %s".formatted(op));
        }

        public enum Op {
            SB("sb"),
            SH("sh"),
            SW("sw"),
            SD("sd");

            protected final String op;

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
    }

    public record Fsw(Register.Float rd, Register.Int rs1, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("fsw %s, %d(%s)", rd, imm, rs1);
        }
    }

    public record Sw_global(Register.Int rd, Label label, Register.Int tmp) implements Instr {
        @Override
        public String toString() {
            return String.format("sw %s, %s, %s", rd, label, tmp);
        }
    }

    public record Fsw_global(Register.Float rd, Label label, Register.Int tmp) implements Instr {
        @Override
        public String toString() {
            return String.format("fsw %s, %s, %s", rd, label, tmp);
        }
    }

    public record Branch(Op op, Register.Int rs1, Register.Int rs2, Label label)
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

            protected final String op;

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
    }

    public record BranchZ(Op op, Register.Int rs1, Label label)
            implements Instr {
        public enum Op {
            BEQZ("beqz"),
            BGEZ("bgez"),
            BGTZ("bgtz"),
            BLTZ("bltz"),
            BLEZ("blez"),
            BNEZ("bnez");

            protected final String op;

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
    }

    public record Jal(Register.Int rd, Label label) implements Instr {

        @Override
        public String toString() {
            return String.format("jal %s %s", rd, label);
        }
    }

    public record Call(Label label) implements Instr {

        @Override
        public String toString() {
            return String.format("call %s", label);
        }
    }

    public record Jalr(Register.Int rd, Register.Int rs1, int imm) implements Instr {

        public Jalr {
            Assertions.requires(-2048 <= imm && imm < 2048, "Immediate incompatible in JALR");
        }

        @Override
        public String toString() {
            return String.format("jalr %s %d(%s)", rd, imm, rs1);
        }
    }

    public record LocalLabel(Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("%s", label);
        }
    }

    public record Auipc(Register.Int rd, int immu) implements Instr {
        @Override
        public String toString() {
            return String.format("auipc %s, %d", rd, immu);
        }
    }

    public record Lui(Register.Int rd, int immu) implements Instr {
        @Override
        public String toString() {
            return String.format("lui %s, %d", rd, immu);
        }
    }

    public record Ecall() implements Instr {}

    public record Li(Register.Int rd, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("li %s, %d", rd, imm);
        }
    }

    public record Li_globl(Register.Int rd, Label imm) implements Instr {
        @Override
        public String toString() {
            return String.format("li %s, $%s", rd, imm);
        }
    }

    public record La(Register.Int rd, Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("la %s, %s", rd, label);
        }
    }

    public record J(Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("j %s", label);
        }
    }

    public record Jr(Register.Int rs) implements Instr {
        @Override
        public String toString() {
            return String.format("jr %s", rs);
        }
    }

    public record Ret() implements Instr {
        @Override
        public String toString() {
            return String.format("ret");
        }
    } 
}
