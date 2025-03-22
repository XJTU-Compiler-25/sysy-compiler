package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.*;
import cn.edu.xjtu.sysy.ast.node.Expr.Operator;
import cn.edu.xjtu.sysy.symbol.SymbolTable;

/**
 * 第一轮遍历，仅确定定义中的编译时可确定表达式（即constExp），不包括effectively final
 */
@Deprecated(forRemoval = true) // 即将合并到 AstAnnotator 中
public class CompTimeValueFolder extends AstVisitor {
    private boolean toBool(int val) {
        return val != 0;
    }

    private boolean toBool(float val) {
        return val != 0.f;
    }

    private int toInt(boolean val) {
        return val ? 1 : 0;
    }

    public Number calculate(float left, Operator op, float right) {
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
    public Number visit(Expr.Binary expr) {
        Number left = visit(expr.lhs);
        if (expr.isLogicalExpr()) {
            if (expr.op.equals("&&") && left.floatValue() == 0.f) {
                //expr.setConstantValue(0);
                return 0;
            } else if (expr.op.equals("||") && left.floatValue() != 0.f) {
                //expr.setConstantValue(1);
                return 1;
            }
        }
        Number right = visit(expr.rhs);
        if (left == null || right == null) {
            return null;
        }
        if (left instanceof Float || right instanceof Float) {
            Number value = calculate(left.floatValue(), expr.op, right.floatValue());
            //expr.setConstantValue(value);
            return value;
        }
        Integer value = calculate(left.intValue(), expr.op, right.intValue());
        //expr.setConstantValue(value);
        return value;
    }

    @Override
    public Number visit(Expr.Unary expr) {
        Number operand = visit(expr.rhs);
        if (operand == null) {
            return null;
        }
        if (operand instanceof Float) {
            Number value = calculate(expr.op, operand.floatValue());
            //expr.setConstantValue(value);
            return value;
        }
        Integer value = calculate(expr.op, operand.intValue());
        //expr.setConstantValue(value);
        return value;
    }

    @Override
    public Number visit(Expr.Call node) {
        return null;
    }

    @Override
    public Number visit(Expr.Array node) {
        return null;
    }

    @Override
    public Number visit(Expr.IndexAccess node) {
        return null;
    }

    @Override
    public Number visit(Expr.IntLiteral node) {
        //node.setConstantValue(node.value);
        return node.value;
    }

    @Override
    public Number visit(Expr.FloatLiteral node) {
        //node.setConstantValue(node.value);
        return node.value;
    }

    @Override
    public Number visit(Expr.VarAccess node) {
        return constInfos.get(node.name);
    }
}
