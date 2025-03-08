package cn.edu.xjtu.sysy.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

/** Compile Unit */
public final class CompUnit extends Node {

    public final List<Node> declarations;

    public CompUnit(Token start, Token end) {
        super(start, end);
        this.declarations = new ArrayList<>();
    }

    public void addDeclaration(VarDecl decl) {
        this.declarations.add(decl);
    }

    public void addDeclaration(FuncDef def) {
        this.declarations.add(def);
    }
}
