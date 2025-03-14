package cn.edu.xjtu.sysy.astnodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;
import cn.edu.xjtu.sysy.scope.FuncInfo;
import cn.edu.xjtu.sysy.scope.SymbolTable;
import cn.edu.xjtu.sysy.scope.VarInfo;

/** Compile Unit
 * 1. 一个 SysY 程序由单个文件组成，文件内容对应 EBNF 表示中的 CompUnit。
在该 CompUnit 中，必须存在且仅存在一个标识为 ‘main’ 、无参数、返回类
型为 int 的 FuncDef(函数定义)。main 函数是程序的入口点，main 函数的返
回结果需要输出。
2. CompUnit 的顶层变量/常量声明语句（对应 Decl）、函数定义（对应 FuncDef）
都不可以重复定义同名标识符（Ident），即便标识符的类型不同也不允许。
3. CompUnit 的变量/常量/函数声明的作用域从该声明处开始到文件结尾。
 */
public final class CompUnit extends Node {

    public final List<Decl> declarations;

    public List<SemanticError> semErrors = new ArrayList<>();

    private SymbolTable<FuncInfo> funcInfos;
    private SymbolTable<VarInfo> varInfos;

    public CompUnit(Token start, Token end, List<Decl> declarations) {
        super(start, end);
        this.declarations = declarations;
    }

    public void semError(Node errNode, String errMsgForm, Object... args) {
        SemanticError err = new SemanticError(errNode, String.format(errMsgForm, args));
        this.semErrors.add(err);
    }

    @Override
    public String toString() {
        return "CompUnit [declarations=" + declarations + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }

    public SymbolTable<FuncInfo> getFuncInfos() {
        return funcInfos;
    }

    public void setFuncInfos(SymbolTable<FuncInfo> funcInfos) {
        this.funcInfos = funcInfos;
    }

    public SymbolTable<VarInfo> getVarInfos() {
        return varInfos;
    }

    public void setVarInfos(SymbolTable<VarInfo> varInfos) {
        this.varInfos = varInfos;
    }

    public void accept(AstVisitor visitor) {
        for (Decl decl : declarations) {
            visitor.visit(decl);
        }
    }
}
