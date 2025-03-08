package cn.edu.xjtu.sysy.ast;

/**
 * 标记会引入新的作用域的节点，可以获取作用域的符号表
 */
public sealed interface Scoped permits CompUnit, Block {
    /**
     * @return 作用域的符号表
     */
    SymbolTable getSymbolTable();
}
