package cn.edu.xjtu.sysy.symbol;

import java.util.List;

@SuppressWarnings("unused")
public final class BuiltinSymbols {

    private BuiltinSymbols() { }

    // function types

    // () -> void
    private static final Type.Function FTY_V = Types.function(Types.Void);
    // () -> int
    private static final Type.Function FTY_I = Types.function(Types.Int);
    // () -> float
    private static final Type.Function FTY_F = Types.function(Types.Float);
    // (int[]) -> int
    private static final Type.Function FTY_I_IPtr = Types.function(Types.Int, Types.IntPtr);
    // (float[]) -> int
    private static final Type.Function FTY_I_FPtr = Types.function(Types.Int, Types.FloatPtr);
    // (int) -> void
    private static final Type.Function FTY_V_I = Types.function(Types.Void, Types.Int);
    // (float) -> void
    private static final Type.Function FTY_V_F = Types.function(Types.Void, Types.Float);
    // (int, int[]) -> void
    private static final Type.Function FTY_V_I_IPtr = Types.function(Types.Void, Types.Int, Types.IntPtr);
    // (int, float[]) -> void
    private static final Type.Function FTY_V_I_FPtr = Types.function(Types.Void, Types.Int, Types.FloatPtr);

    // symbols

    public static final Symbol.Func GETINT = new Symbol.Func("getint",
            FTY_I, List.of(), true);

    public static final Symbol.Func GETCH = new Symbol.Func("getch",
            FTY_I, List.of(), true);

    public static final Symbol.Func GETFLOAT = new Symbol.Func("getfloat",
            FTY_F, List.of(), true);

    public static final Symbol.Var GETARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.IntPtr, false);
    public static final Symbol.Func GETARRAY = new Symbol.Func("getarray",
            FTY_I_IPtr, List.of(GETARRAY_ARG0), true);

    public static final Symbol.Var GETFARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.FloatPtr, false);
    public static final Symbol.Func GETFARRAY = new Symbol.Func("getfarray",
            FTY_I_FPtr, List.of(GETFARRAY_ARG0), true);

    public static final Symbol.Var PUTINT_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.Int, false);
    public static final Symbol.Func PUTINT = new Symbol.Func("putint",
            FTY_V_I, List.of(PUTINT_ARG0), true);

    public static final Symbol.Var PUTCH_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.Int, false);
    public static final Symbol.Func PUTCH = new Symbol.Func("putch",
            FTY_V_I, List.of(PUTCH_ARG0), true);

    public static final Symbol.Var PUTFLOAT_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.Float, false);
    public static final Symbol.Func PUTFLOAT = new Symbol.Func("putfloat",
            FTY_V_F, List.of(PUTFLOAT_ARG0), true);

    public static final Symbol.Var PUTARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.Int, false);
    public static final Symbol.Var PUTARRAY_ARG1 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg1",
            Types.IntPtr, false);
    public static final Symbol.Func PUTARRAY = new Symbol.Func("putarray",
            FTY_V_I_IPtr, List.of(PUTARRAY_ARG0, PUTARRAY_ARG1), true);

    public static final Symbol.Var PUTFARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Types.Int, false);
    public static final Symbol.Var PUTFARRAY_ARG1 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg1",
            Types.FloatPtr, false);
    public static final Symbol.Func PUTFARRAY = new Symbol.Func("putfarray",
            FTY_V_I_FPtr, List.of(PUTFARRAY_ARG0, PUTFARRAY_ARG1), true);

    // void putf(format, ...) 未实现

    public static final Symbol.Func STARTTIME = new Symbol.Func("_sysy_starttime",
            FTY_V, List.of(), true);
    public static final Symbol.Func STOPTIME = new Symbol.Func("_sysy_stoptime",
            FTY_V, List.of(), true);

}
