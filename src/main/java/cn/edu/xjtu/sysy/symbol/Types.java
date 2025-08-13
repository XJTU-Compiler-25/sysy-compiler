package cn.edu.xjtu.sysy.symbol;

import cn.edu.xjtu.sysy.symbol.Type.Float;
import cn.edu.xjtu.sysy.symbol.Type.Void;

import cn.edu.xjtu.sysy.symbol.Type.*;
import java.util.Arrays;

import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

public final class Types {

    private Types() {}

    public static final Int Int = new Int();
    public static final Float Float = new Float();
    public static final Void Void = new Void();

    public static final Pointer IntPtr = ptrOf(Int);
    public static final Pointer FloatPtr = ptrOf(Float);

    public static Pointer ptrOf(Type type) {
        return new Pointer(type);
    }

    public static Array arrayOf(Type type, int size) {
        return switch (type) {
            case Type.Scalar s -> arrayOf(s, size);
            case Array a -> arrayOf(a, size);
            default -> unsupported(type);
        };
    }

    public static Array arrayOf(Array array, int size) {
        var oldDim = array.dimensions;
        var oldDimLen = oldDim.length;
        var newDim = Arrays.copyOf(oldDim, oldDimLen + 1);
        newDim[oldDimLen] = size;
        return new Array(array.elementType, newDim);
    }

    public static Array arrayOf(Type.Scalar type, int size) {
        return new Array(type, new int[] { size });
    }

    public static Array arrayOf(Type.Scalar type, int[] dims) {
        return new Array(type, Arrays.copyOf(dims, dims.length));
    }

    public static Function function(Type retType, Type... argTypes) {
        return new Function(retType, argTypes);
    }

    public static Pointer decay(Array arr) {
        return ptrOf(arr.getIndexElementType(1));
    }

    // 将指针指向的大小固定，使之成为数组
    public static Array fixed(Pointer ptr, int size) {
        return arrayOf(ptr.baseType, size);
    }

    public static int[] strides(Type t) {
        return switch (t) {
            case Pointer ptr -> strides(ptr);
            case Array array -> strides(array);
            default -> unsupported(t);
        };
    }

    public static int[] strides(Pointer ptr) {
        var base = ptr.baseType;
        return switch (base) {
            case Array it -> {
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
            case Pointer it -> {
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


    public static int[] strides(Array array) {
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

    public static int sizeOf(Type type) {
        return switch (type) {
            case Array array -> sizeOf(array.elementType) * array.elementCount;
            case Pointer _ -> 8;
            case Int _, Float _ -> 4;
            default -> unsupported(type);
        };
    }

    public static int alignmentOf(Type type) {
        return switch (type) {
            case Array array -> alignmentOf(array.elementType);
            case Pointer _ -> 8;
            case Int _, Float _ -> 4;
            default ->  unsupported(type);
        };
    }

}
