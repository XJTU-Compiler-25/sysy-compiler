import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import cn.edu.xjtu.sysy.ast.AstBuilder;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.pass.AstPassGroups;
import cn.edu.xjtu.sysy.ast.pass.AstPrettyPrinter;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class TestSolution {

    private final AstPrettyPrinter app = new AstPrettyPrinter();

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
        Files.walk(Paths.get("src/test/resources", new String[0]), new FileVisitOption[0]).filter(path -> {
            return path.toString().endsWith(".sy");
        }).forEach((var path2) -> {
            System.err.println("testing %s...".formatted(path2.toString()));
            try {
                var testCase = new String(Files.readAllBytes(path2));
                var ast = compileToAst(testCase);
                AstPassGroups.GROUP.process(ast);
                //app.visit(ast);
        
                ErrManager.GLOBAL.printErrs(); 
            } catch (IOException e) {
                System.err.println(e);
            }
        });
    }

    /*
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
    */

}
