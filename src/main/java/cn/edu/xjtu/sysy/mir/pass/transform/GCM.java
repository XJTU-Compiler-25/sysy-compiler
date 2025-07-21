package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// Global Code Motion
public final class GCM extends ModuleVisitor {
    public GCM() { }
    public GCM(ErrManager errManager) { super(errManager); }

}
