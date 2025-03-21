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
    private List<SemanticError> errors = new ArrayList<>();

    public boolean hasError() {
        return !errors.isEmpty();
    }

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
                    if (result instanceof Integer it && it >= 0) {
                        dims[i] = it;
                    } else {
                        //error
                    }
                }
                ArrayChecker checker = new ArrayChecker(dims);
                if (varDef.init instanceof Expr.Array it) {
                    varDef.init = checker.generateRealValue(it);
                    errors.addAll(checker.getErrors());
                } else {
                    //err
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

    public List<SemanticError> getErrors() {
        return errors;
    }
}
