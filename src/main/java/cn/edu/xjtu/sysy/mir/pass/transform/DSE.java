package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;

// Dead Store Elimination 死存储消除，需要先做指针分析
public final class DSE extends AbstractTransform {
    public DSE(Pipeline<Module> pipeline) { super(pipeline); }

}
