package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;

import static cn.edu.xjtu.sysy.util.Assertions.todo;

public final class ConstFold extends ModuleVisitor {
    public ConstFold(ErrManager errManager) {
        super(errManager);
    }

    // 在折叠失败的时候返回 null
    public ImmediateValue fold(Expr expr) {
        return todo();
    }
}
