import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import cn.edu.xjtu.sysy.astnodes.CompUnit;
import cn.edu.xjtu.sysy.astnodes.SemanticError;
import cn.edu.xjtu.sysy.astvisitor.BuildAstVisitor;
import cn.edu.xjtu.sysy.astvisitor.TopLevelSemanticAnalyzer;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.parse.SysYParser.CompUnitContext;
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
    public static void main(String[] args) throws IOException {
        Assertions.requires(args.length > 4, "Not enough arguments");

        String input = args[0];
        String output = args[3];

        CharStream inputStream = CharStreams.fromFileName(input);
        SysYLexer lexer = new SysYLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SysYParser parser = new SysYParser(tokenStream);

        CompUnitContext cst = parser.compUnit();
        BuildAstVisitor buildAstVisitor = new BuildAstVisitor();
        CompUnit compUnit = buildAstVisitor.visitCompUnit(cst);
        if (buildAstVisitor.hasError()) {
            for (SemanticError error : buildAstVisitor.getErrors()) {
                System.out.println(error);
            }
            return;
        }

        TopLevelSemanticAnalyzer analyzer = new TopLevelSemanticAnalyzer();
        analyzer.visit(compUnit);

        if (analyzer.hasError()) {
            for (SemanticError error : analyzer.getErrors()) {
                System.err.println(error);
            }
            return;
        }
        System.out.println(compUnit.toString());
    }
}
