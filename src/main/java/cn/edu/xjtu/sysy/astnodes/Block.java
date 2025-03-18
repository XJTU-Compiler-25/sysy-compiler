package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;
import cn.edu.xjtu.sysy.scope.SymbolTable;
import cn.edu.xjtu.sysy.scope.VarInfo;

/** Block 
 * 1. Block 表示语句块。语句块会创建作用域，语句块内声明的变量的生存期在
该语句块内。
2. 语句块内可以再次定义与语句块外同名的变量或常量（通过 Decl 语句)，其
作用域从定义处开始到该语句块尾结束，它隐藏语句块外的同名变量或常量
*/
public final class Block extends Node {
    /** 语句块元素 */
    public List<Stmt> stmts;
    
    private SymbolTable<VarInfo> varInfos;

    public Block(Token start, Token end, List<Stmt> stmts) {
        super(start, end);
        this.stmts = stmts;
    }

    @Override
    public String toString() {
        return "Block [Location=" + Arrays.toString(getLocation()) + "]";
    }

    public SymbolTable<VarInfo> getVarInfos() {
        return varInfos;
    }

    public void setVarInfos(SymbolTable<VarInfo> varInfos) {
        this.varInfos = varInfos;
    }

    public void accept(AstVisitor visitor) {
        for (Stmt stmt : stmts) {
            visitor.visit(stmt);
        }
    }
}
