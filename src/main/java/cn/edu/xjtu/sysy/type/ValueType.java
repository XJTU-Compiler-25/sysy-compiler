package cn.edu.xjtu.sysy.type;

public abstract sealed class ValueType permits BaseType, ArrayType, ArrayLiteralType {
    // TODO

    public static final BaseType FLOAT_TYPE = new BaseType("float");
    public static final BaseType INT_TYPE = new BaseType("int");
    
    public boolean isBaseType() {
        return false;
    }

    public boolean isArrayType() {
        return false;
    }
}
