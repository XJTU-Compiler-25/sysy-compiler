package cn.edu.xjtu.sysy.ast.pass;

import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.error.ErrManager;

/**
 * todo
 * 把 Array 正规化为只有 单值 或 数组 元素
 */
public final class ArrayNormalizer extends AstVisitor {
    public ArrayNormalizer(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Expr.Array node) {
        super.visit(node);
    }
}
