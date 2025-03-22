package cn.edu.xjtu.sysy.ast.pass;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xjtu.sysy.ast.node.*;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;

public class TopLevelSemanticAnalyzer extends AstVisitor {
    private SymbolTable<FuncInfo> funcInfos = new SymbolTable<>();
    private SymbolTable<Symbol.Var> varInfos = new SymbolTable<>();
    private SymbolTable<Number> constInfos = new SymbolTable<>();

    @Override
    public void visit(CompUnit node) {
        node.accept(this);
    }

    @Override
    public void visit(Decl.VarDefs node) {
        boolean isConst = node.isConst;
        Type.Primitive bType = new Type.Primitive(node.type);
        Type varType;
        for (Decl.RawVarDef varDef : node.defs) {
            if (isConst && varDef.init == null) {
                //err
            }
            CompTimeValueFolder analyzer = new CompTimeValueFolder(constInfos);
            if (varDef.dimensions != null) {
                int[] dims = new int[varDef.dimensions.size()];
                for (int i = 0; i < varDef.dimensions.size(); i++) {
                    Number result = analyzer.visit(varDef.dimensions.get(i));
                    if (result instanceof Integer it) {
                        if (it < 0) {
                            err(varDef, "size of array '%s' is negative", varDef.id.name);
                            continue;
                        }
                        dims[i] = it;

                    } else {
                        err(varDef,
                            "constant expression required for size of array '%s'",
                            varDef.id.name);
                    }
                }
                ArrayChecker checker = new ArrayChecker(dims);
                if (varDef.init instanceof Expr.Array it) {
                    varDef.init = checker.generateRealValue(it);
                    errors.addAll(checker.getErrors());
                } else {
                    err(varDef, "invalid initializer");
                }
                
                varType = new Type.Array(bType, dims);
            } else {
                if (isConst) {
                    Number result = analyzer.visit(varDef.init);
                    constInfos.put(varDef.name.name, result);
                }
                varType = bType;
            }
            Symbol.Var var = new Symbol.Var(varDef.name.name, varType, isConst, null);
            varInfos.put(varDef.name.name, var);
        }
    }

    @Override
    public void visit(Decl.FuncDef node) {
        
    }
}
