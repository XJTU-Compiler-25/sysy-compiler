package cn.edu.xjtu.sysy.riscv;

import java.util.Arrays;
import java.util.HashSet;

import static cn.edu.xjtu.sysy.riscv.Register.Int.*;
import static cn.edu.xjtu.sysy.riscv.Register.Float.*;

public final class ValueUtils {

    private ValueUtils() {}

    // 12 bits
    public static final int immWidth = 12;

    // 8 bytes
    public static final int regSize = 8;

    public static final Register.Int stackPointer = SP;
    public static final Register.Int framePointer = FP;
    public static final Register.Int intRetAddr = RA;
    public static final Register.Float floatRetAddr = FA0;

    public static final Register.Int[] intParams = new Register.Int[]{
            A0, A1, A2, A3, A4, A5, A6, A7
    };

    public static final Register.Float[] floatParams = new Register.Float[]{
            FA0, FA1, FA2, FA3, FA4, FA5, FA6, FA7
    };

    public static final Register.Int[] calleeSavedUsableIntRegs = new Register.Int[] {
            S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11
    };

    public static final Register.Int[] callerSavedUsableIntRegs = new Register.Int[] {
            A0, A1, A2, A3, A4, A5, A6, A7,
            T0, T1, T2, // T3, T4, T5, T6 preserved
    };

    public static final int usableIntRegCount = calleeSavedUsableIntRegs.length + callerSavedUsableIntRegs.length;

    public static final Register.Float[] calleeSavedUsableFloatRegs = new Register.Float[] {
            FS0, FS1, FS2, FS3, FS4, FS5, FS6, FS7, FS8, FS9, FS10, FS11
    };

    public static final Register.Float[] callerSavedUsableFloatRegs = new Register.Float[] {
            FA0, FA1, FA2, FA3, FA4, FA5, FA6, FA7,
            FT0, FT1, FT2, FT3, FT4, FT5, FT6, FT7, // FT8, FT9, FT10, FT11 preserved
    };

    public static final int usableFloatRegCount = calleeSavedUsableFloatRegs.length + callerSavedUsableFloatRegs.length;

    public static final Register.Int spillIntReg = T5;
    public static final Register.Int spillIntReg2 = T6;
    public static final Register.Float spillFloatReg = FT10;
    public static final Register.Float spillFloatReg2 = FT11;

    public static final Register.Int phiElimIntReg = T3;
    public static final Register.Float phiElimFloatReg = FT8;

    public static final Register.Int intScratchReg = T4;
    public static final Register.Float floatScratchReg = FT9;

    public static final HashSet<Register> calleeSaved = new HashSet<>(Arrays.asList(
            FP, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11,
            FS0, FS1, FS2, FS3, FS4, FS5, FS6, FS7, FS8, FS9, FS10, FS11));

    public static final HashSet<Register> callerSaved = new HashSet<>(Arrays.asList(
            RA, A0, A1, A2, A3, A4, A5, A6, A7,
            T0, T1, T2, T3, T4, T5, T6,
            FA0, FA1, FA2, FA3, FA4, FA5, FA6, FA7,
            FT0, FT1, FT2, FT3, FT4, FT5, FT6, FT7, FT8, FT9, FT10, FT11
    ));

    public static boolean isCalleeSaved(Register reg) {
        return calleeSaved.contains(reg);
    }

    public static boolean isCallerSaved(Register reg) {
        return callerSaved.contains(reg);
    }

}
