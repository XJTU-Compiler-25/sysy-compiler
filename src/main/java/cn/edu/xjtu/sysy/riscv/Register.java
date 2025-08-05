package cn.edu.xjtu.sysy.riscv;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

import java.util.stream.Stream;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public sealed interface Register {

    public abstract Type getType();
    /** 整数寄存器 */
    enum Int implements Register {
        A0("a0"), A1("a1"), A2("a2"), A3("a3"), A4("a4"), A5("a5"), A6("a6"),
        A7("a7"),
        T0("t0"), T1("t1"), T2("t2"), T3("t3"), T4("t4"), T5("t5"), T6("t6"),
        S1("s1"), S2("s2"), S3("s3"), S4("s4"), S5("s5"),
        S6("s6"), S7("s7"), S8("s8"), S9("s9"), S10("s10"), S11("s11"),
        FP("fp"), SP("sp"), GP("gp"), TP("tp"), RA("ra"), ZERO("zero");

        private final String name;
        public final Type type = Types.Int;

        Int(String name) { this.name = name; }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.name;
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
        public final Type type = Types.Float;

        Float(String name) { this.name = name; }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static Register.Float FA(int i) {
        return switch (i) {
            case 0 -> Float.FA0;
            case 1 -> Float.FA1;
            case 2 -> Float.FA2;
            case 3 -> Float.FA3;
            case 4 -> Float.FA4;
            case 5 -> Float.FA5;
            case 6 -> Float.FA6;
            case 7 -> Float.FA7;
            default -> null;
        };
    }

    public static Register.Int A(int i) {
        return switch (i) {
            case 0 -> Int.A0;
            case 1 -> Int.A1;
            case 2 -> Int.A2;
            case 3 -> Int.A3;
            case 4 -> Int.A4;
            case 5 -> Int.A5;
            case 6 -> Int.A6;
            case 7 -> Int.A7;
            default -> null;
        };
    }

    public static Stream<Register> calleeSaved() {
        return Stream.of(Int.FP, Int.S1, Int.S2, Int.S3, Int.S4, Int.S5, Int.S6, 
            Int.S7, Int.S8, Int.S9, Int.S10, Int.S11,
            Float.FS0, Float.FS1, Float.FS2, Float.FS3, Float.FS4, Float.FS5, Float.FS6, 
            Float.FS7, Float.FS8, Float.FS9, Float.FS10, Float.FS11);
    }

    public static Stream<Register> callerSaved() {
        return Stream.of(Int.RA, Int.A0, Int.A1, Int.A2, Int.A3, Int.A4, Int.A5, Int.A6, Int.A7, 
            Int.T0, Int.T1, Int.T2, Int.T3, Int.T4, Int.T5, Int.T6,
            Float.FA0, Float.FA1, Float.FA2, Float.FA3, Float.FA4, Float.FA5, Float.FA6, Float.FA7,
            Float.FT0, Float.FT1, Float.FT2, Float.FT3, Float.FT4, Float.FT5, Float.FT6, 
            Float.FT7, Float.FT8, Float.FT9, Float.FT10, Float.FT11);
    }
}
