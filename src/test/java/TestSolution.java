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
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.util.Exceptions;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class TestSolution {

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
    @Order(1)
    public void testFunctional() {
        try (var testFile = this.getClass().getResourceAsStream("sysy-testcases/functional/68_brainfk.sy")) {
            var testCase = new String(testFile.readAllBytes());
            var ast = compileToAst(testCase);
            AstPassGroups.GROUP.process(ast);
            var pp = new AstPrettyPrinter();
            pp.visit(ast);
        } catch (Exception e) {
            throw Exceptions.sneakyThrows(e);
        }
    }

    @Test
    @Order(2)
    public void testPerformance() {

    }

}
