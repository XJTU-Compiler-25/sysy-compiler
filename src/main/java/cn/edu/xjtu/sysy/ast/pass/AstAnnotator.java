package cn.edu.xjtu.sysy.ast.pass;

import java.util.Arrays;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import cn.edu.xjtu.sysy.util.Placeholder;

/**
 * 标注类型信息、做类型检查、
 * 标注符号表信息、
 * 折叠编译期常量表达式
 *
 * 应当注意此时处理的数组仍然是 RawArray
 */
public final class AstAnnotator extends AstVisitor {
    public AstAnnotator(ErrManager errManager) {
        super(errManager);
    }

    private SymbolTable.Global globalST;
    private SymbolTable currentST;

    private Symbol.Func currentFunc;

    @Override
    public void visit(CompUnit node) {
        var globalST = new SymbolTable.Global();
        currentST = globalST;
        this.globalST = globalST;
        node.globalST = globalST;

        super.visit(node);
    }

    @Override
    public void visit(Decl.FuncDef node) {
        var funcST = new SymbolTable.Local(currentST);
        currentST = funcST;
        node.symbolTable = funcST;

        // resolveType
        var retTypeName = node.retTypeName;
        node.retType = retTypeName.equals("void") ? Type.Void.INSTANCE : Type.Primitive.of(node.retTypeName);

        // func symbol 依赖于 param symbol 和 func ret type
        node.params.forEach(this::visit);

        // resolveSymbol
        var paramSymbols = node.params.stream().map(p -> p.resolution).toList();
        var funcSymbol = new Symbol.Func(node.name, node.retType, paramSymbols);
        // 先 declare func symbol，再 visit func body 是为了防止递归时找不到 func symbol
        globalST.declareFunc(funcSymbol);
        node.resolution = funcSymbol;

        currentFunc = funcSymbol;
        visit(node.body);

        currentFunc = null;
        currentST = funcST.getParent();
    }

    @Override
    public void visit(Decl.VarDef node) {
        var isConst = node.isConst;

        node.dimensions.forEach(this::visit);

        var initExpr = node.init;
        visit(initExpr);

        resolveType(node);

        // resolveSymbol
        // symbol 对 initExpr 的 comptime value 有数据依赖
        var varSymbol = new Symbol.Var(node.kind, node.name, node.type, isConst);
        currentST.declare(varSymbol);
        node.resolution = varSymbol;

        // foldComptimeValue
        if(isConst) {
            if (initExpr == null) {
                err(node, "Const variable must be initialized");
                return;
            }

            if (!initExpr.isComptime) {
                err(node, "Const variable must be initialized with a const expression");
                return;
            }

            varSymbol.comptimeValue = initExpr.getComptimeValue();
        }
    }

    private void resolveType(Decl.VarDef node) {
        // type constructing
        var dimExprs = node.dimensions;
        var baseType = Type.Primitive.of(node.baseType);
        Type varType = baseType;
        // if array var, construct array type
        if (!dimExprs.isEmpty()) {
            int[] dims = dimExprs.stream().mapToInt(it -> {
                var v = it.getComptimeValue();
                if (v instanceof Integer intVal) return intVal;

                err(node, "Dimension not comptime constant");
                return 1; // gcc默认行为似乎是返回1
            }).toArray();
            varType = new Type.Array(baseType, dims);
        }
        node.type = varType;

        // type check
        var initExpr = node.init;
        if (initExpr == null) return;
        node.init = matchVarValueType(varType, initExpr);
    }

    private Expr matchVarValueType(Type varType, Expr valueExpr) {
        var valueType = valueExpr.type;
        if (varType.equals(valueType)) return valueExpr;

        if (!(valueExpr instanceof Expr.RawArray valueArrExpr)) {
            // int/float 互相可以隐式转换
            if (varType instanceof Type.Primitive) return new Expr.Cast(valueExpr, varType);
            err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
            return valueExpr;
        }

        if (!(varType instanceof Type.Array varArrType)) {
            err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
            return valueExpr;
        }

        var varElemType = varArrType.elementType;
        if (valueType instanceof Type.EmptyArray) {
            // 从接收该空数组值的变量类型推导出该空数组值的类型
            var originDims = varArrType.dimensions;
            var newDims = Arrays.copyOf(originDims, originDims.length);
            newDims[0] = 0;
            valueArrExpr.type = new Type.Array(varElemType, newDims);
        } else if (valueType instanceof Type.Primitive baseType) {
            if (varElemType == baseType) valueExpr.type = varType;
            else if (varElemType == Type.Primitive.FLOAT && baseType == Type.Primitive.INT) {
                valueArrExpr.type = varType;
                promoteElementType(valueArrExpr);
            } else err( valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
        } else err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);

        return valueExpr;
    }

    @Override
    public void visit(Stmt.Assign node) {
        super.visit(node);
        matchVarValueType(node.target.type, node.value);
    }

    @Override
    public void visit(Stmt.Block node) {
        var blockST = new SymbolTable.Local(currentST);
        currentST = blockST;
        node.symbolTable = blockST;

        super.visit(node);

        currentST = blockST.getParent();
    }

    @Override
    public void visit(Stmt.Return node) {
        super.visit(node);

        var retVal = node.value;
        var retType = currentFunc.retType;
        if (retType != Type.Void.INSTANCE) node.value = matchVarValueType(retType, node.value);
        else if (retVal != null) err(node, "Void function should not return value");
    }

    @Override
    public void visit(Expr.VarAccess node) {
        super.visit(node);

        // resolveSymbol
        var resolution = currentST.resolve(node.name);
        node.resolution = resolution;

        // resolveType
        node.type = resolution.type;

        // foldComptimeValue
        if(resolution.isConst) node.setComptimeValue(resolution.comptimeValue);
    }

    @Override
    public void visit(Expr.Call node) {
        super.visit(node);

        // resolveSymbol
        node.resolution = globalST.resolveFunc(node.funcName);

        // resolveType
        resolveType(node);

        // foldComptimeValue
        // 待定.
        // 纯函数编译时求值暂未实现，而且似乎等化为线性 IR 之后做 inline + const fold 也可以
    }

    private void resolveType(Expr.Call node) {
        var resolution = node.resolution;
        node.type = resolution.retType;

        var params = resolution.params;
        var args = node.args;
        var paramsCount = params.size();
        if(args.size() != paramsCount) err(node, "Argument count does not match");

        for (int i = 0; i < paramsCount; i++)
            args.set(i, matchVarValueType(params.get(i).type, args.get(i)));
    }

    @Override
    public void visit(Expr.IndexAccess node) {
        super.visit(node);

        // resolveType & foldComptimeValue
        var lhs = node.lhs;
        var lhsType = lhs.type;

        if(lhsType instanceof Type.Array arrType) node.type = arrType.getIndexElementType(node.indexes.size());
        else err(node, "Index access on non-array type");
    }

    /**
     * RawArray 此时只需标注 baseType，在 VarDef 或者 Assign 中确定具体 type
     * baseType 只可能是 EmptyArray, int 或 float
     */
    @Override
    public void visit(Expr.RawArray node) {
        super.visit(node);

        // resolveType
        var elements = node.elements;
        var elemCount = elements.size();
        if (elemCount == 0) {
            node.isComptime = true;
            node.type = Type.EmptyArray.INSTANCE;
            return;
        }

        var elemType = Type.Primitive.INT;
        boolean elemTypeWidened = false;
        for (var thisElem : elements) {
            var thisElemType = thisElem.type;
            switch (thisElemType) {
                case Type.EmptyArray _ -> { continue; }
                case Type.Array aType -> thisElemType = aType.elementType;
                default -> { }
            }

            if (thisElemType == Type.Primitive.FLOAT) {
                elemTypeWidened = true;
                elemType = Type.Primitive.FLOAT;
                break;
            }
        }

        if (elemTypeWidened) promoteElementType(node);

        node.type = elemType;

        boolean isComptime = true;
        for (var thisElem : elements) {
            if (!thisElem.isComptime) {
                isComptime = false;
                break;
            }
        }

        node.isComptime = isComptime;
    }

    private void promoteElementType(Expr.RawArray node) {
        var elements = node.elements;
        var elemCount = elements.size();
        for (var i = 0; i < elemCount; i++) {
            var thisElem = elements.get(i);

            if (thisElem instanceof Expr.RawArray arr) promoteElementType(arr);
            else if (thisElem.type == Type.Primitive.INT)
                elements.set(i, new Expr.Cast(thisElem, Type.Primitive.FLOAT));
        }
    }

    @Override
    public void visit(Expr.Unary node) {
        super.visit(node);

        resolveType(node);

        foldComptimeValue(node);
    }

    private void resolveType(Expr.Unary node) {
        var rhsType = node.rhs.type;
        node.type = switch (node.op) {
            case NOT -> {
                if (rhsType != Type.Primitive.INT)
                    err(node, "Not operator can only be applied to int (bool)");
                yield Type.Primitive.INT;
            }
            case ADD, SUB -> {
                if (rhsType != Type.Primitive.INT && rhsType != Type.Primitive.FLOAT)
                    err(node, "Unary operator can only be applied to number");
                yield rhsType;
            }
            default -> unreachable();
        };
    }

    private void foldComptimeValue(Expr.Unary node) {
        var rhs = node.rhs;
        if (!rhs.isComptime) return;

        var rhsValue = rhs.getComptimeValue();
        node.setComptimeValue(switch (node.op) {
            case NOT -> ((Integer) rhsValue) == 0 ? 1 : 0;
            case ADD -> rhsValue;
            case SUB -> switch (rhsValue) {
                case Integer intVal -> -intVal;
                case Float floatVal -> -floatVal;
                default -> unreachable();
            };
            default -> unreachable();
        });
    }

    @Override
    public void visit(Expr.Binary node) {
        super.visit(node);

        resolveType(node);

        foldComptimeValue(node);
    }

    private void resolveType(Expr.Binary node) {
        var lhs = node.lhs;
        var rhs = node.rhs;
        var lhsType = lhs.type;
        var rhsType = rhs.type;

        if (lhsType.equals(Type.Primitive.FLOAT) && rhsType.equals(Type.Primitive.INT)) {
            node.rhs = new Expr.Cast(rhs, Type.Primitive.FLOAT);
            rhsType = Type.Primitive.FLOAT;
        } else if (lhsType.equals(Type.Primitive.INT) && rhsType.equals(Type.Primitive.FLOAT)) {
            node.lhs = new Expr.Cast(lhs, Type.Primitive.FLOAT);
            lhsType = Type.Primitive.FLOAT;
        }

        if (!lhsType.equals(rhsType)) err(node, "Binary operator can only be applied to the same type");
        var type = lhsType;

        node.type = switch(node.op) {
            case ADD, SUB, MUL, DIV -> {
                if (type != Type.Primitive.INT && type != Type.Primitive.FLOAT)
                    err(node, "Arithmetic operator can only be applied to number");
                yield type;
            }
            case MOD -> {
                if (type != Type.Primitive.INT)
                    err(node, "Mod operator can only be applied to int");
                yield type;
            }
            case GT, GE, LT, LE -> {
                if (type != Type.Primitive.INT && type != Type.Primitive.FLOAT)
                    err(node, "Relational operator can only be applied to number");
                yield Type.Primitive.INT;
            }
            case EQ, NE -> Type.Primitive.INT;
            case AND, OR -> {
                if (type != Type.Primitive.INT)
                    err(node, "Binary operator can only be applied to int (bool)");
                yield Type.Primitive.INT;
            }
            default -> unreachable();
        };
    }

    private void foldComptimeValue(Expr.Binary node) {
        var lhs = node.lhs;
        var rhs = node.rhs;
        if (!(lhs.isComptime && rhs.isComptime)) return;

        // 由于在 resolveType 中已经保证了两侧类型一致，不用检查 lhs.type == rhs.type
        var type = lhs.type;
        var lhsValue = lhs.getComptimeValue();
        var rhsValue = rhs.getComptimeValue();

        if(type.equals(Type.Primitive.INT))
            node.setComptimeValue(calculate(node.op, (Integer) lhsValue, (Integer) rhsValue));
        else if(type.equals(Type.Primitive.FLOAT))
            node.setComptimeValue(calculate(node.op, (Float) lhsValue, (Float) rhsValue));
        else if (type instanceof Type.Array) {
            Placeholder.pass();
            // 在 C 语言中，两个变量具有相同的数组常量值，但是应该不会被分配到同一地址
            // 因此指针比较是不相等的，不应该做常量折叠？
        } else unreachable();
    }

    private static int calculate(Expr.Operator op, int left, int right) {
        return switch (op) {
            case ADD -> left + right;
            case SUB -> left - right;
            case MUL -> left * right;
            case DIV -> left / right;
            case MOD -> left % right;

            case GT -> left > right ? 1 : 0;
            case GE -> left >= right ? 1 : 0;
            case LT -> left < right ? 1 : 0;
            case LE -> left <= right ? 1 : 0;

            case EQ -> left == right ? 1 : 0;
            case NE -> left != right ? 1 : 0;

            case AND -> left != 0 && right != 0 ? 1 : 0;
            case OR -> left != 0 || right != 0 ? 1 : 0;
            default -> unreachable();
        };
    }

    private static float calculate(Expr.Operator op, float left, float right) {
        return switch (op) {
            case ADD -> left + right;
            case SUB -> left - right;
            case MUL -> left * right;
            case DIV -> left / right;

            case GT -> left > right ? 1 : 0;
            case GE -> left >= right ? 1 : 0;
            case LT -> left < right ? 1 : 0;
            case LE -> left <= right ? 1 : 0;

            case EQ -> left == right ? 1 : 0;
            case NE -> left != right ? 1 : 0;
            default -> unreachable();
        };
    }
}