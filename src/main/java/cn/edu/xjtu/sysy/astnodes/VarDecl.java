package cn.edu.xjtu.sysy.astnodes;

import java.util.List;

import org.antlr.v4.runtime.Token;

/** VarDecl */
public final class VarDecl extends Node {

    public BType bType;
    public List<VarDef> defs;
    public boolean isConst;

    public VarDecl(Token start, Token end, boolean isConst, BType bType, List<VarDef> defs) {
        super(start, end);
        this.isConst = isConst;
        this.bType = bType;
        this.defs = defs;
    }
}
