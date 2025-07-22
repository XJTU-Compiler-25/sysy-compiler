package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * 收集变量符号，用于后续 IR 生成
 */
public class VarCollector extends AstVisitor {
    public VarCollector(Pipeline<CompUnit> pipeline) {
        super(pipeline);
    }

    private List<Symbol.VarSymbol> vars;

    @Override
    public void visit(Decl.FuncDef node) {
        vars = new ArrayList<>();
        super.visit(node);
        node.allVars = vars;
        vars = null;
    }

    @Override
    public void visit(Stmt.LocalVarDef node) {
        node.varDefs.forEach(it -> vars.add(it.resolution));
    }
}
