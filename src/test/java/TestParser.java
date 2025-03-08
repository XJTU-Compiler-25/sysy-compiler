import cn.edu.xjtu.sysy.ast.Cst2AstVisitor;
import cn.edu.xjtu.sysy.parse.SysYLexer;
import cn.edu.xjtu.sysy.parse.SysYParser;

import org.antlr.v4.runtime.*;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestParser {

    public static InputStream testCase;
    public static CharStream charStream;
    public static SysYLexer lexer;
    public static TokenStream tokenStream;

    public static SysYParser parser;
    public static SysYParser.CompUnitContext astRoot;

    @BeforeAll
    public static void setup() throws IOException {
        testCase = TestParser.class.getResourceAsStream("test.c");
        Assertions.assertNotNull(testCase);
        charStream = CharStreams.fromStream(testCase);
        lexer = new SysYLexer(charStream);
        tokenStream = new CommonTokenStream(lexer);
    }

    @Test
    @Order(0)
    public void testParser() {
        parser = new SysYParser(tokenStream);
        astRoot = parser.compUnit();
        Assertions.assertNotNull(astRoot);
        System.out.println(astRoot.toStringTree(parser));
    }

    @Test
    @Order(1)
    public void testCst2Ast() {
        Cst2AstVisitor visitor = new Cst2AstVisitor();
        visitor.visit(astRoot);
    }

}
