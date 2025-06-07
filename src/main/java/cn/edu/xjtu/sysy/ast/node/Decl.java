package cn.edu.xjtu.sysy.ast.node;

import cn.edu.xjtu.sysy.symbol.Symbol;
import cn.edu.xjtu.sysy.symbol.SymbolTable;
import cn.edu.xjtu.sysy.symbol.Type;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

/** 声明基类 */
public abstract sealed class Decl extends Node {

    public Decl(Token start, Token end) {
        super(start, end);
    }

    /** Function Definition
     * FuncDef 表示函数定义。其中的 FuncType 指明返回类型。
    a) 当返回类型为 int/float 时，函数内所有分支都应当含有带有 Exp 的 return
    语句。不含有 return 语句的分支的返回值未定义。
    b) 当返回值类型为 void 时，函数内只能出现不带返回值的 return 语句。
    2. FuncDef 中形参列表（FuncFParams）的每个形参声明（FuncFParam）用于声
    明 int/float 类型的参数，或者是元素类型为 int/float 的多维数组。FuncFParam
    的语义参见前文。
     */
    public static final class FuncDef extends Decl {
        public String name;
        public String retTypeName;
        public List<VarDef> params;
        public Stmt.Block body;

        public Type retType;
        public Symbol.Func resolution;
        public SymbolTable.Local symbolTable;
        public List<Symbol.Var> allVars;

        public FuncDef(Token start, Token end, String name, String retTypeName, List<VarDef> params, Stmt.Block body) {
            super(start, end);
            this.name = name;
            this.retTypeName = retTypeName;
            this.params = params;
            this.body = body;
        }
    }

    /** Single Variable Definition
     * 1. ConstDef 用于定义符号常量。ConstDef 中的 Ident 为常量的标识符，在 Ident
     后、‘=’之前是可选的数组维度和各维长度的定义部分，在 ‘=’ 之后是初始值。
     2. ConstDef 的数组维度和各维长度的定义部分不存在时，表示定义单个变量。
     此时 ‘=’ 右边必须是单个初始数值。
     3. ConstDef 的数组维度和各维长度的定义部分存在时，表示定义数组。其语义
     和 C 语言一致，比如[2][8/2][1*3]表示三维数组，第一到第三维长度分别为
     2、4、3，每维的下界从 0 编号。 ConstDef 中表示各维长度的 ConstExp 都
     必须能在编译时求值到非负整数。
     ISO/IEC 9899 <a href="http://www.open-std.org/jtc1/sc22/wg14/www/docs/n1124.pdf">...</a> 第 125 页
     6.7.8 节的第 6 点规定如下：
     注意：SysY 在声明数组时各维长度都需要显式给出，而不允许是未知的。
     4. 当 ConstDef 定义的是数组时，‘=’ 右边的 ConstInitVal 表示常量初始化器。
     ConstInitVal 中的 ConstExp 是能在编译时求值的 int/float 型表达式，其中可
     以引用已定义的符号常量。
     5. ConstInitVal 初始化器必须是以下三种情况之一：
     a) 一对花括号 {}，表示所有元素初始为 0。
     b) 与多维数组中数组维数和各维长度完全对应的初始值，如{{1,2},{3,4},
     {5,6}}、{1,2,3,4,5,6}、{1,2,{3,4},5,6}均可作为 a[3][2]的初始值。
     c) 如果花括号括起来的列表中的初始值少于数组中对应维的元素个数，则
     该维其余部分将被隐式初始化，需要被隐式初始化的整型元素均初始为
     0，如{{1, 2},{3}, {5}}、{1,2,{3},5}、{{},{3,4},5,6}均可作为 a[3][2]的初
     始值，前两个将 a 初始化为{{1, 2},{3,0}, {5,0}}，{{},{3,4},5,6}将 a 初始
     化为{{0,0},{3,4},{5,6}}。
     例如：下图中变量 a~e 的声明和初始化都是允许的。
     d) 数组元素初值类型应与数组元素声明类型一致，例如整型数组初值列表
     中不能出现浮点型元素；但是浮点型数组的初始化列表中可以出现整型
     常量或整型常量表达式；
     e) 数组元素初值大小不能超出对应元素数据类型的表示范围；
     f) 初始化列表中的元素个数不能超过数组声明时给出的总的元素个数。


     1. VarDef 用于定义变量。当不含有‘=’和初始值时，其运行时实际初值未定义。
     2. VarDef 的数组维度和各维长度的定义部分不存在时，表示定义单个变量。存
     在时，和 ConstDef 类似，表示定义多维数组。（参见 ConstDef 的第 2 点）
     3. 当 VarDef 含有 ‘=’ 和初始值时， ‘=’ 右边的 InitVal 和 CostInitVal 的结构要
     求相同，唯一的不同是 ConstInitVal 中的表达式是 ConstExp 常量表达式，而
     InitVal 中的表达式可以是当前上下文合法的任何 Exp。
     4. VarDef 中表示各维长度的 ConstExp 必须是能求值到非负整数，但 InitVal 中
     的初始值为 Exp，其中可以引用变量，例如下图中的变量 e 的初始化表达式
     d[2][1]。

     1. 全局变量声明中指定的初值表达式必须是常量表达式。
     2. 常量或变量声明中指定的初值要与该常量或变量的类型一致
     如下形式的 VarDef / ConstDef 不满足 SysY 语义约束：
     a[4] = 4
     a[2] = {{1,2}, 3}
     a = {1,2,3}
     3. 未显式初始化的局部变量，其值是不确定的；而未显式初始化的全局变量，
     其（元素）值均被初始化为 0 或 0.0。
     */
    public static final class VarDef extends Decl {
        public Symbol.Var.Kind kind;
        public String name;
        public String baseType;
        /**
         * dimensions 应该是 NotNull 的
         * Param 中具有空第一维的情况，[0] 会赋值为 -1，注意 type 构造中的处理
         */
        public List<Expr> dimensions;
        public boolean isConst;
        public Expr init;

        /**
         * 在 Annotating 中将 dimensions 求值后与 baseType 构建 Type
         */
        public Type type;
        public Symbol.Var resolution;

        public VarDef(
                Token start,
                Token end,
                Symbol.Var.Kind kind,
                String name,
                String baseType,
                boolean isConst,
                Expr init) {
            this(start, end, kind, name, baseType, List.of(), isConst, init);
        }

        public VarDef(
                Token start,
                Token end,
                Symbol.Var.Kind kind,
                String name,
                String baseType,
                List<Expr> dimensions,
                boolean isConst,
                Expr init) {
            super(start, end);
            this.kind = kind;
            this.name = name;
            this.baseType = baseType;
            this.dimensions = dimensions;
            this.isConst = isConst;
            this.init = init;
        }
    }
}
