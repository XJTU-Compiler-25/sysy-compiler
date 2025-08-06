package cn.edu.xjtu.sysy.util;

public final class MathUtils {
    private MathUtils() {}

    public static int dotProduct(int[] a, int[] b) {
        var len = a.length;
        Assertions.requires(len == b.length);

        int result = 0;
        for (int i = 0; i < len; i++) {
            result += a[i] * b[i];
        }

        return result;
    }

    public static int countElements(int[] dims) {
        var count = 1;
        for (int i = 0, maxLen = dims.length; i < maxLen; i++) {
            count *= dims[i];
        }
        return count;
    }

    public static int[] stride(int[] dims) {
        var dimLen = dims.length;
        var strides = new int[dimLen];
        var stride = 1;
        for (int i = 0; i < dimLen; ++i) {
            strides[dimLen - i - 1] = stride;
            stride *= dims[i];
        }
        return strides;
    }

    public static int saturatedAdd(int a, int b) {
        var sum = (long) a + (long) b;
        return (int) Math.min(sum, Integer.MAX_VALUE);
    }

    public static int saturatedMul(int a, int b) {
        var product = (long) a * (long) b;
        return (int) Math.min(product, Integer.MAX_VALUE);
    }

}
