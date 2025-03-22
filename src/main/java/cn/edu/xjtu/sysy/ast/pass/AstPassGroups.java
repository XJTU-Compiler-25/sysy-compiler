package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.PassGroup;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.error.ErrManager;

public final class AstPassGroups {

    private AstPassGroups() { }

    public static final PassGroup<CompUnit> GROUP = new PassGroup<>(ErrManager.GLOBAL,
            NotOpChecker::new,
            ArrayNormalizer::new,
            AstAnnotator::new,
            PureFunctionChecker::new
    );

}
