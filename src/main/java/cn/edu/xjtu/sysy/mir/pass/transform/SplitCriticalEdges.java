package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;

// 分割关键边（多前导、多后继的块）
// 会给有关键边的块每个跳转来源都添加一个空前导块在中间
public final class SplitCriticalEdges extends ModuleTransformer {

    @Override
    public void visit(Function function) {
        super.visit(function);
    }

}
