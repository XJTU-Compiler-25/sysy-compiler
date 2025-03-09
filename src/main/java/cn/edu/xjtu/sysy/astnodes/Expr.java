package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.type.ValueType;

/** Expressions */
public abstract class Expr extends Node {
    public ValueType inferredType = null;

    public boolean isComptime = false;

    public Number constantValue = null;
    
    public Expr(Token start, Token end) {
        super(start, end);
    }

    public void setInferredType(ValueType type) {
        this.inferredType = type;
    } 

    public ValueType getInferredType() {
        return this.inferredType;
    }

    public void setConstantValue(Integer value) {
        this.constantValue = value;
    }

    public void setConstantValue(Float value) {
        this.constantValue = value;
    }

    public boolean isConstant() {
        return constantValue == null;
    }

    public boolean isAssignable() {
        return false;
    }
}
