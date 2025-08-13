package cn.edu.xjtu.sysy.symbol;

import java.util.Arrays;

public abstract sealed class Type {

    @Override
    public abstract String toString();

    /**
     * void 类型，只能出现在函数返回值处
     * EmptyArray 这一临时类型现在可以用 void 代替标注
     */
    public static final class Void extends Type {
        Void() { }

        @Override
        public String toString() {
            return "void";
        }
    }

    public static abstract sealed class Scalar extends Type { }

    public static final class Int extends Scalar {
        Int() { }

        @Override
        public String toString() {
            return "i32";
        }
    }

    public static final class Float extends Scalar {
        Float() { }

        @Override
        public String toString() {
            return "f32";
        }
    }

    /**
     * 按照比赛技术方案，target 均为 64 位，指针的大小为 8
     */
    public static final class Pointer extends Type {
        public final Type baseType;

        Pointer(Type baseType) {
            this.baseType = baseType;
        }

        @Override
        public String toString() {
            return "[Any x " + baseType.toString() + "]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Pointer ptrType && baseType.equals(ptrType.baseType);
        }

        /**
         * 取 depth 次索引后的元素类型
         */
        public Type getIndexElementType(int depth) {
            if (depth < 0) throw new IllegalArgumentException("Negative depth");
            else if (depth == 0) return this;
            else if (depth == 1) return baseType;
            else return switch (baseType) {
                    case Pointer p -> p.getIndexElementType(depth - 1);
                    case Array a -> a.getIndexElementType(depth - 1);
                    default -> throw new IllegalArgumentException("Cannot index into " + baseType);
                };
        }
    }


    // 从实现来看更像 matrix
    public static final class Array extends Type {
        public final Scalar elementType;
        public final int elementCount;

        /**
         * 各维度的长度反序存放，如 int[4][2] = Array(int, [2, 4])
         * 以便从数组构造减维度或加维度的数组（尾插/尾删更方便）
         */
        public final int[] dimensions;

        Array(Scalar elementType, int[] dimensions) {
            this.elementType = elementType;
            this.dimensions = dimensions;
            var size = 1;
            for (int dim : dimensions) size *= dim;
            this.elementCount = size;
        }

        @Override
        public String toString() {
            var dimLen = dimensions.length;
            var sb = new StringBuilder();

            for (int i = dimLen - 1; i >= 0; --i)
                sb.append('[').append(dimensions[i]).append(" x ");
            sb.append(elementType.toString());
            sb.append("]".repeat(dimLen));

            return sb.toString();
        }

        public boolean equals(Object obj) {
            return obj instanceof Array arrType && this.elementType.equals(arrType.elementType)
                    && Arrays.equals(this.dimensions, arrType.dimensions);
        }

        /**
         * 取 depth 次索引后的元素类型
         */
        public Type getIndexElementType(int depth) {
            if (depth < 0) throw new IllegalArgumentException("Negative depth");
            if (depth > dimensions.length) throw new IllegalArgumentException("depth > dimensions.length");
            else if (depth == 0) return this;
            else if (depth == dimensions.length) return elementType;
            else return getSubArrayType(depth);
        }

        public Array getSubArrayType(int depth) {
            return new Array(elementType, Arrays.copyOfRange(dimensions, 0, dimensions.length - depth));
        }

        public int getDimension(int depth) {
            return dimensions[dimensions.length - depth - 1];
        }
    }

    public static final class Function extends Type {
        public final Type returnType;
        public final Type[] paramTypes;

        Function(Type returnType, Type[] paramTypes) {
            this.returnType = returnType;
            this.paramTypes = paramTypes;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            sb.append('(');
            for (int i = 0; i < paramTypes.length; i++) {
                sb.append(paramTypes[i]);
                if (i != paramTypes.length - 1) sb.append(", ");
            }
            sb.append(") -> ").append(returnType);
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Function funcType && this.returnType.equals(funcType.returnType)
                    && Arrays.equals(this.paramTypes, funcType.paramTypes);
        }
    }
}
