package cn.edu.xjtu.sysy.ast.pass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Stmt;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import cn.edu.xjtu.sysy.util.Placeholder;

/**
 * 标注类型信息、做类型检查、 标注符号表信息、 折叠编译期常量表达式
 *
 * <p>应当注意此时处理的数组仍然是 RawArray
 */
public final class AstAnnotator extends AstVisitor {
    public AstAnnotator(ErrManager errManager) {
        super(errManager);
    }

    private SymbolTable.Global globalST;
    private SymbolTable currentST;

    private Symbol.FuncSymbol currentFunc;

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
        var retType =
                switch (retTypeName) {
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

        var funcType =
                Types.function(
                        retType,
                        params.stream().map(it -> it.resolution.type).toArray(Type[]::new));

        // resolveSymbol
        var paramSymbols = params.stream().map(p -> p.resolution).toList();
        var funcSymbol = new Symbol.FuncSymbol(node.name, funcType, paramSymbols);
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
        var varSymbol = new Symbol.VarSymbol(node.name, false, node.type, isConst);
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

        // 全局变量必须是常量表达式
        if (currentST instanceof SymbolTable.Global) {
            if (initExpr != null && !initExpr.isComptime) {
                err(node, "Global variable must be initialized with a const expression");
            }
        }

        var varSymbol = new Symbol.VarSymbol(node.name, node.isGlobal, node.type, isConst);
        currentST.declare(varSymbol);
        node.resolution = varSymbol;

        // foldComptimeValue
        if (isConst) {
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
        var baseType =
                switch (node.baseType) {
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
            int[] dims =
                    dimExprs.reversed().stream()
                            .mapToInt(
                                    it -> {
                                        var v = it.getComptimeValue();
                                        if (v instanceof Integer intVal) return intVal;
                                        err(node, "Dimension not comptime constant");
                                        return 1; // gcc默认行为似乎是返回1
                                    })
                            .toArray();
            for (int i = 0; i < dims.length - 1; i++) {
                if (dims[i] < 0) {
                    err(node, "Dimension is negative");
                    dims[i] = 1;
                }
            }

            if (isFParam && dims[dims.length - 1] == -1) {
                varType =
                        dims.length == 1
                                ? Types.ptrOf(baseType)
                                : Types.ptrOf(
                                Types.arrayOf(
                                        baseType,
                                        Arrays.copyOfRange(dims, 0, dims.length - 1)));
            } else varType = Types.arrayOf(baseType, dims);
        }
        node.type = varType;

        // type check
        var initExpr = node.init;
        if (initExpr == null) return;
        node.init = matchVarValueType(varType, initExpr, false);
    }

    // 如果类似于 builtin getarray(int[]) 可以接收任意形状的 int[][][][] 等等，将 arrayTolerant 设为 true
    private Expr matchVarValueType(Type varType, Expr valueExpr, boolean arrayTolerant) {
        var valueType = valueExpr.type;
        if (varType.equals(valueType)) return valueExpr;

        // int/float 互相可以隐式转换
        if (valueType == Types.Float && varType == Types.Int)
            return Expr.Cast.float2Int(valueExpr);
        if (valueType == Types.Int && varType == Types.Float)
            return Expr.Cast.int2Float(valueExpr);

        if (varType instanceof Type.Pointer varPtrType) {
            if (valueType instanceof Type.Array valueArrType) {
                if (varPtrType.equals(Types.decay(valueArrType))) return Expr.Cast.decay(valueExpr);
                else if (arrayTolerant && varPtrType.baseType == valueArrType.elementType) return Expr.Cast.decayAll(valueExpr);
                else err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
            } else err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
        }

        if (!(varType instanceof Type.Array varArrType
                && valueExpr instanceof Expr.RawArray valueArrExpr)) {
            err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
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
            } else err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);
        } else err(valueExpr, "Value type: " + valueType + " not compatible with var type: " + varType);

        return valueExpr;
    }

    @Override
    public void visit(Stmt.Assign node) {
        super.visit(node);
        node.value = matchVarValueType(node.target.type, node.value, false);
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
        if (retType != Types.Void) node.value = matchVarValueType(retType, node.value, false);
        else if (retVal != null) err(node, "Void function should not return value");
    }

    @Override
    public void visit(Stmt.If node) {
        super.visit(node);
        var cond = node.cond;
        var type = cond.type;
        if (type != Types.Int) {
            if (type == Types.Float) {
                var newCond = new Expr.Binary(null, null, List.of(Expr.Operator.NE),
                        List.of(cond, new Expr.Literal(null, null, 0.0F)));
                newCond.type = Types.Int;
                node.cond = newCond;
            } else if (type instanceof Type.Array || type instanceof Type.Pointer)
                node.cond = Expr.Cast.ptr2bool(node.cond);
            else err(cond, "Condition expression must be of type int");
        }
    }

    @Override
    public void visit(Stmt.While node) {
        super.visit(node);
        var cond = node.cond;
        var type = cond.type;
        if (type != Types.Int) {
            if (type == Types.Float) {
                var newCond = new Expr.Binary(null, null, List.of(Expr.Operator.NE),
                        List.of(cond, new Expr.Literal(null, null, 0.0F)));
                newCond.type = Types.Int;
                node.cond = newCond;
            } else if (type instanceof Type.Array || type instanceof Type.Pointer)
                node.cond = Expr.Cast.ptr2bool(node.cond);
            else err(cond, "Condition expression must be of type int");
        }
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
        if (resolution.isConst) node.setComptimeValue(resolution.comptimeValue);
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
        if (args.size() != paramsCount) {
            err(node, "Argument count does not match");
            return;
        }

        var arrayTolerant = resolution.isExternal;
        for (int i = 0; i < paramsCount; i++)
            args.set(i, matchVarValueType(funcType.paramTypes[i], args.get(i), arrayTolerant));
    }

    @Override
    public void visit(Expr.IndexAccess node) {
        super.visit(node);

        // resolveType & foldComptimeValue
        var lhs = node.lhs;
        var lhsType = lhs.type;
        if (lhsType instanceof Type.Pointer ptrType) lhsType = Types.fixed(ptrType, 0);
        if (lhsType instanceof Type.Array arrType) node.type = arrType.getIndexElementType(node.indexes.size());
        else err(node, "Index access on non-array type: " + lhsType);
    }

    /**
     * RawArray 此时只需标注 baseType，在 VarDef 或者 Assign 中确定具体 type baseType 只可能是 EmptyArray, int 或 float
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
                case Type.Void _ -> {
                    continue;
                }
                case Type.Array aType -> thisElemType = aType.elementType;
                default -> {}
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
        for (var i = 0; i < elements.size(); i++) {
            var thisElem = elements.get(i);
            if (thisElem instanceof Expr.RawArray arr) promoteElementType(arr);
            else if (thisElem.type == Types.Int) elements.set(i, Expr.Cast.int2Float(thisElem));
        }
    }

    @Override
    public void visit(Expr.Unary node) {
        super.visit(node);

        resolveType(node);

        foldComptimeValue(node);
    }

    private void resolveType(Expr.Unary node) {
        var type = node.operand.type;
        for (var op : node.operators) {
            switch (op) {
                case NOT -> {
                    if (type != Types.Int && type != Types.Float)
                        err(node, "Unary operator can only be applied to number");
                    if (type == Types.Float) node.operand = Expr.Cast.float2Int(node.operand);
                    type = Types.Int;
                }
                case ADD, SUB -> {
                    if (type != Types.Int && type != Types.Float)
                        err(node, "Unary operator can only be applied to number");
                }
                default -> unreachable();
            }
        }
        node.type = type;
    }

    private void foldComptimeValue(Expr.Unary node) {
        var operand = node.operand;
        var ctv = operand.getComptimeValue();
        for (var op : node.operators) {
            if (!operand.isComptime) return;
            switch (op) {
                case ADD -> node.setComptimeValue(ctv);
                case SUB -> node.setComptimeValue(switch (ctv) {
                    case Integer intVal -> -intVal;
                    case Float floatVal -> -floatVal;
                    default -> unreachable();
                });
                case NOT -> {}
                default -> unreachable();
            }
        }

    }

    @Override
    public void visit(Expr.Binary node) {
        super.visit(node);

        resolveType(node);

        foldComptimeValue(node);
    }

    private void resolveType(Expr.Binary node) {
        var opers = node.operands;
        var ops = node.operators;
        var firstOp = ops.getFirst();
        switch (firstOp) {
            // 处理逻辑运算
            case AND, OR -> {
                node.type = Types.Int;
                for (int i = 0, size = opers.size(); i < size; i++) {
                    var operand = opers.get(i);
                    var type = operand.type;

                    if (type == Types.Int) Placeholder.pass();
                    else if (type == Types.Float)
                        opers.set(i, Expr.Cast.float2Int(operand));
                    else if (type instanceof Type.Array || type instanceof Type.Pointer)
                        opers.set(i, Expr.Cast.ptr2bool(operand));
                    else err(operand, "logical operator can't be applied to this type: " + type);
                }
            }
            case EQ, NE, GT, LT, GE, LE -> {
                node.type = Types.Int;
                /*
                 * 由于比较类表达式的返回值永远是 int，每碰到一个 float 类型的操作数，
                 * 就需要将前面所有操作数打包成新的 binary 然后套上 int2float
                 *
                 * 首先当第一个是 float ，第二个是 int 时，需要给第二个元素套上 int2float
                 * 后续 前面的表达式计算之和都是 int，当后面遇到 float 时，就打包
                 *
                 * 假设表达式是 0.0 != 0.0 != 0.0 != 0.0
                 * 应该转化为 (float) ((float) (0.0 != 0.0) != 0.0) != 0.0
                 */
                var newOps = new ArrayList<Expr.Operator>();
                var newOpers = new ArrayList<Expr>();
                var first = opers.getFirst();
                var firstTy = first.type;
                var second = opers.get(1);
                var secondTy = second.type;
                if (firstTy == Types.Float) {
                    if (secondTy == Types.Int) {
                        second = Expr.Cast.int2Float(second);
                        opers.set(1, second);
                    } else if (secondTy != Types.Float)
                        err(second, "This operator can't only be applied to this type: " + secondTy);
                } else if (firstTy == Types.Int) {
                    if (secondTy == Types.Float) {
                        first = Expr.Cast.int2Float(first);
                        opers.set(0, first);
                    } else if (secondTy != Types.Int)
                        err(second, "This operator can't only be applied to this type: " + secondTy);
                } else err(first, "This operator can't only be applied to this type: " + firstTy);
                newOpers.add(first);
                newOpers.add(second);
                newOps.add(firstOp);
                for (int i = 1, size = ops.size(); i < size; i++) {
                    var thisOp = ops.get(i);
                    var thisOper = opers.get(i + 1);
                    var thisTy = thisOper.type;

                    // 前面运算结果类型必定是 int，不用考虑将当前操作数转换为 float 的问题
                    // if (thisTy == Types.Int)

                    if (thisTy == Types.Float) {
                        var subExpr = new Expr.Binary(null, null, newOps, newOpers);
                        subExpr.type = Types.Int;
                        foldComptimeValue(subExpr);

                        var n = new ArrayList<Expr>();
                        n.add(Expr.Cast.int2Float(subExpr));
                        newOpers = n;
                        newOps = new ArrayList<>();

                        newOpers.add(thisOper);
                        newOps.add(thisOp);
                    } else if (thisTy == Types.Int) {
                        newOpers.add(thisOper);
                        newOps.add(thisOp);
                    } else err(thisOper, "This operator can't only be applied to this type: " + thisTy);

                    switch (thisOp) {
                        case GT, GE, LT, LE -> {
                            if (thisTy != Types.Int && thisTy != Types.Float)
                                err(thisOper, "Relational operator can only be applied to number: " + thisTy);
                        }
                        case EQ, NE -> { }
                        default -> unreachable();
                    }
                }
                node.operands = newOpers;
                node.operators = newOps;
            }
            default -> {
                /*
                 * 假设表达式是 1 + 1 + 1.0 + 1 + 1.0 + 1
                 * 应该转化为 (float) (1 + 1) + 1.0 + (float) 1 + 1.0 + (float) 1
                 * 则标定出 firstFloat, lastInt 后
                 * [binary 1 + 1 + 1.0 + 1 + 1.0 + 1] 转为
                 * [binary i2f[binary 1 + 1] + 1.0 + i2f[1] + 1.0 + i2f[1]]
                 *                           ^ firstFloat
                 */
                // 表达式整体的类型
                var type = opers.getFirst().type;
                var firstFloat = type == Types.Float ? 0 : -1;
                for (int i = 0, size = ops.size(); i < size; i++) {
                    var op = ops.get(i);
                    var thisOperand = opers.get(i + 1);
                    var thisTy = thisOperand.type;

                    if (thisTy == Types.Float) {
                        if (firstFloat == -1) firstFloat = i + 1;
                        if (type == Types.Int) type = Types.Float;
                    }

                    if (type == Types.Float && thisTy == Types.Int) {
                        thisOperand = Expr.Cast.int2Float(thisOperand);
                        thisTy = Types.Float;
                        opers.set(i + 1, thisOperand);
                    }

                    if (!thisTy.equals(type))
                        err(thisOperand, "Binary operator can only be applied to the same type: " + type + " != " + thisTy);

                    switch (op) {
                        case ADD, SUB, MUL, DIV -> {
                            if (thisTy != Types.Int && thisTy != Types.Float)
                                err(thisOperand, "Arithmetic operator can only be applied to number: " + thisTy);
                        }
                        case MOD -> {
                            if (thisTy != Types.Int)
                                err(thisOperand, "Mod operator can only be applied to int: " + thisTy);
                        }
                        default -> unreachable();
                    }
                }

                /*
                 * 表达式整体是 float，但前面有前缀的 int 操作数，将其整体拆分并打上 int2Float
                 * 例如： [binary 1 + 1 + 1.0 + 1 + 1.0 + 1]
                 *                        ^ firstFloat = 2
                 * 需要转为 [binary i2f[binary 1 + 1] + 1.0 + i2f[1] + 1.0 + i2f[1]]
                 * 大表达式拆出了 [0, firstFloat) 的操作数，[0, firstFloat - 1) 的操作符
                 * 保留了 [firstFloat, size) 的操作数和 [firstFloat - 1, size) 的操作符
                 */
                if (firstFloat != -1 && firstFloat != 0) {
                    // 只有第一个操作数是 int，不用拆 binary 出来
                    if (firstFloat == 1) opers.set(0, Expr.Cast.int2Float(opers.getFirst()));
                    else {
                        var subExprOpers = new ArrayList<>(opers.subList(0, firstFloat));
                        var subExprOps = new ArrayList<>(ops.subList(0, firstFloat - 1));
                        var subExpr = new Expr.Binary(null, null, subExprOps, subExprOpers);
                        subExpr.type = Types.Int;
                        foldComptimeValue(subExpr);

                        var remainOpers = new ArrayList<Expr>();
                        remainOpers.add(Expr.Cast.int2Float(subExpr));
                        remainOpers.addAll(opers.subList(firstFloat, opers.size()));
                        var remainOps = new ArrayList<>(ops.subList(firstFloat - 1, ops.size()));
                        node.operands = remainOpers;
                        node.operators = remainOps;
                    }
                }
                node.type = type;
            }
        }
    }

    private void foldComptimeValue(Expr.Binary node) {
        var opers = node.operands;
        var first = opers.getFirst();
        if (!first.isComptime) return;
        var ops = node.operators;
        var ctv = opers.getFirst().getComptimeValue();
        // 由于在 resolveType 中已经保证了所有操作数类型一致，不用检查每个操作数的类型了
        var type = node.type;

        for (int i = 0; i < ops.size(); i++) {
            var oper = opers.get(i + 1);
            if (!oper.isComptime) return;
            var op = ops.get(i);
            var thisCtv = oper.getComptimeValue();

            if (type == Types.Int) ctv = calculate(op, ctv.intValue(), thisCtv.intValue());
            else if (type == Types.Float) ctv = calculate(op, ctv.floatValue(), thisCtv.floatValue());
            else unreachable();
        }
        node.setComptimeValue(ctv);
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
            case AND -> left != 0 && right != 0 ? 1 : 0;
            case OR -> left != 0 || right != 0 ? 1 : 0;
            default -> unreachable(op.toString());
        };
    }

}
