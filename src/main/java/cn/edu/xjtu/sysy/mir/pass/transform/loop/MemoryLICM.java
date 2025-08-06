package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;

// GCM 实现了除了 load 和 store 以外的 code motion
// MemoryLICM 专门提取循环中的 load store，其他的影响倒不一定大
public class MemoryLICM extends ModuleTransformer {


}
