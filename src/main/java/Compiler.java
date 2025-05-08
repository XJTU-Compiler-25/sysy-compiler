import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import cn.edu.xjtu.sysy.ast.AstBuilder;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.pass.AstPassGroups;
import cn.edu.xjtu.sysy.ast.pass.AstPrettyPrinter;
import cn.edu.xjtu.sysy.ast.pass.RiscVCGen;
import cn.edu.xjtu.sysy.ast.pass.StackCalculator;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.parse.SysYParser.CompUnitContext;
import cn.edu.xjtu.sysy.riscv.RiscVWriter;
import cn.edu.xjtu.sysy.util.Assertions;

/**
 * 根据大赛技术方案规定，编译器的主类名为 Compiler，不带包名。

 * 原文摘录：

 * Java 平台与编译器：Oracle Java SE 17，主类名与编译选项：
 * 主类名：Compiler.java （主类不带包名）
 * 编译选项：javac -encoding utf-8

 * 编译生成的学生编译器统一命名为compiler，参数如下：
 * 功能测试：compiler testcase.sysy -S -o testcase.s
 * 性能测试：compiler testcase.sysy -S -o testcase.s -O1
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

        ErrManager em = new ErrManager();
        CompUnitContext cst = parser.compUnit();
        AstBuilder astBuilder = new AstBuilder(em);
        CompUnit compUnit = astBuilder.visitCompUnit(cst);

        AstPassGroups.makePassGroup(em).process(compUnit);
        if (em.hasErr()) {
            em.printErrs();
            return;
        }
        var pp = new AstPrettyPrinter();
        pp.visit(compUnit);
        var calc = new StackCalculator();
        calc.visit(compUnit);
        var asm = new RiscVWriter();
        var riscv = new RiscVCGen(asm);
        riscv.visit(compUnit);
        
        System.out.println(asm.toString());
    }
}
