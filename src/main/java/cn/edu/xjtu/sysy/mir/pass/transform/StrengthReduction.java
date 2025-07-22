package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

// 运算强度削减
public final class StrengthReduction extends AbstractTransform {
    public StrengthReduction(Pipeline<Module> pipeline) { super(pipeline); }

}
