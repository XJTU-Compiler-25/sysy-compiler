import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import cn.edu.xjtu.sysy.ast.AstBuilder;
import cn.edu.xjtu.sysy.ast.AstPipelines;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.pass.AstPrettyPrinter;
import cn.edu.xjtu.sysy.ast.pass.RiscVCGen;
import cn.edu.xjtu.sysy.ast.pass.StackCalculator;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.MirBuilder;
import cn.edu.xjtu.sysy.mir.pass.AsmCGen;
import cn.edu.xjtu.sysy.mir.pass.Interpreter;
import cn.edu.xjtu.sysy.mir.pass.MirPipelines;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.riscv.RiscVWriter;
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
        Assertions.requires(args.length > 4, "Not enough arguments");

        var input = args[0];
        var output = args[3];

        var em = ErrManager.GLOBAL;
        var testCodeStream = new FileInputStream(input);
        var testCode = new String(testCodeStream.readAllBytes());
        var compUnit = compileToAst(em, testCode);
        AstPipelines.DEFAULT.process(compUnit);
        if (em.hasErr()) {
            em.printErrs();
            return;
        }
        var pp = new AstPrettyPrinter();
        //pp.visit(compUnit);

        var mirBuilder = new MirBuilder(em);
        var mir = mirBuilder.build(compUnit);
        //System.out.println(mir);
        MirPipelines.DEFAULT.process(mir);
        if (em.hasErr()) {
            em.printErrs();
            return;
        }
        System.out.println(mir);
        if (false) {
             System.out.println("Interpreting test...");
            var testInFile = new File(input.substring(0, input.length() - 3) + ".in");
            var testInStream = testInFile.exists() ? new FileInputStream(testInFile) : null;
            var is = new ByteArrayInputStream(testInStream != null ? testInStream.readAllBytes() : new byte[0]);
            if (testInStream != null) testInStream.close();
            var os = new ByteArrayOutputStream();
            var interpreter = new Interpreter(new PrintStream(os), is);
            interpreter.process(mir);
            var out = os.toString();
            System.out.println("Test output: \n" + out);
        }

        var cgen = new AsmCGen();
        cgen.process(mir);
        System.out.println(cgen.toString());
        /* 
        var calc = new StackCalculator();
        calc.visit(compUnit);
        var asm = new RiscVWriter();
        var cgen = new RiscVCGen(asm);
        cgen.visit(compUnit);
        
        asm.emitAll();
        var riscVCode = asm.toString();
        //System.out.println(riscVCode);
        File out = new File(output);
        if (out.exists()) {
            out.delete();
        }
        try (var outStream = new FileOutputStream(out)) {
            outStream.write(riscVCode.getBytes());
            outStream.close();
        }
        */
    }

    public static CompUnit compileToAst(ErrManager em, String s) {
        var cs = CharStreams.fromString(s);
        var lexer = new SysYLexer(cs);
        var tokens = new CommonTokenStream(lexer);
        var parser = new SysYParser(tokens);
        var cst = parser.compUnit();
        var ast = new AstBuilder().visitCompUnit(cst);
        return ast;
    }

    public static String CompileToRiscV(CompUnit ast) {
        var calc = new StackCalculator();
        var writer = new RiscVWriter();
        var cgen = new RiscVCGen(writer);
        calc.visit(ast);
        cgen.visit(ast);
        writer.emitAll();
        return writer.toString();
    }

}
