package cn.edu.xjtu.sysy.astvisitor;

import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.BinaryExpr;
import cn.edu.xjtu.sysy.astnodes.CallExpr;
import cn.edu.xjtu.sysy.astnodes.FloatLiteral;
import cn.edu.xjtu.sysy.astnodes.Ident;
import cn.edu.xjtu.sysy.astnodes.IndexExpr;
import cn.edu.xjtu.sysy.astnodes.IntLiteral;
import cn.edu.xjtu.sysy.astnodes.UnaryExpr;
import cn.edu.xjtu.sysy.scope.FuncInfo;
import cn.edu.xjtu.sysy.scope.SymbolTable;
import cn.edu.xjtu.sysy.type.ValueType;
import static cn.edu.xjtu.sysy.util.Todo.todo;

public class TypeAnalyzer extends ExprVisitor<ValueType> {

    SymbolTable<FuncInfo> funcInfos;

    @Override
    public ValueType visit(BinaryExpr expr) {
        ValueType left = visit(expr.left);
        ValueType right = visit(expr.right);
        if (left == null || right == null) {
            //err  void value not ignored as it ought to be
            expr.setInferredType(ValueType.ERR_TYPE);
            return ValueType.ERR_TYPE;
        }
        if (left.equals(ValueType.ERR_TYPE) || right.equals(ValueType.ERR_TYPE)) {
            expr.setInferredType(ValueType.ERR_TYPE);
            return ValueType.ERR_TYPE;
        }
        
        // 非基本类型无法运算。
        if (!left.isBaseType() || !right.isBaseType()) {
            //err invalid operands to binary %s (have ‘%s’ and ‘%s’), op, lefttype, righttype
            expr.setInferredType(ValueType.ERR_TYPE);
            return ValueType.ERR_TYPE;
        }
        // 关系运算和逻辑运算均返回0，1的int类型
        if (!expr.isArithExpr()) {
            expr.setInferredType(ValueType.INT_TYPE);
            return ValueType.INT_TYPE;
        }
        // 算数运算若包含float则int隐式转换为float，结果为float
        if (left.equals(ValueType.FLOAT_TYPE) || right.equals(ValueType.FLOAT_TYPE)) {
            expr.setInferredType(ValueType.FLOAT_TYPE);
            return ValueType.FLOAT_TYPE;
        }
        // 否则int op int = int
        expr.setInferredType(ValueType.INT_TYPE);
        return ValueType.INT_TYPE;
    }

    @Override
    public ValueType visit(UnaryExpr expr) {
        ValueType operand = visit(expr.operand);
        if (operand == null) {
            //err  void value not ignored as it ought to be
            return ValueType.ERR_TYPE;
        }
        if (operand.equals(ValueType.ERR_TYPE)) {
            return ValueType.ERR_TYPE;
        }
        if (!operand.isBaseType()) {
            //err invalid operands to unary %s (have ‘%s’), op, type
            expr.setInferredType(ValueType.ERR_TYPE);
            return ValueType.ERR_TYPE;
        }
        // 逻辑运算（非），返回类型为int
        if (expr.isLogicalExpr()) {
            expr.setInferredType(ValueType.INT_TYPE);
            return ValueType.INT_TYPE;
        }
        // 算数运算类型不变
        expr.setInferredType(operand);
        return operand;
    }

    @Override
    public ValueType visit(CallExpr expr) {
        FuncInfo funcInfo = funcInfos.get(expr.function.name);
        // 函数先声明再使用
        if (funcInfo == null) {
            //err
            expr.setInferredType(ValueType.ERR_TYPE);
            return ValueType.ERR_TYPE;
        }
        //ValueType type = funcInfo.retType;
        //node.setInferredType(type);
        //return type;
        return todo();
    }

    @Override
    public ValueType visit(ArrayExpr node) {
        return null;
    }

    @Override
    public ValueType visit(IndexExpr node) {
        return null;
    }

    @Override
    public ValueType visit(IntLiteral node) {
        return null;
    }

    @Override
    public ValueType visit(FloatLiteral node) {
        return null;
    }

    @Override
    public ValueType visit(Ident node) {
        return null;
    }
}
