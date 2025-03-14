package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** VarDefStmt */
public final class VarDefStmt extends Stmt {
    
    public VarDefs varDefs;

    public VarDefStmt(Token start, Token end, VarDefs varDefs) {
        super(start, end);
        this.varDefs = varDefs;
    }

    @Override
    public String toString() {
        return "VarDefStmt [varDefs=" + varDefs + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(varDefs);
    }
}