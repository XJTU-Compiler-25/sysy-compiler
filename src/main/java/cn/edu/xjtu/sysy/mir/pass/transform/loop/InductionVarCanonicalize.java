package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;

/*
 * https://understanding-llvm-transformation-passes.readthedocs.io/en/latest/LLVM-transformation-passes/15-canonicalize-induction-variables.html
 * 所有的 loop 都会被 transform 只有一个 single induction variable，而且这个 induction variable 是 canonical 的（starting from 0）。
 * The transformed canonical induction variable 一定是 loop 的 head basic block 的 first PHI node。
 * 指针的递归操作会使用数组角标完成。
 */
public final class InductionVarCanonicalize extends ModuleTransformer {
}
