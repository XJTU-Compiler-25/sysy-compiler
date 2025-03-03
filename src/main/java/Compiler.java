import cn.edu.xjtu.sysy.util.Assertions;

/**
 * 根据大赛技术方案规定，编译器的主类名为 Compiler，不带包名。

 * 原文摘录：

 * Java 平台与编译器：Oracle Java SE 17，主类名与编译选项：
 * 主类名：Compiler.java （主类不带包名）
 * 编译选项：javac -encoding utf-8

 * 编译生成的学生编译器统一命名为compiler，参数如下：
 * 功能测试：compiler testcase.sysy -S -o testcase.s
 * 性能测试：compiler testcase.sysy -S -o testcase.s-O1
 */
public class Compiler {
    public static void main(String[] args) {
        Assertions.check(args.length > 4, "Not enough arguments");

        String input = args[0];
        String output = args[3];


    }
}
