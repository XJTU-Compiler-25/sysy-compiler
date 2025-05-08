package cn.edu.xjtu.sysy.symbol;

import java.util.List;

@SuppressWarnings("unused")
public final class BuiltinSymbols {

    private BuiltinSymbols() { }

    public static final Symbol.Func GETINT = new Symbol.Func("getint",
            Type.Primitive.INT, List.of());

    public static final Symbol.Func GETCH = new Symbol.Func("getch",
            Type.Primitive.INT, List.of());

    public static final Symbol.Func GETFLOAT = new Symbol.Func("getfloat",
            Type.Primitive.FLOAT, List.of());

    public static final Symbol.Var GETARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            new Type.Array(Type.Primitive.INT, new int[] { -1 }), false, -8);
    public static final Symbol.Func GETARRAY = new Symbol.Func("getarray",
            Type.Primitive.INT, List.of(GETARRAY_ARG0));

    public static final Symbol.Var GETFARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            new Type.Array(Type.Primitive.FLOAT, new int[] { -1 }), false, -8);
    public static final Symbol.Func GETFARRAY = new Symbol.Func("getfarray",
            Type.Primitive.INT, List.of(GETFARRAY_ARG0));

    public static final Symbol.Var PUTINT_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Type.Primitive.INT, false, -4);
    public static final Symbol.Func PUTINT = new Symbol.Func("putint",
            Type.Void.INSTANCE, List.of(PUTINT_ARG0));

    public static final Symbol.Var PUTCH_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Type.Primitive.INT, false, -4);
    public static final Symbol.Func PUTCH = new Symbol.Func("putch",
            Type.Void.INSTANCE, List.of(PUTCH_ARG0));

    public static final Symbol.Var PUTFLOAT_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Type.Primitive.FLOAT, false, -4);
    public static final Symbol.Func PUTFLOAT = new Symbol.Func("putfloat",
            Type.Void.INSTANCE, List.of(PUTFLOAT_ARG0));

    public static final Symbol.Var PUTARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Type.Primitive.INT, false, -4);
    public static final Symbol.Var PUTARRAY_ARG1 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg1",
            new Type.Array(Type.Primitive.INT, new int[] { -1 }), false, -20);
    public static final Symbol.Func PUTARRAY = new Symbol.Func("putarray",
            Type.Void.INSTANCE, List.of(PUTARRAY_ARG0, PUTARRAY_ARG1));

    public static final Symbol.Var PUTFARRAY_ARG0 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg0",
            Type.Primitive.INT, false, -4);
    public static final Symbol.Var PUTFARRAY_ARG1 = new Symbol.Var(Symbol.Var.Kind.LOCAL, "arg1",
            new Type.Array(Type.Primitive.FLOAT, new int[] { -1 }), false, -20);
    public static final Symbol.Func PUTFARRAY = new Symbol.Func("putfarray",
            Type.Void.INSTANCE, List.of(PUTFARRAY_ARG0, PUTFARRAY_ARG1));

    // void putf(format, ...) 未实现

    public static final Symbol.Func STARTTIME = new Symbol.Func("_sysy_starttime",
            Type.Void.INSTANCE, List.of());
    public static final Symbol.Func STOPTIME = new Symbol.Func("_sysy_stoptime",
            Type.Void.INSTANCE, List.of());

}
