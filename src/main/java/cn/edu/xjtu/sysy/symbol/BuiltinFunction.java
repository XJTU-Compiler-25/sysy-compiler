package cn.edu.xjtu.sysy.symbol;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public enum BuiltinFunction {

    GETINT(new Symbol.FuncSymbol("getint", Types.function(Types.Int), List.of(), true)),

    GETCH(new Symbol.FuncSymbol("getch", Types.function(Types.Int), List.of(), true)),

    GETFLOAT(new Symbol.FuncSymbol("getfloat", Types.function(Types.Float), List.of(), true)),

    GETARRAY(new Symbol.FuncSymbol("getarray", Types.function(Types.Int, Types.IntPtr), List.of(
            new Symbol.VarSymbol("arg0", false, Types.IntPtr, false)), true)),

    GETFARRAY(new Symbol.FuncSymbol("getfarray", Types.function(Types.Int, Types.FloatPtr), List.of(
            new Symbol.VarSymbol("arg0", false, Types.FloatPtr, false)), true)),

    PUTINT(new Symbol.FuncSymbol("putint", Types.function(Types.Void, Types.Int), List.of(
            new Symbol.VarSymbol("arg0", false, Types.Int, false)), true)),

    PUTCH(new Symbol.FuncSymbol("putch", Types.function(Types.Void, Types.Int), List.of(
            new Symbol.VarSymbol("arg0", false, Types.Int, false)), true),
            "putch"),

    PUTFLOAT(new Symbol.FuncSymbol("putfloat", Types.function(Types.Void, Types.Float), List.of(
            new Symbol.VarSymbol("arg0", false, Types.Float, false)), true)),

    PUTARRAY(new Symbol.FuncSymbol("putarray", Types.function(Types.Void, Types.Int, Types.IntPtr), List.of(
            new Symbol.VarSymbol("arg0", false, Types.Int, false),
            new Symbol.VarSymbol("arg1", false, Types.IntPtr, false)), true)),

    PUTFARRAY(new Symbol.FuncSymbol("putfarray", Types.function(Types.Void, Types.Int, Types.FloatPtr), List.of(
            new Symbol.VarSymbol("arg0", false, Types.Int, false),
            new Symbol.VarSymbol("arg1", false, Types.FloatPtr, false)), true)),

    STARTTIME(new Symbol.FuncSymbol("starttime", Types.function(Types.Void), List.of(), true),
            "_sysy_starttime"),

    STOPTIME(new Symbol.FuncSymbol("stoptime", Types.function(Types.Void), List.of(), true),
            "_sysy_stoptime"),
    ;

    public final Symbol.FuncSymbol symbol;
    /**
     * 比如 starttime 这样的函数需要链接到 _sysy_starttime
     * 则在 symbol 中填写语言中调用使用的名称，在 linkName 填写库里的符号名称
     */
    public final String linkName;

    BuiltinFunction(Symbol.FuncSymbol symbol) {
        this(symbol, symbol.name);
    }

    BuiltinFunction(Symbol.FuncSymbol symbol, String linkName) {
        this.symbol = symbol;
        this.linkName = linkName;
    }

    public static BuiltinFunction of(String name) {
        return Arrays.stream(BuiltinFunction.values()).filter(it -> name.equals(it.symbol.name)).findFirst().orElse(null);
    }

}
