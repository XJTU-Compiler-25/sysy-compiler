package cn.edu.xjtu.sysy.riscv.node;

import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.util.Assertions;

public sealed interface Instr {
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
        public String toString() {
            return String.format("%s %s, %s", op, rd, rs1);
        }
    }

    record Fclass(Register.Int rd, Register.Float rs) implements Instr {
        @Override
        public String toString() {
            return String.format("fclass.s %s, %s", rd, rs);
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
    }

    record FloatIntMv(Register.Int rd, Register.Float rs)
            implements Instr {

        @Override
        public String toString() {
            return String.format("fmv.x.w %s, %s", rd, rs);
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
    }

    record IntFloatMv(Register.Float rd, Register.Int rs)
            implements Instr {

        @Override
        public String toString() {
            return String.format("fmv.w.x %s, %s", rd, rs);
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
    }

    record Flw(Register.Float rd, Register.Int rs1, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("flw %s, %d(%s)", rd, imm, rs1);
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
    }

    record Fsw(Register.Float rd, Register.Int rs1, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("fsw %s, %d(%s)", rd, imm, rs1);
        }
    }

    record Sw_global(Register.Int rd, Label label, Register.Int tmp) implements Instr {
        @Override
        public String toString() {
            return String.format("sw %s, %s, %s", rd, label, tmp);
        }
    }

    record Fsw_global(Register.Float rd, Label label, Register.Int tmp) implements Instr {
        @Override
        public String toString() {
            return String.format("fsw %s, %s, %s", rd, label, tmp);
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
    }

    record Jal(Register.Int rd, Label label) implements Instr {

        @Override
        public String toString() {
            return String.format("jal %s %s", rd, label);
        }
    }

    record Call(Label label) implements Instr {

        @Override
        public String toString() {
            return String.format("call %s", label);
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
    }

    record LocalLabel(Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("%s", label);
        }
    }

    record Auipc(Register.Int rd, int immu) implements Instr {
        @Override
        public String toString() {
            return String.format("auipc %s, %d", rd, immu);
        }
    }

    record Lui(Register.Int rd, int immu) implements Instr {
        @Override
        public String toString() {
            return String.format("lui %s, %d", rd, immu);
        }
    }

    record Ecall() implements Instr {}

    record Li(Register.Int rd, int imm) implements Instr {
        @Override
        public String toString() {
            return String.format("li %s, %d", rd, imm);
        }
    }

    record Li_globl(Register.Int rd, Label imm) implements Instr {
        @Override
        public String toString() {
            return String.format("li %s, $%s", rd, imm);
        }
    }

    record La(Register.Int rd, Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("la %s, %s", rd, label);
        }
    }

    record J(Label label) implements Instr {
        @Override
        public String toString() {
            return String.format("j %s", label);
        }
    }

    record Jr(Register.Int rs) implements Instr {
        @Override
        public String toString() {
            return String.format("jr %s", rs);
        }
    }

    record Ret() implements Instr {
        @Override
        public String toString() {
            return "ret";
        }
    } 
}
