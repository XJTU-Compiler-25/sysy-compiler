package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;

import java.util.Arrays;

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

    public static ImmediateValue.Undefined undefined() {
        return new ImmediateValue.Undefined();
    }

    public static final ImmediateValue.ZeroInit ZeroInit = new ImmediateValue.ZeroInit();

    public static ImmediateValue.DenseArray denseArrayOf(Type type, Value[] values) {
        return new ImmediateValue.DenseArray(type, values);
    }

    public static ImmediateValue.SparseArray sparseArrayOf(Type type, int capacity, int[] indexes, Value[] values) {
        return new ImmediateValue.SparseArray(type, capacity, indexes, values);
    }

    public static ImmediateValue.DenseArray zeroedDenseArray(Type type) {
        var arrType = (Type.Array) type;
        var values = new Value[arrType.elementCount];
        var elemType = arrType.elementType;
        if (elemType == Types.Int) Arrays.fill(values, iZero);
        else if (elemType == Types.Float) Arrays.fill(values, fZero);
        else Assertions.unsupported(elemType);
        return new ImmediateValue.DenseArray(type, values);
    }

    public static ImmediateValue.DenseArray sparseToDense(ImmediateValue.SparseArray sparseArray) {
        var values = sparseArray.values;
        var denseValues = new Value[sparseArray.size];
        values.forEach((index, value) -> denseValues[index] = value);
        return new ImmediateValue.DenseArray(sparseArray.type, denseValues);
    }

}
