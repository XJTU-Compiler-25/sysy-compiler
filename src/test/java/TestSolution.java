import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import cn.edu.xjtu.sysy.mir.MirBuilder;
import cn.edu.xjtu.sysy.mir.pass.MirPassGroups;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import cn.edu.xjtu.sysy.ast.AstBuilder;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.pass.AstPassGroups;
import cn.edu.xjtu.sysy.ast.pass.AstPrettyPrinter;
import cn.edu.xjtu.sysy.ast.pass.RiscVCGen;
import cn.edu.xjtu.sysy.ast.pass.StackCalculator;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.riscv.RiscVWriter;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class TestSolution {

    private final AstPrettyPrinter app = new AstPrettyPrinter();

    private CompUnit compileToAst(ErrManager em, String s) {
        var cs = CharStreams.fromString(s);
        var lexer = new SysYLexer(cs);
        var tokens = new CommonTokenStream(lexer);
        var parser = new SysYParser(tokens);
        var cst = parser.compUnit();
        var ast = new AstBuilder(null).visitCompUnit(cst);
        return ast;
    }

    private String CompileToRiscV(CompUnit ast) throws IOException {
        var calc = new StackCalculator();
        var writer = new RiscVWriter();
        var cgen = new RiscVCGen(writer);
        calc.visit(ast);
        cgen.visit(ast);
        writer.emitAll();
        return writer.toString();
    }

    private List<DynamicTest> genDynamicTest(String folder) throws Exception {
        var testFileFolder = new File(this.getClass().getResource(folder).toURI());
        var testFiles = testFileFolder.listFiles();
        return Arrays.stream(testFiles)
                .filter(f -> f.getName().endsWith(".sy"))
                .sorted(Comparator.comparing(File::getName))
                .map(f -> {
                    try {
                        var testFileStream = new FileInputStream(f);
                        var testCase = new String(testFileStream.readAllBytes());
                        testFileStream.close();
                        return dynamicTest(f.getName(), () -> {
                            var em = new ErrManager();
                            var ast = compileToAst(em, testCase);
                            AstPassGroups.makePassGroup(em).process(ast);
                            //app.visit(ast);
                            if (em.hasErr()) {
                                System.out.println(
                                        "Testing %s ..."
                                                .formatted(f.getName()));
                                em.printErrs();
                                Assertions.fail("Semantic Error");
                            } else {
                                System.out.println(
                                        "Testing %s ...\n Semantic Analysis Passed!"
                                                .formatted(f.getName()));
                            }

                            var mirBuilder = new MirBuilder();
                            var module = mirBuilder.build(ast);
                            MirPassGroups.makePassGroup(em).process(module);
                            //System.out.println(module.toString());

                            String riscVCode = CompileToRiscV(ast);
                            File out = new File(f.getParent(), f.getName() + ".s");
                            if (out.exists()) {
                                out.delete();
                            }
                            try (var output = new FileOutputStream(out)) {
                                output.write(riscVCode.getBytes());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @TestFactory
    @Order(0)
    public List<DynamicTest> testCustom() throws Exception {
        return genDynamicTest("custom");
    }

    @TestFactory
    @Order(1)
    public List<DynamicTest> testFunctional() throws Exception {
        return genDynamicTest("functional");
    }

    @TestFactory
    @Order(2)
    public List<DynamicTest> testHiddenFunctional() throws Exception {
        return genDynamicTest("hidden_functional");
    }

    @TestFactory
    @Order(3)
    public List<DynamicTest> testPerformance() throws Exception {
        return genDynamicTest("performance");
    }

    @TestFactory
    @Order(4)
    public List<DynamicTest> testFinalPerformance() throws Exception {
        return genDynamicTest("final_performance");
    }
}
