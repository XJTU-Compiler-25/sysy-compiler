import cn.edu.xjtu.sysy.ast.AstBuilder;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.pass.AstPassGroups;
import cn.edu.xjtu.sysy.ast.pass.AstPrettyPrinter;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.util.Exceptions;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class TestSolution {

    private AstPrettyPrinter app = new AstPrettyPrinter();

    private CompUnit compileToAst(String s) {
        var cs = CharStreams.fromString(s);
        var lexer = new SysYLexer(cs);
        var tokens = new CommonTokenStream(lexer);
        var parser = new SysYParser(tokens);
        var cst = parser.compUnit();
        var ast = new AstBuilder(null).visitCompUnit(cst);
        return ast;
    }

    @Test
    @Order(0)
    public void test() throws Exception {
        var testFileStream = this.getClass().getResourceAsStream("test.sy");
        var testCase = new String(testFileStream.readAllBytes());
        var ast = compileToAst(testCase);
        AstPassGroups.GROUP.process(ast);
        app.visit(ast);
        testFileStream.close();
    }

    @Test
    @Order(1)
    public void testFunctional() throws Exception {
        var testFileFolder = new File(this.getClass().getResource("functional").toURI());
        var testFiles = testFileFolder.listFiles();
        testFiles = Arrays.stream(testFiles)
                .filter(f -> f.getName().endsWith(".sy"))
                .sorted(Comparator.comparing(File::getName))
                .toArray(File[]::new);
        for (var testFile : testFiles) {
            var testFileStream = new FileInputStream(testFile);
            var testCase = new String(testFileStream.readAllBytes());
            var ast = compileToAst(testCase);
            AstPassGroups.GROUP.process(ast);
            app.visit(ast);
            testFileStream.close();
        }
    }

    @Test
    @Order(2)
    public void testPerformance() {

    }

}
