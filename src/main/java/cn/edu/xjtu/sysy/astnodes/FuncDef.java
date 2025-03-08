package cn.edu.xjtu.sysy.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

/** Function Definition */
public final class FuncDef extends Node {

    public final List<FuncParam> params;
    public final Ident id;
    public final BType funcType;
    public final Block block;

    public FuncDef(Token start, Token end, Ident id, BType funcType, Block block) {
        super(start, end);
        this.params = new ArrayList<>();
        this.id = id;
        this.funcType = funcType;
        this.block = block;
    }

    public FuncDef(
            Token start, Token end, List<FuncParam> params, Ident id, BType funcType, Block block) {
        super(start, end);
        this.params = params;
        this.id = id;
        this.funcType = funcType;
        this.block = block;
    }

    public void addParam(FuncParam param) {
        this.params.add(param);
    }
}
