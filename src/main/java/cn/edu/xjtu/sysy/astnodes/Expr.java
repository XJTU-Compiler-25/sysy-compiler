package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.scope.ValueInfo;
import cn.edu.xjtu.sysy.type.ValueType;

/** Expressions */
public abstract class Expr extends Node {
    public ValueType inferredType = null;

    public boolean isComptime = false;

    /** 可在编译时确认的常量值 */
    public ValueInfo constantValue = null;
    
    public Expr(Token start, Token end) {
        super(start, end);
    }

    public void setInferredType(ValueType type) {
        this.inferredType = type;
    } 

    public ValueType getInferredType() {
        return this.inferredType;
    }

    public void setConstantValue(ValueInfo value) {
        this.constantValue = value;
    }

    public boolean isConstant() {
        return constantValue != null;
    }

    public boolean isAssignable() {
        return false;
    }

    public ValueInfo getConstantValue() {
        return constantValue;
    }
}
