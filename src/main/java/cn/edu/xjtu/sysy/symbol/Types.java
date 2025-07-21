package cn.edu.xjtu.sysy.symbol;

import cn.edu.xjtu.sysy.util.Assertions;

import java.util.Arrays;

import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

public final class Types {

    private Types() {}

    public static final Type.Int Int = new Type.Int();
    public static final Type.Float Float = new Type.Float();
    public static final Type.Void Void = new Type.Void();

    public static final Type.Pointer IntPtr = ptrOf(Int);
    public static final Type.Pointer FloatPtr = ptrOf(Float);

    public static Type.Pointer ptrOf(Type type) {
        return new Type.Pointer(type);
    }

    public static Type.Array arrayOf(Type type, int size) {
        return switch (type) {
            case Type.Scalar s -> arrayOf(s, size);
            case Type.Array a -> arrayOf(a, size);
            default -> unsupported(type);
        };
    }

    public static Type.Array arrayOf(Type.Array array, int size) {
        var oldDim = array.dimensions;
        var oldDimLen = oldDim.length;
        var newDim = Arrays.copyOf(oldDim, oldDimLen + 1);
        newDim[oldDimLen] = size;
        return new Type.Array(array.elementType, newDim);
    }

    public static Type.Array arrayOf(Type.Scalar type, int size) {
        return new Type.Array(type, new int[] { size });
    }

    public static Type.Array arrayOf(Type.Scalar type, int[] dims) {
        return new Type.Array(type, Arrays.copyOf(dims, dims.length));
    }

    public static Type.Function function(Type retType, Type... argTypes) {
        return new Type.Function(retType, argTypes);
    }

    public static Type.Pointer decay(Type.Array arr) {
        return ptrOf(arr.getIndexElementType(1));
    }

    // 将指针指向的大小固定，使之成为数组
    public static Type.Array fixed(Type.Pointer ptr, int size) {
        return arrayOf(ptr.baseType, size);
    }

    public static int[] strides(Type t) {
        return switch (t) {
            case Type.Pointer ptr -> strides(ptr);
            case Type.Array array -> strides(array);
            default -> unsupported(t);
        };
    }

    public static int[] strides(Type.Pointer ptr) {
        var base = ptr.baseType;
        return switch (base) {
            case Type.Array it -> {
                var dims = it.dimensions;
                var len = dims.length;
                var strides = new int[len + 1];
                int accmu = 1;
                for (int i = 0; i < len; ++i) {
                    strides[len - i] = accmu;
                    accmu *= it.dimensions[i];
                }
                strides[0] = accmu;
                yield strides;
            }
            case Type.Pointer it -> {
                var s = strides(it);
                var len = s.length;
                var strides = new int[len + 1];
                System.arraycopy(s, 0, strides, 0, len);
                strides[len] = 1;
                yield strides;
            }
            default -> new int[] { 1 };
        };
    }


    public static int[] strides(Type.Array array) {
        var dims = array.dimensions;
        var len = dims.length;
        var strides = new int[len];
        int accmu = 1;
        for (int i = 0; i < len; ++i) {
            strides[len - 1 - i] = accmu;
            accmu *= array.dimensions[i];
        }
        return strides;
    }

}
