import java.io.*;

import cn.edu.xjtu.sysy.mir.pass.SimpleCodegen;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import cn.edu.xjtu.sysy.ast.AstBuilder;
import cn.edu.xjtu.sysy.ast.AstPipelines;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.MirBuilder;
import cn.edu.xjtu.sysy.mir.pass.MirPipelines;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.util.Assertions;

/**
 * 根据大赛技术方案规定，编译器的主类名为 Compiler，不带包名。
 *
 * <p>原文摘录：
 *
 * <p>Java 平台与编译器：Oracle Java SE 17，主类名与编译选项： 主类名：Compiler.java （主类不带包名） 编译选项：javac -encoding utf-8
 *
 * <p>编译生成的学生编译器统一命名为compiler，参数如下： 功能测试：compiler testcase.sysy -S -o testcase.s 性能测试：compiler
 * testcase.sysy -S -o testcase.s -O1
 */
public class Compiler {

    public static void main(String[] args) throws IOException {

        String input = null, output = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].endsWith(".sy") || args[i].endsWith(".sysy")) {
                input = args[i];
            }
            if (args[i].equals("-o")) {
                output = args[i+1];
            }
        }

        Assertions.requires(input != null && output != null, "Not enough arguments");

        CharStream inputStream = CharStreams.fromFileName(input);
        SysYLexer lexer = new SysYLexer(inputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SysYParser parser = new SysYParser(tokenStream);

        ErrManager em = new ErrManager();
        SysYParser.CompUnitContext cst = parser.compUnit();
        AstBuilder astBuilder = new AstBuilder(em);
        CompUnit compUnit = astBuilder.visitCompUnit(cst);

        AstPipelines.DEFAULT.process(compUnit);
        em.printErrs();
        MirBuilder mirBuilder = new MirBuilder(em);
        Module mir = mirBuilder.build(compUnit);
        em.printErrs();

        var cgen = new SimpleCodegen();
        cgen.visit(mir);

        var riscVCode = cgen.output();
        File out = new File(output);
        if (out.exists()) out.delete();
        out.createNewFile();
        try (var outStream = new FileOutputStream(out)) {
            outStream.write(riscVCode.getBytes());
        }
    }

    public static CompUnit compileToAst(String s) {
        var cs = CharStreams.fromString(s);
        var lexer = new SysYLexer(cs);
        var tokens = new CommonTokenStream(lexer);
        var parser = new SysYParser(tokens);
        var cst = parser.compUnit();
        var ast = new AstBuilder().visitCompUnit(cst);
        return ast;
    }

    public static String compileToRiscV(CompUnit ast) {
        AstPipelines.DEFAULT.process(ast);

        var mirBuilder = new MirBuilder();
        var mir = mirBuilder.build(ast);
        MirPipelines.STACK.process(mir);

        var cgen = new SimpleCodegen();
        cgen.process(mir);
        return cgen.output();
    }

}
