package cn.edu.xjtu.sysy.scope;

import cn.edu.xjtu.sysy.type.ValueType;

/** 符号表中的变量信息 */
public class VarInfo {
    
    private final String varName;
    private final ValueType varType;
    private final boolean isConst;
    private final ScopeInfo scope;

    public VarInfo(String varName, ValueType varType, boolean isConst, ScopeInfo scope) {
        this.varName = varName;
        this.varType = varType;
        this.isConst = isConst;
        this.scope = scope;
    }

    public String getVarName() {
        return varName;
    }

    public ValueType getVarType() {
        return varType;
    }

    public boolean isConst() {
        return isConst;
    }

    public ScopeInfo getScope() {
        return scope;
    }
}
