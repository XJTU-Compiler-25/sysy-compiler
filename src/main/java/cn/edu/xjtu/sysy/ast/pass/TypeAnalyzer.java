package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.*;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;

import static cn.edu.xjtu.sysy.util.Todo.todo;
import static cn.edu.xjtu.sysy.symbol.Type.*;

@Deprecated(forRemoval = true) // 即将合并到 AstAnnotator 中
public class TypeAnalyzer extends AstVisitor {
    SymbolTable.Global globalST;

    @Override
    public void visit(Expr.Binary expr) {
        var op = expr.op;
        var lhs = expr.lhs;
        var rhs = expr.rhs;

        visit(lhs);
        visit(rhs);

        Type lType = lhs.type;
        Type rType = rhs.type;

        if (lType == null || rType == null) {
            throw new IllegalArgumentException("Subexpression type not annotated");
        }

        // 非基本类型无法运算
        if (!(lType instanceof Primitive lTy && rType instanceof Primitive rTy)) {
            throw new IllegalArgumentException("Incompatible type in binary expr: " + lType + op + rType);
        }

        switch(op) {
            case LT, GT, LE, GE, EQ, NE, AND, OR:
                // 关系运算和逻辑运算均返回0，1的int类型
                expr.setType(Primitive.INT);
                break;
            case ADD, SUB, MUL, DIV, MOD:
                // 算数运算若包含float则int隐式转换为float，结果为float
                if (lTy.equals(Primitive.FLOAT) || rTy.equals(Primitive.FLOAT)) expr.setType(Primitive.FLOAT);
                // 否则int op int = int
                else expr.setType(Primitive.INT);
                break;
            default:
                throw new IllegalArgumentException("Unexpected operator in binary expr: " + op);
        }
    }

    @Override
    public void visit(Expr.Unary expr) {
    }

    @Override
    public Type visit(Expr.Call node) {
        FuncInfo funcInfo = funcInfos.get(node.funcName.name);
        // 函数先声明再使用
        if (funcInfo == null) {
            //err
        }
        // 可以为null
        //ValueType type = funcInfo.retType;
        //node.setInferredType(type);
        //return type;
        return todo();
    }

    @Override
    public void visit(Expr.Array node) {
        super.visit(node);
    }

    @Override
    public Type visit(Expr.IndexAccess node) {
        return null;
    }

    @Override
    public Type visit(Expr.IntLiteral node) {
        return null;
    }

    @Override
    public Type visit(Expr.FloatLiteral node) {
        return null;
    }

    @Override
    public Type visit(Expr.VarAccess node) {
        return null;
    }
}
