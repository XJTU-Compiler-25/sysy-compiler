import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DynamicTest;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestMethodOrder;

import cn.edu.xjtu.sysy.ast.AstPipelines;
import cn.edu.xjtu.sysy.ast.pass.AstPrettyPrinter;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.MirBuilder;
import cn.edu.xjtu.sysy.mir.pass.Interpreter;
import cn.edu.xjtu.sysy.mir.pass.MirPipelines;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class TestSolution {

    private final AstPrettyPrinter app = new AstPrettyPrinter();

    private List<DynamicTest> genDynamicTest(String folder) throws Exception {
        var testFileFolder = new File(this.getClass().getResource(folder).toURI());
        var testFiles = testFileFolder.listFiles();
        return Arrays.stream(testFiles)
                .filter(f -> f.getName().endsWith(".sy"))
                .sorted(Comparator.comparing(File::getName))
                .map(f -> {
                    var testName = f.getName().substring(0, f.getName().length() - 3);
                    var testInFile = new File(testFileFolder, testName + ".in");
                    var testOutFile = new File(testFileFolder, testName + ".out");
                    try {
                        var testCodeStream = new FileInputStream(f);
                        var testInStream = testInFile.exists() ? new FileInputStream(testInFile) : null;
                        var testOutStream = testOutFile.exists() ? new FileInputStream(testOutFile) : null;
                        var testCode = new String(testCodeStream.readAllBytes());
                        var testIn = testInStream != null ? testInStream.readAllBytes() : null;
                        testCodeStream.close();
                        if (testInStream != null) testInStream.close();
                        if (testOutStream != null) testOutStream.close();
                        return dynamicTest(testName, () -> {
                            var em = ErrManager.GLOBAL;
                            var ast = Compiler.compileToAst(em, testCode);
                            AstPipelines.DEFAULT.process(ast);
                            //app.visit(ast);
                            if (em.hasErr()) {
                                System.out.println("Semantic Error on " + testName);
                                em.printErrs();
                            } else System.out.println("Semantic Analysis Passed on " + testName);

                            var mirBuilder = new MirBuilder();
                            var module = mirBuilder.build(ast);
                            //System.out.println(module);
                            MirPipelines.DEFAULT.process(module);
                            System.out.println(module);

                            if (true) {
                                System.out.println("Interpreting test...");
                                var is = new ByteArrayInputStream(testIn != null ? testIn : new byte[0]);
                                var os = new ByteArrayOutputStream();
                                var interpreter = new Interpreter(new PrintStream(os), is);
                                interpreter.process(module);
                                var out = os.toString();
                                System.out.println("Test output: \n" + out);
                                //Assertions.assertEquals(testOut, out);
                            }

                            var riscVCode = Compiler.CompileToRiscV(ast);
                            var out = new File(f.getParent(), f.getName() + ".s");
                            if (out.exists()) out.delete();
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
