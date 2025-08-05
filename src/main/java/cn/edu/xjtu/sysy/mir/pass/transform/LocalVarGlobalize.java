package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;

// 把局部数组提升成全局变量
public final class LocalVarGlobalize extends AbstractTransform {
    public LocalVarGlobalize(Pipeline<Module> pipeline) { super(pipeline); }


}
