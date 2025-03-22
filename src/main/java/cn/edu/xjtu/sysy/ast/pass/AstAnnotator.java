package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.*;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.*;
import cn.edu.xjtu.sysy.util.Placeholder;

import java.util.Arrays;
import java.util.List;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

/**
 * 标注类型信息、做类型检查、
 * 标注符号表信息、
 * 折叠编译期常量表达式
 */
public final class AstAnnotator extends AstVisitor {
    public AstAnnotator(ErrManager errManager) {
        super(errManager);
    }

    private SymbolTable.Global globalST;
    private SymbolTable currentST;

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
        for (var p : node.params) visit(p);
        // resolveSymbol
        var paramSymbols = node.params.stream().map(p -> p.resolution).toList();
        var funcSymbol = new Symbol.Func(node.name, node.retType, paramSymbols);
        globalST.declareFunc(funcSymbol);
        node.resolution = funcSymbol;
        // 先 declare func symbol，再 visit func body 是为了防止递归时找不到 func symbol
        visit(node.body);

        currentST = funcST.getParent();
    }

    @Override
    public void visit(Decl.VarDef node) {
        for (var d : node.dimensions) visit(d);
        var isConst = node.isConst;
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
            if (!initExpr.isComptime())
                throw new IllegalArgumentException("Const variable must be initialized with a const expression");
            varSymbol.comptimeValue = initExpr.comptimeValue;
        }
    }

    private void resolveType(Decl.VarDef node) {
        // type constructing
        var dimExprs = node.dimensions;
        var baseType = Type.Primitive.of(node.baseType);
        Type varType = baseType;
        // if array var, construct array type
        if(dimExprs != null) {
            int[] dims = node.dimensions.stream().mapToInt(it -> {
                var v = it.comptimeValue;
                if (!(v instanceof ComptimeValue.Int intVal))
                    throw new IllegalArgumentException("Dimension not comptime constant");
                return intVal.value;
            }).toArray();
            varType = new Type.Array(baseType, dims);
        }
        node.type = varType;

        // type check
        var valueExpr = node.init;
        var valueType = valueExpr.type;
        if(!varType.equals(valueExpr.type)) {
            // int/float 互相可以隐式转换
            if (varType instanceof Type.Primitive && valueType instanceof Type.Primitive) {
                node.init = new Expr.Cast(valueExpr, varType);
            } else if (varType instanceof Type.Array aType && valueType instanceof Type.EmptyArray) {
                if (aType.isWildcard()) {
                    var originDims = aType.dimensions;
                    var newDims = Arrays.copyOf(originDims, originDims.length);
                    newDims[0] = 0;
                    valueExpr.type = new Type.Array(aType.elementType, newDims);
                } else valueExpr.type = varType; // 从接收该空数组值的变量类型推导出该空数组值的类型
            } else throw new IllegalArgumentException("Type of init value is not compatible with the type of variable");
        }
    }

    @Override
    public void visit(Stmt.Assign node) {
        super.visit(node);
        resolveType(node);
    }

    private void resolveType(Stmt.Assign node) {
        var varType = node.target.type;
        var valueExpr = node.value;
        var valueType = valueExpr.type;
        if(!varType.equals(valueExpr.type)) {
            // int/float 互相可以隐式转换
            if (varType instanceof Type.Primitive && valueType instanceof Type.Primitive) {
                node.value = new Expr.Cast(valueExpr, varType);
            } else if (varType instanceof Type.Array aType && valueType instanceof Type.EmptyArray) {
                if (aType.isWildcard()) {
                    var originDims = aType.dimensions;
                    var newDims = Arrays.copyOf(originDims, originDims.length);
                    newDims[0] = 0;
                    valueExpr.type = new Type.Array(aType.elementType, newDims);
                } else valueExpr.type = varType; // 从接收该空数组值的变量类型推导出该空数组值的类型
            } else throw new IllegalArgumentException("Type of value is not compatible with the type of variable");
        }
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
    public void visit(Expr.VarAccess node) {
        super.visit(node);
        // resolveSymbol
        var resolution = currentST.resolve(node.name);
        node.resolution = resolution;
        // resolveType
        node.type = resolution.type;
        // foldComptimeValue
        if(resolution.isConst) node.comptimeValue = resolution.comptimeValue;
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
        if(args.size() != paramsCount) throw new IllegalArgumentException("Argument count does not match");

        for (int i = 0; i < paramsCount; i++) {
            if(!params.get(i).type.equals(args.get(i).type))
                throw new IllegalArgumentException("Type of argument is not compatible with the type of parameter");
        }
    }

    @Override
    public void visit(Expr.IndexAccess node) {
        super.visit(node);
        // resolveType & foldComptimeValue
        var lhs = node.lhs;
        var lhsType = lhs.type;
        if(!(lhsType instanceof Type.Array arrType))
            throw new IllegalArgumentException("Index access on non-array type");
        var depth = node.indexes.size();
        node.type = arrType.getIndexElementType(depth);

        if(!lhs.isComptime()) return;
        var comptimeValue = lhs.comptimeValue;
        List<Expr> indexes = node.indexes;
        for (int i = 0, indexesSize = indexes.size(); i < indexesSize; i++) {
            var index = indexes.get(i);
            var indexType = index.type;
            if (!indexType.equals(Type.Primitive.INT)) {
                if (indexType.equals(Type.Primitive.FLOAT)) indexes.set(i, new Expr.Cast(index, Type.Primitive.INT));
                else throw new IllegalArgumentException("Index must be an number");
            }

            if (comptimeValue != null) {
                if (index.isComptime()) comptimeValue =
                        ((ComptimeValue.Array) comptimeValue).values[((ComptimeValue.Int) index.comptimeValue).value];
                else comptimeValue = null;
            }
        }
        if(comptimeValue != null) node.comptimeValue = comptimeValue;
    }

    @Override
    public void visit(Expr.Array node) {
        super.visit(node);
        // resolveType & foldComptimeValue
        var elements = node.elements;
        var elemCount = elements.size();
        if (elemCount == 0) {
            node.type = Type.EmptyArray.INSTANCE;
            return;
        }
        var elemType = elements.get(0).type;
        var comptimeValues = new ComptimeValue[elemCount];
        for (var i = 0; i < elemCount; i++) {
            var thisElem = elements.get(i);
            var thisElemType = thisElem.type;
            if (!elemType.equals(thisElemType)) {
                // int 可以在 float[] 中，但 float 不能在 int[] 中
                if (elemType.equals(Type.Primitive.INT) && thisElemType.equals(Type.Primitive.FLOAT))
                    elemType = Type.Primitive.FLOAT;
                else if (elemType.equals(Type.Primitive.FLOAT) && thisElemType.equals(Type.Primitive.INT))
                    elements.set(i, new Expr.Cast(thisElem, Type.Primitive.FLOAT));
                else throw new IllegalArgumentException("Type of element is not compatible with the type of array");
            }

            if (comptimeValues != null) {
                if (thisElem.isComptime()) comptimeValues[i] = thisElem.comptimeValue;
                else comptimeValues = null;
            }
        }
        if(comptimeValues != null) node.comptimeValue = new ComptimeValue.Array(comptimeValues);
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
                if (!rhsType.equals(Type.Primitive.INT))
                    throw new IllegalArgumentException("Not operator can only be applied to int (bool)");
                yield Type.Primitive.INT;
            }
            case ADD, SUB -> {
                if (!(rhsType.equals(Type.Primitive.INT) || rhsType.equals(Type.Primitive.FLOAT)))
                    throw new IllegalArgumentException("Unary operator can only be applied to number");
                yield rhsType;
            }
            default -> unreachable();
        };
    }

    private void foldComptimeValue(Expr.Unary node) {
        var rhs = node.rhs;
        if (!rhs.isComptime()) return;

        var rhsValue = rhs.comptimeValue;
        node.comptimeValue = switch (node.op) {
            case NOT -> new ComptimeValue.Int(((ComptimeValue.Int) rhsValue).value == 0 ? 1 : 0);
            case ADD -> rhsValue;
            case SUB -> {
                if (rhsValue instanceof ComptimeValue.Int intVal)
                    yield new ComptimeValue.Int(-intVal.value);
                else if (rhsValue instanceof ComptimeValue.Float floatVal)
                    yield new ComptimeValue.Float(-floatVal.value);
                else yield unreachable();
            }
            default -> unreachable();
        };
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

        if (!lhsType.equals(rhsType))
            throw new IllegalArgumentException("Binary operator can only be applied to the same type");
        var type = lhsType;

        node.type = switch(node.op) {
            case ADD, SUB, MUL, DIV -> {
                if (!(type == Type.Primitive.INT || type == Type.Primitive.FLOAT))
                    throw new IllegalArgumentException("Arithmetic operator can only be applied to number");
                yield type;
            }
            case MOD -> {
                if (!(type == Type.Primitive.INT))
                    throw new IllegalArgumentException("Mod operator can only be applied to int");
                yield type;
            }
            case GT, GE, LT, LE -> {
                if (!(type == Type.Primitive.INT || type == Type.Primitive.FLOAT))
                    throw new IllegalArgumentException("Relational operator can only be applied to number");
                yield Type.Primitive.INT;
            }
            case EQ, NE -> Type.Primitive.INT;
            case AND, OR -> {
                if (type.equals(Type.Primitive.INT))
                    throw new IllegalArgumentException("Binary operator can only be applied to int (bool)");
                yield Type.Primitive.INT;
            }
            default -> unreachable();
        };
    }

    private void foldComptimeValue(Expr.Binary node) {
        var lhs = node.lhs;
        var rhs = node.rhs;
        if (!(lhs.isComptime() && rhs.isComptime())) return;

        // 由于在 resolveType 中已经保证了两侧类型一致，不用检查 lhs.type == rhs.type
        var type = lhs.type;
        var lhsValue = lhs.comptimeValue;
        var rhsValue = rhs.comptimeValue;

        if(type.equals(Type.Primitive.INT)) node.comptimeValue = new ComptimeValue.Int(calculate(node.op,
                ((ComptimeValue.Int) lhsValue).value, ((ComptimeValue.Int) rhsValue).value));
        else if(type.equals(Type.Primitive.FLOAT)) node.comptimeValue = new ComptimeValue.Float(calculate(node.op,
                ((ComptimeValue.Float) lhsValue).value, ((ComptimeValue.Float) rhsValue).value));
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
