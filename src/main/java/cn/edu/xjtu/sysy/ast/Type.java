package cn.edu.xjtu.sysy.ast;

public sealed class Type {

    public final String name;

    private Type(String name) { this.name = name; }

    @Override
    public String toString() {
        return name;
    }

    /**
     * 未知类型，在语义检查最后发现有未知应报错
     */
    public static final Type Unknown = new Type("unknown");

    public static final Type Int = new Type("int");
    public static final Type Float = new Type("float");

    public static final class Array extends Type {
        public Type baseType;
        public int size;

        public Array(Type baseType, int size) {
            super(baseType.name);
            this.baseType = baseType;
            this.size = size;
        }

        public Array(Type baseType) {
            this(baseType, -1);
        }
    }
}
