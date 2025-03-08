package cn.edu.xjtu.sysy.ast;

/**
 * 语法树的语句类型节点
 */
public abstract sealed class Stmt extends Node permits BlockStmt, BreakStmt, ContinueStmt, EmptyStmt, ReturnStmt {
}
