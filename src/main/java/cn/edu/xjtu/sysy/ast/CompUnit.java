package cn.edu.xjtu.sysy.ast;

/**
 * 编译单元
 */
public final class CompUnit extends Node implements Scoped {
    private SymbolTable symbolTable;

    @Override
    public SymbolTable getSymbolTable() {
        return null;
    }
}
