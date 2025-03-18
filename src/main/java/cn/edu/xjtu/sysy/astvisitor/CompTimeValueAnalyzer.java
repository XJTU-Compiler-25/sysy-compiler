package cn.edu.xjtu.sysy.astvisitor;

import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.BinaryExpr;
import cn.edu.xjtu.sysy.astnodes.CallExpr;
import cn.edu.xjtu.sysy.astnodes.FloatLiteral;
import cn.edu.xjtu.sysy.astnodes.Ident;
import cn.edu.xjtu.sysy.astnodes.IndexExpr;
import cn.edu.xjtu.sysy.astnodes.IntLiteral;
import cn.edu.xjtu.sysy.astnodes.UnaryExpr;
import cn.edu.xjtu.sysy.scope.SymbolTable;

/** 第一轮遍历，仅确定定义中的编译时可确定表达式（即constExp），不包括effectively final */
public class CompTimeValueAnalyzer extends ExprVisitor<Number> {
    
    private SymbolTable<Number> constInfos;
    
    public CompTimeValueAnalyzer(SymbolTable<Number> constInfos) {
        this.constInfos = constInfos;
    }
    
    private boolean toBool(int val) {
        return val != 0;
    }

    private boolean toBool(float val) {
        return val != 0.f;
    }

    private int toInt(boolean val) {
        return val ? 1 : 0;
    }

    public Number calculate(float left, String op, float right) {
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            case "%" -> left % right;
            case "&&" -> toInt(toBool(left) && toBool(right));
            case "||" -> toInt(toBool(left) || toBool(right));
            case ">=" -> toInt(left >= right);
            case ">" -> toInt(left > right);
            case "<=" -> toInt(left <= right);
            case "<" -> toInt(left < right);
            case "==" -> toInt(left == right);
            case "!=" -> toInt(left != right);
            default -> null;
        };
    }

    public Integer calculate(String op, int operand) {
        return switch (op) {
            case "+" -> operand;
            case "-" -> -operand;
            case "!" -> toInt(!toBool(operand));
            default -> null;
        };
    }

    public Integer calculate(int left, String op, int right) {
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            case "%" -> left % right;
            case "&&" -> toInt(toBool(left) && toBool(right));
            case "||" -> toInt(toBool(left) || toBool(right));
            case ">=" -> toInt(left >= right);
            case ">" -> toInt(left > right);
            case "<=" -> toInt(left <= right);
            case "<" -> toInt(left < right);
            case "==" -> toInt(left == right);
            case "!=" -> toInt(left != right);
            default -> null;
        };
    }

    public Number calculate(String op, float operand) {
        return switch (op) {
            case "+" -> operand;
            case "-" -> -operand;
            case "!" -> toInt(!toBool(operand));
            default -> null;
        };
    }

    @Override
    public Number visit(BinaryExpr expr) {
        Number left = visit(expr.left);
        if (expr.isLogicalExpr()) {
            if (expr.operator.equals("&&") && left.floatValue() == 0.f) {
                //expr.setConstantValue(0);
                return 0;
            } else if (expr.operator.equals("||") && left.floatValue() != 0.f) {
                //expr.setConstantValue(1);
                return 1;
            }
        }
        Number right = visit(expr.right);
        if (left == null || right == null) {
            return null;
        }
        if (left instanceof Float || right instanceof Float) {
            Number value = calculate(left.floatValue(), expr.operator, right.floatValue());
            //expr.setConstantValue(value);
            return value;
        }
        Integer value = calculate(left.intValue(), expr.operator, right.intValue());
        //expr.setConstantValue(value);
        return value;
    }

    @Override
    public Number visit(UnaryExpr expr) {
        Number operand = visit(expr.operand);
        if (operand == null) {
            return null;
        }
        if (operand instanceof Float) {
            Number value = calculate(expr.operator, operand.floatValue());
            //expr.setConstantValue(value);
            return value;
        }
        Integer value = calculate(expr.operator, operand.intValue());
        //expr.setConstantValue(value);
        return value;
    }

    @Override
    public Number visit(CallExpr node) {
        return null;
    }

    @Override
    public Number visit(ArrayExpr node) {
        return null;
    }

    @Override
    public Number visit(IndexExpr node) {
        return null;
    }

    @Override
    public Number visit(IntLiteral node) {
        //node.setConstantValue(node.value);
        return node.value;
    }

    @Override
    public Number visit(FloatLiteral node) {
        //node.setConstantValue(node.value);
        return node.value;
    }

    @Override
    public Number visit(Ident node) {
        return constInfos.get(node.name);
    }
}
