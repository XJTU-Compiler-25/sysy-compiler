package cn.edu.xjtu.sysy.ast;

/**
 * 语法树的表达式类型节点
 */
public abstract sealed class Expr extends Node
        permits BinaryExpr, IntConstExpr, UnaryExpr {
    /**
     * 是否是可赋值的（左值）表达式
     */
    public boolean isAssignable = false;

    /**
     * 是否是编译时可求值的表达式
     */
    public boolean isComptime = false;

    /**
     * 是否是常量表达式
     * 注：编译时可求值表达式 总是 常量表达式
     */
    public boolean isConstant = false;

    /**
     * 表达式的类型
     */
    public Type type = Type.Unknown;
}
