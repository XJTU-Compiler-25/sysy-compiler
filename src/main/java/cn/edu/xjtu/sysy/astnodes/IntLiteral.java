package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;

import org.antlr.v4.runtime.Token;

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
public final class IntLiteral extends Literal {

    public int value;

    public IntLiteral(Token start, Token end, int value) {
        super(start, end);
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntLiteral [value=" + value + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }
}
