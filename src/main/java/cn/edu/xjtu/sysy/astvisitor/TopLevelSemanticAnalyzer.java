package cn.edu.xjtu.sysy.astvisitor;

import cn.edu.xjtu.sysy.analysis.ArrayChecker;
import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.CompUnit;
import cn.edu.xjtu.sysy.astnodes.FuncDef;
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
            // 常量未初始化
            if (isConst && varDef.value == null) {
                err(varDef, "uninitialized const '%s'", varDef.id.name);
            }
            // 编译时常量分析
            CompTimeValueAnalyzer analyzer = new CompTimeValueAnalyzer(constInfos);
            
            // 数组初始化
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
                if (varDef.value instanceof ArrayExpr it) {
                    ArrayChecker checker = new ArrayChecker(dims);
                    varDef.value = checker.generateRealValue(it);
                    err(checker.getErrors());
                } else {
                    err(varDef, "invalid initializer");
                }

                varType = new ArrayType(bType, dims);
            } else {            // 标量初始化 
                if (varDef.value instanceof ArrayExpr) {
                    err(varDef, "invalid initializer");
                }
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
}
