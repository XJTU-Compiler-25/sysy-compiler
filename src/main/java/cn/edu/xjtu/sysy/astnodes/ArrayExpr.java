package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

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
public final class ArrayExpr extends Expr {

    /** 数组元素 */
    public List<Expr> elements;

    public ArrayExpr(Token start, Token end, List<Expr> elements) {
        super(start, end);
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "ArrayExpr [Location="
                + Arrays.toString(getLocation())
                + "]";
    }

    public void accept(AstVisitor visitor) {
        for (Expr element : elements) {
            visitor.visit(element);
        }
    }
}
