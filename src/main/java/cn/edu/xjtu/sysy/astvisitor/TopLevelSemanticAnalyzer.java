package cn.edu.xjtu.sysy.astvisitor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xjtu.sysy.analysis.ArrayChecker;
import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.CompUnit;
import cn.edu.xjtu.sysy.astnodes.FuncDef;
import cn.edu.xjtu.sysy.astnodes.SemanticError;
import cn.edu.xjtu.sysy.astnodes.VarDef;
import cn.edu.xjtu.sysy.astnodes.VarDefs;
import cn.edu.xjtu.sysy.scope.FuncInfo;
import cn.edu.xjtu.sysy.scope.SymbolTable;
import cn.edu.xjtu.sysy.scope.VarInfo;
import cn.edu.xjtu.sysy.type.ArrayType;
import cn.edu.xjtu.sysy.type.BaseType;
import cn.edu.xjtu.sysy.type.ValueType;

public class TopLevelSemanticAnalyzer extends AstVisitor {
    private SymbolTable<FuncInfo> funcInfos = new SymbolTable<>();
    private SymbolTable<VarInfo> varInfos = new SymbolTable<>();
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
    public void visit(VarDefs node) {
        boolean isConst = node.isConst;
        BaseType bType = new BaseType(node.type);
        ValueType varType;
        for (VarDef varDef : node.defs) {
            if (isConst && varDef.value == null) {
                //err
            }
            CompTimeValueAnalyzer analyzer = new CompTimeValueAnalyzer(constInfos);
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
                if (varDef.value instanceof ArrayExpr it) {
                    varDef.value = checker.generateRealValue(it);
                    errors.addAll(checker.getErrors());
                } else {
                    //err
                }
                
                varType = new ArrayType(bType, dims);
            } else {
                if (isConst) {
                    Number result = analyzer.visit(varDef.value);
                    constInfos.put(varDef.id.name, result);
                }
                varType = bType;
            }
            VarInfo varInfo = new VarInfo(varDef.id.name, varType, isConst, null);
            varInfos.put(varDef.id.name, varInfo);
        }
    } 

    @Override
    public void visit(FuncDef node) {
        
    }

    public List<SemanticError> getErrors() {
        return errors;
    }
}
