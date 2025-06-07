package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import static cn.edu.xjtu.sysy.util.Assertions.todo;

public final class ImmediateValues {

    private ImmediateValues() {}

    public static final ImmediateValue.IntConst iZero = new ImmediateValue.IntConst(0);
    public static final ImmediateValue.IntConst iOne = new ImmediateValue.IntConst(1);
    public static final ImmediateValue.IntConst iNegOne = new ImmediateValue.IntConst(-1);

    public static final ImmediateValue.FloatConst fZero = new ImmediateValue.FloatConst(0.0f);
    public static final ImmediateValue.FloatConst fOne = new ImmediateValue.FloatConst(1.0f);

    public static final ImmediateValue.IntConst iTrue = iOne;
    public static final ImmediateValue.IntConst iFalse = iZero;

    public static ImmediateValue.IntConst intConst(int value) {
        if (value == 0) return iZero;
        else if (value == 1) return iOne;
        else if (value == -1) return iNegOne;
        else return new ImmediateValue.IntConst(value);
    }

    public static ImmediateValue.FloatConst floatConst(float value) {
        if (value == 0.0f) return fZero;
        else if (value == 1.0f) return fOne;
        else return new ImmediateValue.FloatConst(value);
    }

    public static ImmediateValue.ZeroInit ZeroInit = new ImmediateValue.ZeroInit();

    public static ImmediateValue.SparseArrayInit sparseArrayOf(Type type, int[] indexes, Value[] values) {
        return new ImmediateValue.SparseArrayInit(type, indexes, values);
    }

}
