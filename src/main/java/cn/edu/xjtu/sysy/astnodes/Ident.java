package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

/** Identifiers
 * 同名标识符的约定：
全局变量和局部变量的作用域可以重叠，重叠部分局部变量优先；同名局
部变量的作用域不能重叠；
SysY 语言中变量名可以与函数名相同。
 */
public final class Ident extends AssignableExpr {

    public String name;

    public Ident(Token start, Token end, String name) {
        super(start, end);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ident [Location=" + Arrays.toString(getLocation()) + ", name="+ name +"]";
    }
}