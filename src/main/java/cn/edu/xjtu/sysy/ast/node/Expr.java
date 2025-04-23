package cn.edu.xjtu.sysy.ast.node;

import java.util.List;

import org.antlr.v4.runtime.Token;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;

import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.Type;

/** Expressions */
public abstract sealed class Expr extends Node {
    /** 表达式的类型 */
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteMapNullValue)
    public Type type = null;
    private Number comptimeValue = null;
    public boolean isComptime = false;

    public Expr(Token start, Token end) {
        super(start, end);
    }

    public Expr(int[] location) {
        super(location);
    }

    public Number getComptimeValue() {
        return comptimeValue;
    }

    public void setComptimeValue(Number comptimeValue) {
        this.comptimeValue = comptimeValue;
        this.isComptime = true;
    }

    public enum Operator {
        ADD("+", true, false, false, false),
        SUB("-", true, false, false, false),
        MUL("*", true, false, false, false),
        DIV("/", true, false, false, false),
        MOD("%", true, false, false, false),

        EQ("==", false, true, false, false),
        NE("!=", false, true, false, false),

        GT(">", false, false, true, false),
        GE(">=", false, false, true, false),
        LT("<", false, false, true, false),
        LE("<=", false, false, true, false),
        AND("&&", false, false, false, true),
        OR("||", false, false, false, true),
        NOT("!", false, false, false, true),
        ;

        public final String text;
        public final boolean isArithmetic;
        public final boolean isEquality;
        public final boolean isLogical;
        public final boolean isRelational;

        Operator(String text, boolean isArithmetic, boolean isEquality, boolean isRelational, boolean isLogical) {
            this.text = text;
            this.isArithmetic = isArithmetic;
            this.isEquality = isEquality;
            this.isLogical = isLogical;
            this.isRelational = isRelational;
        }

        public static Operator of(String text) {
            return switch (text) {
                case "+" -> ADD;
                case "-" -> SUB;
                case "*" -> MUL;
                case "/" -> DIV;
                case "%" -> MOD;
                case "==" -> EQ;
                case "!=" -> NE;
                case ">" -> GT;
                case ">=" -> GE;
                case "<" -> LT;
                case "<=" -> LE;
                case "&&" -> AND;
                case "||" -> OR;
                case "!" -> NOT;
                default -> throw new IllegalArgumentException("Unknown operator: " + text);
            };
        }
    }

    /**
     * BinaryExpr 二元表达式 | cond op=('<' | '>' | '<=' | '>=') cond # relCond | cond op=('==' | '!=') cond
     * # eqCond | cond '&&' cond # andCond | cond '||' cond # orCond | exp op=('*' | '/' | '%') exp #
     * mulExp | exp op=('+' | '-') exp # addExp
     *
     * <p>1. Exp 在 SysY 中代表 int/float 型表达式，故它定义为 AddExp；Cond 代表条件 表达式，故它定义为
     * LOrExp。前者的单目运算符中不出现'!'，后者可以出现。 此外，当 Exp 作为数组维度时，必须是非负整数。 4. SysY 中算符的优先级与结合性与 C 语言一致，在上面的 SysY
     * 文法中已体现 出优先级与结合性的定义。
     */
    public static final class Binary extends Expr {
        /** 运算符 */
        public Operator op;
        /** 左运算元 */
        public Expr lhs;
        /** 右运算元 */
        public Expr rhs;

        public Binary(Token start, Token end, Expr lhs, Operator op, Expr rhs) {
            super(start, end);
            this.lhs = lhs;
            this.op = op;
            this.rhs = rhs;
        }
    }

    /** Unary Expressions */
    public static final class Unary extends Expr {
        /** 运算符 */
        public Operator op;
        /** 运算元 */
        public Expr rhs;

        public Unary(Token start, Token end, Operator op, Expr rhs) {
            super(start, end);
            this.rhs = rhs;
            this.op = op;
        }
    }

    /** AssignableExpr
    1. LVal 表示具有左值的表达式，可以为变量或者某个数组元素。
    2. 当 LVal 表示数组时，方括号个数必须和数组变量的维数相同（即定位到元
    素）。
    3. 当 LVal 表示单个变量时，不能出现后面的方括号。
    LVal 必须是当前作用域内、该 Exp 语句之前有定义的变量或常量；对于赋值
    号左边的 LVal 必须是变量。
    */
    public sealed abstract static class Assignable extends Expr {
        public Assignable(Token start, Token end) {
            super(start, end);
        }
    }

    /** Identifiers
     * 同名标识符的约定：
    全局变量和局部变量的作用域可以重叠，重叠部分局部变量优先；同名局
    部变量的作用域不能重叠；
    SysY 语言中变量名可以与函数名相同。
     */
    public static final class VarAccess extends Assignable {
        public String name;

        public Symbol.Var resolution;

        public VarAccess(Token start, Token end, String name) {
            super(start, end);
            this.name = name;
        }
    }

    /** Expressions */
    public static final class IndexAccess extends Assignable {
        /**
         * 在 SysY 中可求值得到数组的表达式只可能是取变量
         */
        public VarAccess lhs;
        // public Expr lhs;
        public List<Expr> indexes;

        public IndexAccess(Token start, Token end, VarAccess lhs, List<Expr> indexes) {
            super(start, end);
            this.lhs = lhs;
            this.indexes = indexes;
        }
    }

    /** Call Expressions
     * 函数调用形式是 Ident ‘(’ FuncRParams ‘)’，其中的 FuncRParams 表示实际参
    数。实际参数的类型和个数必须与 Ident 对应的函数定义的形参完全匹配。
     */
    public static final class Call extends Expr {
        public String funcName;
        public List<Expr> args;

        public Symbol.Func resolution;

        public Call(Token start, Token end, String funcName, List<Expr> args) {
            super(start, end);
            this.funcName = funcName;
            this.args = args;
        }
    }

    /** Literal */
    public static final class Literal extends Expr {
        public Literal(Token start, Token end, int value) {
            super(start, end);
            this.type = Type.Primitive.INT;
            this.setComptimeValue(value);
        }

        public Literal(Token start, Token end, float value) {
            super(start, end);
            this.type = Type.Primitive.FLOAT;
            this.setComptimeValue(value);
        }
    }

    /**
     * ArrayExpr ConstInitVal 初始化器必须是以下三种情况之一： a) 一对花括号 {}，表示所有元素初始为 0。 b)
     * 与多维数组中数组维数和各维长度完全对应的初始值，如{{1,2},{3,4}, {5,6}}、{1,2,3,4,5,6}、{1,2,{3,4},5,6}均可作为 a[3][2]的初始值。 c)
     * 如果花括号括起来的列表中的初始值少于数组中对应维的元素个数，则 该维其余部分将被隐式初始化，需要被隐式初始化的整型元素均初始为 0，如{{1, 2},{3},
     * {5}}、{1,2,{3},5}、{{},{3,4},5,6}均可作为 a[3][2]的初 始值，前两个将 a 初始化为{{1, 2},{3,0}, {5,0}}，{{},{3,4},5,6}将
     * a 初始 化为{{0,0},{3,4},{5,6}}。 d) 数组元素初值类型应与数组元素声明类型一致，例如整型数组初值列表 中不能出现浮点型元素；但是浮点型数组的初始化列表中可以出现整型
     * 常量或整型常量表达式； e) 数组元素初值大小不能超出对应元素数据类型的表示范围； f) 初始化列表中的元素个数不能超过数组声明时给出的总的元素个数。
     *
     * <p>当 VarDef 含有 ‘=’ 和初始值时， ‘=’ 右边的 InitVal 和 CostInitVal 的结构要 求相同，唯一的不同是 ConstInitVal 中的表达式是
     * ConstExp 常量表达式，而 InitVal 中的表达式可以是当前上下文合法的任何 Exp。
     */
    public static final class RawArray extends Expr {
        /**
         * 数组元素
         */
        public List<Expr> elements;

        public RawArray(Token start, Token end, List<Expr> elements) {
            super(start, end);
            this.elements = elements;
        }
    }
    
    /**
     *  标准化数组结点，仅储存所有显示初始化的元素及其位置信息
     *  eg. int a[3][3] = {1,2,3,{4,5},6} => {{1,2,3},{4,5,0},{6,0,0}}
     *      标准化之后: elements = [1, 2, 3, 4, 5, 6]
     *                 indexes = [0, 1, 2, 3, 4, 6]
     */
    public static final class Array extends Expr {
        /**
         * 数组元素
         */
        public List<Expr> elements;
        /**
         * 与以上元素一一对应的位置信息
         */
        public List<Integer> indexes;

        public Array(RawArray node, List<Expr> elements, List<Integer> indexes) {
            super(node.getLocation());
            this.type = node.type;
            this.elements = elements;
            this.indexes = indexes;
        }
        
        /**
         * 获取index位置的元素，注意index是否合法要在调用前进行检查
         * @param index - 返回的元素在展平后的数组的位置
         * @return - 在指定位置的元素
         */
        public Expr get(int index) {
            int i = indexes.indexOf(index);
            if (i == -1) {
                return new Literal(null, null, 0);
            }
            return elements.get(indexes.indexOf(index));
        }
    }

    /**
     * 隐式转换，在类型分析中被插入，AstBuilder 不需要生成这个节点
     * 便于直接遍历生成下一步 IR，不用在 expected type 是 int/float 时特判 actual type 是否是 float/int
     */
    public static final class Cast extends Expr {
        public Expr value;
        public Type fromType;
        public Type toType;

        public Cast(Expr value, Type toType) {
            this(null, null, value, toType);
        }

        public Cast(Token start, Token end, Expr value, Type toType) {
            super(start, end);
            this.type = toType;
            this.value = value;
            this.toType = toType;
            this.fromType = value.type;

            if (value.isComptime) {
                if (toType == Type.Primitive.INT) this.setComptimeValue(value.comptimeValue.intValue());
                else if (toType == Type.Primitive.FLOAT) this.setComptimeValue(value.comptimeValue.floatValue());
                else throw new RuntimeException("Invalid comptime value type: " + type);
            }
        }
    }
}
