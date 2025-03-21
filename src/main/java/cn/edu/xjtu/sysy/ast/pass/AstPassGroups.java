package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.PassGroup;
import cn.edu.xjtu.sysy.ast.node.CompUnit;

public final class AstPassGroups {

    private AstPassGroups() { }

    public static final PassGroup<CompUnit> GROUP = new PassGroup<>(
            new ExprValidator(),
            new ArrayNormalizer(),
            new AstAnnotator()
            // new TopLevelSemanticAnalyzer()
            // other Ast processing Passes
    );

}
