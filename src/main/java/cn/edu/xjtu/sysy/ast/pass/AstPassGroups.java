package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.PassGroup;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.error.ErrManager;

public final class AstPassGroups {

    private AstPassGroups() { }

    public static PassGroup<CompUnit> makePassGroup(ErrManager em) {
        return new PassGroup<>(em,
                NotOpChecker::new,
                AstAnnotator::new,
                ArrayNormalizer::new,
                PureFunctionChecker::new
        );
    }

}
