package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 负责将 while 翻译成 if-do-while
public final class LoopRotate extends ModuleVisitor {
    public LoopRotate(ErrManager errManager) {
        super(errManager);
    }


}
