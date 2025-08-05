package cn.edu.xjtu.sysy.riscv;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public sealed interface Register extends ValuePosition {
    /** 整数寄存器 */
    enum Int implements Register {
        A0("a0"), A1("a1"), A2("a2"), A3("a3"), A4("a4"), A5("a5"), A6("a6"),
        A7("a7"),
        T0("t0"), T1("t1"), T2("t2"), T3("t3"), T4("t4"), T5("t5"), T6("t6"),
        S1("s1"), S2("s2"), S3("s3"), S4("s4"), S5("s5"),
        S6("s6"), S7("s7"), S8("s8"), S9("s9"), S10("s10"), S11("s11"),
        FP("fp"), SP("sp"), GP("gp"), TP("tp"), RA("ra"), ZERO("zero");

        private final String name;

        Int(String name) { this.name = name; }

        @Override
        public String toString() {
            return this.name;
        }

        public static final Register.Int S0 = FP;

        public static Register.Int A(int i) {
            return switch (i) {
                case 0 -> A0;
                case 1 -> A1;
                case 2 -> A2;
                case 3 -> A3;
                case 4 -> A4;
                case 5 -> A5;
                case 6 -> A6;
                case 7 -> A7;
                default -> unreachable();
            };
        }
    }

    /** 浮点数寄存器 */
    enum Float implements Register {
        FT0("ft0"), FT1("ft1"), FT2("ft2"), FT3("ft3"), FT4("ft4"), FT5("ft5"), 
        FT6("ft6"), FT7("ft7"), FT8("ft8"), FT9("ft9"), FT10("ft10"), FT11("ft11"), 
        FS0("fs0"), FS1("fs1"), FS2("fs2"), FS3("fs3"), FS4("fs4"), FS5("fs5"), 
        FS6("fs6"), FS7("fs7"), FS8("fs8"), FS9("fs9"), FS10("fs10"), FS11("fs11"), 
        FA0("fa0"), FA1("fa1"), FA2("fa2"), FA3("fa3"), FA4("fa4"), FA5("fa5"), 
        FA6("fa6"), FA7("fa7");
        private final String name;

        Float(String name) { this.name = name; }

        @Override
        public String toString() {
            return this.name;
        }

    }
}
