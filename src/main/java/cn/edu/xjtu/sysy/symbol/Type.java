package cn.edu.xjtu.sysy.symbol;

import java.util.Arrays;

public abstract sealed class Type {
    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    /**
     * 判断本类型的变量是否能接收另一类型的值
     * 例：
     * 函数形参类型 isAssignableFrom 函数实参类型
     * 变量类型 isAssignableFrom 初值表达式类型
     * 赋值表达式左端类型 isAssignableFrom 赋值表达式右端类型
     */
    public abstract boolean isAssignableFrom(Type other);

    /**
     * 可以作为值的实际类型的类型
     * 如 void, int[] 就不可能是值的实际类型
     */
    public static abstract sealed class ValueType extends Type {}

    public static final class Primitive extends ValueType {
        public static final Primitive INT = new Primitive("int");
        public static final Primitive FLOAT = new Primitive("float");

        public final String name;

        private Primitive(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Primitive other && name.equals(other.name));
        }

        @Override
        public boolean isAssignableFrom(Type other) {
            if(this.equals(other)) return true;

            // int float 互相可以隐式转换
            if(other instanceof Primitive) return true;

            return false;
        }
    }

    public static final class Array extends ValueType {
        private final Primitive elementType;
        private final int[] dimensions;

        public Array(Primitive elementType, int[] dimensions) {
            this.elementType = elementType;
            this.dimensions = dimensions;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder(elementType.name);
            for (var dim : dimensions) sb.append('[').append(dim).append(']');
            return sb.toString();
        }

        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Array other
                    && elementType == other.elementType
                    && Arrays.equals(dimensions, other.dimensions));
        }

        public Type getElementType() {
            return elementType;
        }

        /**
         * 取 depth 次索引后的元素类型
         */
        public Type getIndexElementType(int depth) {
            if(depth > dimensions.length) throw new IllegalArgumentException("depth > dimensions.length");
            else if(depth == dimensions.length) return elementType;
            else return getSubArrayType(depth);
        }

        public Array getSubArrayType(int depth) {
            return new Array(elementType, Arrays.copyOfRange(dimensions, depth, dimensions.length - depth));
        }

        @Override
        public boolean isAssignableFrom(Type other) {
            return this.equals(other);
        }
    }

    /**
     * 第一维长度未知的数组类型，只能出现在函数参数处
     */
    public static final class WildcardArray extends Type {
        private final Primitive elementType;
        /**
         * 除第一维以外的维度
         */
        private final int[] otherDimensions;

        public WildcardArray(Primitive elementType, int[] otherDimensions) {
            this.elementType = elementType;
            this.otherDimensions = otherDimensions;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder(elementType.name);
            sb.append("[]");
            for (var dim : otherDimensions) sb.append('[').append(dim).append(']');
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || (obj instanceof WildcardArray other
                    && elementType == other.elementType
                    && Arrays.equals(otherDimensions, other.otherDimensions));
        }

        @Override
        public boolean isAssignableFrom(Type other) {
            return other instanceof Array aType && elementType == aType.elementType
                    && Arrays.equals(otherDimensions, 0, otherDimensions.length,
                    aType.dimensions, 1, aType.dimensions.length);
        }
    }

    /**
     * void 类型，只能出现在函数返回值处
     */
    public static final class Void extends Type {
        public static final Void INSTANCE = new Void();

        private Void() {}

        @Override
        public String toString() {
            return "void";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        // 使用变量接收 void 类型值是不合法的
        @Override
        public boolean isAssignableFrom(Type other) {
            return false;
        }
    }
}
