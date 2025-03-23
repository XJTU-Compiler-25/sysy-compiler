package cn.edu.xjtu.sysy.symbol;

import java.util.Arrays;

public abstract sealed class Type {
    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    public static final class Primitive extends Type {
        public static final Primitive INT = new Primitive("int");
        public static final Primitive FLOAT = new Primitive("float");

        public static Primitive of(String name) {
            return switch (name) {
                case "int" -> INT;
                case "float" -> FLOAT;
                default -> throw new IllegalArgumentException("Unknown primitive type: " + name);
            };
        }

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
    }

    public static final class Array extends Type {
        public final Primitive elementType;
        public final int[] dimensions;

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

        public boolean isWildcard() {
            return dimensions[0] == -1;
        }

        /**
         * 取 depth 次索引后的元素类型
         */
        public Type getIndexElementType(int depth) {
            if(depth < 0 || depth > dimensions.length) throw new IllegalArgumentException("Illegal depth");
            else if (depth == 0) return this;
            else if (depth == dimensions.length) return elementType;
            else return getSubArrayType(depth);
        }

        public Array getSubArrayType(int depth) {
            return new Array(elementType, Arrays.copyOfRange(dimensions, depth, dimensions.length - depth + 1));
        }
    }

    /**
     * 空数组表达式类型，可以被任意 baseType 的 Array 接收
     * 只会出现在 Assign 的右端，而且最终要被消除掉
     */
    public static final class EmptyArray extends Type {
        public static final EmptyArray INSTANCE = new EmptyArray();

        private EmptyArray() {}

        @Override
        public String toString() {
            return "<EmptyArray>";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
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
    }
}
