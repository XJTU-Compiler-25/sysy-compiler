package cn.edu.xjtu.sysy.riscv.node;

import cn.edu.xjtu.sysy.riscv.node.Register.Int;
import cn.edu.xjtu.sysy.riscv.node.Register.Float;
public final class PhysicalRegisters {
    public static final Int A0 = new Int("a0");
    public static final Int A1 = new Int("a1");
    public static final Int A2 = new Int("a2");
    public static final Int A3 = new Int("a3");
    public static final Int A4 = new Int("a4");
    public static final Int A5 = new Int("a5");
    public static final Int A6 = new Int("a6");
    public static final Int A7 = new Int("a7");
    public static final Int T0 = new Int("t0");
    public static final Int T1 = new Int("t1");
    public static final Int T2 = new Int("t2");
    public static final Int T3 = new Int("t3");
    public static final Int T4 = new Int("t4");
    public static final Int T5 = new Int("t5");
    public static final Int T6 = new Int("t6");
    public static final Int S1 = new Int("s1");
    public static final Int S2 = new Int("s2");
    public static final Int S3 = new Int("s3");
    public static final Int S4 = new Int("s4");
    public static final Int S5 = new Int("s5");
    public static final Int S6 = new Int("s6");
    public static final Int S7 = new Int("s7");
    public static final Int S8 = new Int("s8");
    public static final Int S9 = new Int("s9");
    public static final Int S10 = new Int("s10");
    public static final Int S11 = new Int("s11");
    public static final Int FP = new Int("fp");
    public static final Int SP = new Int("sp");
    public static final Int GP = new Int("gp");
    public static final Int TP = new Int("tp");
    public static final Int RA = new Int("ra");
    public static final Int ZERO = new Int("zero");

    public static final Float FA0 = new Float("fa0");
    public static final Float FA1 = new Float("fa1");
    public static final Float FA2 = new Float("fa2");
    public static final Float FA3 = new Float("fa3");
    public static final Float FA4 = new Float("fa4");
    public static final Float FA5 = new Float("fa5");
    public static final Float FA6 = new Float("fa6");
    public static final Float FA7 = new Float("fa7");
    public static final Float FT0 = new Float("ft0");
    public static final Float FT1 = new Float("ft1");
    public static final Float FT2 = new Float("ft2");
    public static final Float FT3 = new Float("ft3");
    public static final Float FT4 = new Float("ft4");
    public static final Float FT5 = new Float("ft5");
    public static final Float FT6 = new Float("ft6");
    public static final Float FT7 = new Float("ft7");
    public static final Float FT8 = new Float("ft8");
    public static final Float FT9 = new Float("ft9");
    public static final Float FT10 = new Float("ft10");
    public static final Float FT11 = new Float("ft11");
    public static final Float FS0 = new Float("fs0");
    public static final Float FS1 = new Float("fs1");
    public static final Float FS2 = new Float("fs2");
    public static final Float FS3 = new Float("fs3");
    public static final Float FS4 = new Float("fs4");
    public static final Float FS5 = new Float("fs5");
    public static final Float FS6 = new Float("fs6");
    public static final Float FS7 = new Float("fs7");
    public static final Float FS8 = new Float("fs8");
    public static final Float FS9 = new Float("fs9");
    public static final Float FS10 = new Float("fs10");
    public static final Float FS11 = new Float("fs11");
    
    public static final Int getA(int idx) {
        return switch (idx) {
            case 0 -> A0;
            case 1 -> A1;
            case 2 -> A2;
            case 3 -> A3;
            case 4 -> A4;
            case 5 -> A5;
            case 6 -> A6;
            case 7 -> A7;
            default -> null;
        };
    }

    public static final Float getFA(int idx) {
        return switch (idx) {
            case 0 -> FA0;
            case 1 -> FA1;
            case 2 -> FA2;
            case 3 -> FA3;
            case 4 -> FA4;
            case 5 -> FA5;
            case 6 -> FA6;
            case 7 -> FA7;
            default -> null;
        };
    }
}