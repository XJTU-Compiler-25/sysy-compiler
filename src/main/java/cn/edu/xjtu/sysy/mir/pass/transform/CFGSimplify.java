package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;

import java.util.*;

public final class CFGSimplify extends AbstractTransform {
    public CFGSimplify(Pipeline<Module> pipeline) { super(pipeline); }

    private CFGAnalysis.Result cfg;
    @Override
    public void visit(Module module) {
        cfg = getCFG();
        for (var function : module.getFunctions()) removeEmptyBlocks(function);
        invalidateResult(CFGAnalysis.class);
    }

    private void removeEmptyBlocks(Function function) {
        // 由于删除一个块可能会使别的块也变得可删除，可能需要多次运行
        boolean changed = true;
        while(changed) {
            changed = false;
            for (var block : cfg.getRPOBlocks(function)) {
                // 不删入口块
                if (block == function.entry) continue;
                // 单后继才能删除
                if (!(block.terminator instanceof Instruction.Jmp jmp)) continue;
                if (!block.instructions.isEmpty()) continue;

                var predTerms = cfg.getPredTermsOf(block);
                var succ = jmp.getTarget();
                // 删除自环可能会改变程序语义，不删
                if (succ != block
                        // 没有 phi，一定可以删除，有 phi 而只有单个前驱，也可以删除
                        && (block.args.isEmpty()
                        || predTerms.size() == 1)) {
                    function.removeBlock(block);
                    for (var term : predTerms) {
                        jmp.params.forEach((var, use) -> {
                            // 如果这个值是直接从参数里接到，直接传给后继，则不用 put
                            // 只需要将本块中产生的新值重写到前导跳转上
                            if (!(use.value instanceof BlockArgument arg && arg.block == block))
                                term.putParam(block, var, use.value);
                        });
                        term.replaceTarget(block, succ);
                    }

                    // 将本块的参数都换成后继块的参数
                    block.args.forEach((var, arg) -> {
                        var succArg = succ.getBlockArgument(var);
                        if (succArg == null) succArg = succ.addBlockArgument(var);
                        arg.replaceAllUsesWith(succArg);
                    });
                    // 清空 block 的所有 use
                    block.dispose();
                }
            }
        }
    }
}
