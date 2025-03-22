package cn.edu.xjtu.sysy.type;

public abstract sealed class ValueType permits BaseType, ArrayType, ArrayLiteralType {
    
    public static final BaseType FLOAT_TYPE = new BaseType("float");
    public static final BaseType INT_TYPE = new BaseType("int");
    public static final BaseType VOID_TYPE = new BaseType("void");

    /** 错误类型，用于防止错误传递 */
    public static final BaseType ERR_TYPE = new BaseType("error");

    public boolean isBaseType() {
        return false;
    }

    public boolean isArrayType() {
        return false;
    }
}
