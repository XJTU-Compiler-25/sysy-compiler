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

import cn.edu.xjtu.sysy.symbol.Types;
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
        var _globalST = new SymbolTable.Global();

        currentST = _globalST;
        globalST = _globalST;
        node.globalST = _globalST;

        super.visit(node);
    }

    @Override
    public void visit(Decl.FuncDef node) {
        var funcST = new SymbolTable.Local(currentST);
        currentST = funcST;
        node.symbolTable = funcST;

        // resolveType
        var retTypeName = node.retTypeName;
        var retType = switch (retTypeName) {
            case "void" -> Types.Void;
            case "int" -> Types.Int;
            case "float" -> Types.Float;
            default -> {
                err("Unknown return type: " + retTypeName);
                yield null;
            }
        };

        // func symbol 依赖于 param symbol 和 func ret type
        var params = node.params;
        params.forEach(this::visitParam);

        var funcType = Types.function(retType, params.stream().map(it -> it.resolution.type).toArray(Type[]::new));

        // resolveSymbol
        var paramSymbols = params.stream().map(p -> p.resolution).toList();
        var funcSymbol = new Symbol.Func(node.name, funcType, paramSymbols);
        // 先 declare func symbol，再 visit func body 是为了防止递归时找不到 func symbol
        globalST.declareFunc(funcSymbol);
        node.resolution = funcSymbol;

        currentFunc = funcSymbol;
        visit(node.body);

        currentFunc = null;
        currentST = funcST.getParent();
    }

    public void visitParam(Decl.VarDef node) {
        var isConst = node.isConst;

        node.dimensions.forEach(this::visit);

        resolveType(node, true);

        // resolveSymbol
        // symbol 对 initExpr 的 comptime value 有数据依赖
        var varSymbol = new Symbol.Var(node.kind, node.name, node.type, isConst);
        currentST.declare(varSymbol);
        node.resolution = varSymbol;
    }

    @Override
    public void visit(Decl.VarDef node) {
        var isConst = node.isConst;

        node.dimensions.forEach(this::visit);

        var initExpr = node.init;
        visit(initExpr);

        resolveType(node, false);

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

    private void resolveType(Decl.VarDef node, boolean isFParam) {
        // type constructing
        var dimExprs = node.dimensions;
        var baseType = switch (node.baseType) {
            case "int" -> Types.Int;
            case "float" -> Types.Float;
            default -> {
                err(node, "Unknown base type: " + node.baseType);
                yield null;
            }
        };

        Type varType = baseType;
        // if array var, construct array type
        if (!dimExprs.isEmpty()) {
            int[] dims = dimExprs.stream().mapToInt(it -> {
                var v = it.getComptimeValue();
                if (v instanceof Integer intVal) return intVal;
                err(node, "Dimension not comptime constant");
                return 1; // gcc默认行为似乎是返回1
            }).toArray();
            for (int i = 1; i < dims.length; i++) {
                if (dims[i] < 0) {
                    err(node, "Dimension is negative");
                    dims[i] = 1;
                }
            }

            if (isFParam && dims[0] == -1) {
                varType = dims.length == 1 ? Types.ptrOf(baseType)
                        : Types.ptrOf(Types.arrayOf(baseType, Arrays.copyOfRange(dims, 1, dims.length)));
            } else varType = Types.arrayOf(baseType, dims);
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

        // int/float 互相可以隐式转换
        if (varType == Types.Int || varType == Types.Float) return new Expr.Cast(valueExpr, varType);

        if (varType instanceof Type.Pointer varPtrType) {
            if (valueType instanceof Type.Array valueArrType) {
                if (varPtrType.baseType.equals(valueArrType.getIndexElementType(1))) {
                    return valueExpr;
                } else err( valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
            } else err( valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
        }

        if (!(varType instanceof Type.Array varArrType && valueExpr instanceof Expr.RawArray valueArrExpr)) {
            err( valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
            return valueExpr;
        }

        var varElemType = varArrType.elementType;
        if (valueType == Types.Void) {
            // 从接收该空数组值的变量类型推导出该空数组值的类型
            var originDims = varArrType.dimensions;
            valueArrExpr.type = Types.arrayOf(varElemType, Arrays.copyOf(originDims, originDims.length));
        } else if (valueType instanceof Type.Scalar baseType) {
            if (varElemType == baseType) valueExpr.type = varType;
            else if (varElemType == Types.Float && baseType == Types.Int) {
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
        var retType = currentFunc.funcType.returnType;
        if (retType != Types.Void) node.value = matchVarValueType(retType, node.value);
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
        try {
            node.resolution = globalST.resolveFunc(node.funcName);
        } catch (Exception e) {
            err(node, e.getMessage());
            return;
        }


        // resolveType
        resolveType(node);

        // foldComptimeValue
        // 待定.
        // 纯函数编译时求值暂未实现，而且似乎等化为线性 IR 之后做 inline + const fold 也可以
    }

    private void resolveType(Expr.Call node) {
        var resolution = node.resolution;
        var funcType = resolution.funcType;
        node.type = funcType.returnType;

        var params = resolution.params;
        var args = node.args;
        var paramsCount = params.size();
        if(args.size() != paramsCount) err(node, "Argument count does not match");

        for (int i = 0; i < paramsCount; i++)
            args.set(i, matchVarValueType(funcType.paramTypes[i], args.get(i)));
    }

    @Override
    public void visit(Expr.IndexAccess node) {
        super.visit(node);

        // resolveType & foldComptimeValue
        var lhs = node.lhs;
        var lhsType = lhs.type;
        var indexSize = node.indexes.size();
        if(lhsType instanceof Type.Array arrType) {
            node.type = arrType.getIndexElementType(indexSize);
        } else if(lhsType instanceof Type.Pointer ptrType) {
            var baseType = ptrType.baseType;
            node.type = baseType instanceof Type.Array arrType ? arrType.getIndexElementType(indexSize - 1) : baseType;
        } else err(node, "Index access on non-array type: " + lhsType);
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
            node.type = Types.Void;
            return;
        }

        Type.Scalar elemType = Types.Int;
        boolean elemTypeWidened = false;
        for (var thisElem : elements) {
            var thisElemType = thisElem.type;
            switch (thisElemType) {
                case Type.Void _ -> { continue; }
                case Type.Array aType -> thisElemType = aType.elementType;
                default -> { }
            }

            if (thisElemType == Types.Float) {
                elemTypeWidened = true;
                elemType = Types.Float;
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
            else if (thisElem.type == Types.Int)
                elements.set(i, new Expr.Cast(thisElem, Types.Float));
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
                if (rhsType == Types.Float) {
                    node.rhs = new Expr.Cast(node.rhs, Types.Int);
                    rhsType = Types.Int;
                }
                if (rhsType != Types.Int)
                    err(node, "Not operator can only be applied to int (bool)");
                yield Types.Int;
            }
            case ADD, SUB -> {
                if (rhsType != Types.Int && rhsType != Types.Float)
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
            case NOT -> rhsValue.intValue() == 0 ? 1 : 0;
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
    public void process(Expr.Binary node) {
        resolveType(node);
        foldComptimeValue(node);
    }

    private void resolveType(Expr.Binary node) {
        var lhs = node.lhs;
        var rhs = node.rhs;
        var lhsType = lhs.type;
        var rhsType = rhs.type;

        if (lhsType.equals(Types.Float) && rhsType.equals(Types.Int)) {
            if (node.op == Expr.Operator.AND || node.op == Expr.Operator.OR) {
                node.lhs = new Expr.Cast(lhs, Types.Int);
                lhsType = Types.Int;
            } else {
                node.rhs = new Expr.Cast(rhs, Types.Float);
                rhsType = Types.Float;
            }
        } else if (lhsType.equals(Types.Int) && rhsType.equals(Types.Float)) {
            if (node.op == Expr.Operator.AND || node.op == Expr.Operator.OR) {
                node.rhs = new Expr.Cast(rhs, Types.Int);
                rhsType = Types.Int;
            } else {
                node.lhs = new Expr.Cast(lhs, Types.Float);
                lhsType = Types.Float;
            }
        }

        if (!lhsType.equals(rhsType)) err(node, "Binary operator can only be applied to the same type: %s != %s".formatted(lhsType, rhsType));
        var type = lhsType;

        node.type = switch(node.op) {
            case ADD, SUB, MUL, DIV -> {
                if (type != Types.Int && type != Types.Float)
                    err(node, "Arithmetic operator can only be applied to number: %s".formatted(type));
                yield type;
            }
            case MOD -> {
                if (type != Types.Int)
                    err(node, "Mod operator can only be applied to int: %s".formatted(type));
                yield type;
            }
            case GT, GE, LT, LE -> {
                if (type != Types.Int && type != Types.Float)
                    err(node, "Relational operator can only be applied to number: %s".formatted(type));
                yield Types.Int;
            }
            case EQ, NE -> Types.Int;
            case AND, OR -> {
                if (type != Types.Int)
                    err(node, "Binary operator can only be applied to int (bool): %s".formatted(type));
                yield Types.Int;
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

        if(type.equals(Types.Int))
            node.setComptimeValue(calculate(node.op, lhsValue.intValue(), rhsValue.intValue()));
        else if(type.equals(Types.Float))
            node.setComptimeValue(calculate(node.op, lhsValue.floatValue(), rhsValue.floatValue()));
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
            default -> unreachable(op.toString());
        };
    }
}
