package cn.edu.xjtu.sysy.ast;

import java.util.Arrays;

public final class Block extends Node implements Scoped {
    public final Stmt[] stmts;

    public Block(Stmt[] stmts) {
        this.stmts = stmts;
    }

    @Override
    public SymbolTable getSymbolTable() {
        return null;
    }

    @Override
    public String toString() {
        return "BlockNode{" +
                "stmts=" + Arrays.toString(stmts) +
                '}';
    }
}
