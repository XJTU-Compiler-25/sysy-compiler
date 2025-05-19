// Generated from SysY.g4 by ANTLR 4.13.2

package cn.edu.xjtu.sysy.parse;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SysYParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SysYVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SysYParser#compUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompUnit(SysYParser.CompUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constVarDefs}
	 * labeled alternative in {@link SysYParser#varDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstVarDefs(SysYParser.ConstVarDefsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code normalVarDefs}
	 * labeled alternative in {@link SysYParser#varDefs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalVarDefs(SysYParser.NormalVarDefsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scalarVarDef}
	 * labeled alternative in {@link SysYParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarVarDef(SysYParser.ScalarVarDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayVarDef}
	 * labeled alternative in {@link SysYParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVarDef(SysYParser.ArrayVarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarType(SysYParser.VarTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(SysYParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#returnableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnableType(SysYParser.ReturnableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(SysYParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysYParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SysYParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStmt(SysYParser.EmptyStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varDefStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDefStmt(SysYParser.VarDefStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignmentStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStmt(SysYParser.AssignmentStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpStmt(SysYParser.ExpStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(SysYParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(SysYParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(SysYParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(SysYParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(SysYParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link SysYParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(SysYParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orCond}
	 * labeled alternative in {@link SysYParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrCond(SysYParser.OrCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expCond}
	 * labeled alternative in {@link SysYParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpCond(SysYParser.ExpCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relCond}
	 * labeled alternative in {@link SysYParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelCond(SysYParser.RelCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andCond}
	 * labeled alternative in {@link SysYParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndCond(SysYParser.AndCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqCond}
	 * labeled alternative in {@link SysYParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqCond(SysYParser.EqCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code floatConstExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatConstExp(SysYParser.FloatConstExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExp(SysYParser.UnaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExp(SysYParser.ParenExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intConstExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntConstExp(SysYParser.IntConstExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(SysYParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mulExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp(SysYParser.MulExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcCallExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallExp(SysYParser.FuncCallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varAccessExp}
	 * labeled alternative in {@link SysYParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAccessExp(SysYParser.VarAccessExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scalarAssignable}
	 * labeled alternative in {@link SysYParser#assignableExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarAssignable(SysYParser.ScalarAssignableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayAssignable}
	 * labeled alternative in {@link SysYParser#assignableExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAssignable(SysYParser.ArrayAssignableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code elementExp}
	 * labeled alternative in {@link SysYParser#arrayLiteralExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementExp(SysYParser.ElementExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayExp}
	 * labeled alternative in {@link SysYParser#arrayLiteralExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExp(SysYParser.ArrayExpContext ctx);
}