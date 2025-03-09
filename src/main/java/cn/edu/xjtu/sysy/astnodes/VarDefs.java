package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

/** VarDecl */
public final class VarDefs extends Decl {

    public TypeAnnotation type;
    public List<VarDef> defs;
    public boolean isConst;

    public VarDefs(Token start, Token end, boolean isConst, TypeAnnotation type, List<VarDef> defs) {
        super(start, end);
        this.isConst = isConst;
        this.type = type;
        this.defs = defs;
    }

    @Override
    public String toString() {
        return  (isConst ? "Const" : "") + "VarDefs [type=" + type + ", defs=" + defs + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }
}
