package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// GCM 全局代码移动
public final class GlobalCodeMotion extends ModuleVisitor {
    public GlobalCodeMotion(ErrManager errManager) {
        super(errManager);
    }

}
