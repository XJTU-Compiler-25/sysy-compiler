package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;

// 负责将 while 翻译成 if-do-while
public final class LoopRotate extends ModuleVisitor {
    public LoopRotate(ErrManager errManager) {
        super(errManager);
    }


}
