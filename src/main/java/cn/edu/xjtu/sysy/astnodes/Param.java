package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

import cn.edu.xjtu.sysy.astvisitor.AstVisitor;

/** Function Parameters
 * 1.FuncFParam 定义一个函数的一个形式参数。当 Ident 后面的可选部分存在时，
表示数组定义。
2. 当 FuncFParam 为数组定义时，其第一维的长度省去（用方括号[ ]表示），而
后面的各维则需要用表达式指明长度，长度是整型常量。
3. 函数实参的语法是 Exp。对于 int/float 类型的参数，遵循按值传递；对于数
组类型的参数，则形参接收的是实参数组的地址，并通过地址间接访问实参
数组中的元素。
4. 对于多维数组，可以传递其中的一部分到形参数组中。例如，若 int a[4][3], 
则 a[1]是包含三个元素的一维数组，a[1]可以作为参数传递给类型为 int[]的
形参。
 */
public final class Param extends Node {

    public TypeAnnotation type;
    public Ident id;
    public List<Expr> dimensions;

    public Param(Token start, Token end, Ident id, TypeAnnotation type, List<Expr> dimensions) {
        super(start, end);
        this.dimensions = dimensions;
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Param [Location="
                + Arrays.toString(getLocation()) + "]";
    }

    public void accept(AstVisitor visitor) {
        visitor.visit(type);
        visitor.visit(id);
        for (Expr dim : dimensions) {
            visitor.visit(dim);
        }
    }
}
