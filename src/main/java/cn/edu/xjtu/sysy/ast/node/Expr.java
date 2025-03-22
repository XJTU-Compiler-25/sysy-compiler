package cn.edu.xjtu.sysy.ast.node;

import cn.edu.xjtu.sysy.symbol.Symbol;
import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.List;

/** Expressions */
public abstract sealed class Expr extends Node {
    /** 表达式的类型 */
    public Type type = null;
    /** 编译期常量值 */
    public ComptimeValue comptimeValue = null;

    public Expr(Token start, Token end) {
        super(start, end);
    }

    public void setType(Type type) {
        if (this.type != null) throw new IllegalArgumentException("Double infer");
        this.type = type;
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
    public abstract sealed static class Literal extends Expr {
        public Literal(Token start, Token end) {
            super(start, end);
        }
    }

    /** Integer Literal
     * SysY 语言的 int 和 float 类型的数有以下隐式类型转换：
    1、当 float 类型的值隐式转换为整型时，例如通过赋值 int i = 4.0; 小数部分将被
    丢弃；如果整数部分的值不在整型的表示范围，则其行为是未定义的；
    2、当 int 类型的值转换为 float 型时，例如通过赋值 float j = 3; 则转换后的值保
    持不变。
    注：编译器在实现隐式类型转换时，需要结合硬件体系结构提供的类型转换指令
    或运行时的 ABI（应用二进制接口）。例如，对于 ARM 架构，可以调用运行时
    ABI 函数 float __aeabi_i2f(int) 来将 int 转换为 float。
    参见 https://developer.arm.com/documentation/ihi0043/latest
     */
    public static final class IntLiteral extends Literal {
        public int value;

        public IntLiteral(Token start, Token end, int value) {
            super(start, end);
            this.value = value;
            this.type = Type.Primitive.INT;
            this.comptimeValue = new ComptimeValue.Float(value);
        }
    }

    /** FloatLiteral
     * SysY 语言的 int 和 float 类型的数有以下隐式类型转换：
    1、当 float 类型的值隐式转换为整型时，例如通过赋值 int i = 4.0; 小数部分将被
    丢弃；如果整数部分的值不在整型的表示范围，则其行为是未定义的；
    2、当 int 类型的值转换为 float 型时，例如通过赋值 float j = 3; 则转换后的值保
    持不变。
    注：编译器在实现隐式类型转换时，需要结合硬件体系结构提供的类型转换指令
    或运行时的 ABI（应用二进制接口）。例如，对于 ARM 架构，可以调用运行时
    ABI 函数 float __aeabi_i2f(int) 来将 int 转换为 float。
    参见 https://developer.arm.com/documentation/ihi0043/latest
     */
    public static final class FloatLiteral extends Literal {
        public float value;

        public FloatLiteral(Token start, Token end, float value) {
            super(start, end);
            this.value = value;
            this.type = Type.Primitive.FLOAT;
            this.comptimeValue = new ComptimeValue.Float(value);
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
    public static final class Array extends Expr {
        /**
         * 数组元素
         */
        public List<Expr> elements;

        /**
         * 整理过的数组元素，只有 单值 或 Array 元素
         */
        public List<Expr> normalizedElements;

        public Array(Token start, Token end, List<Expr> elements) {
            super(start, end);
            this.elements = elements;
        }
    }
}
