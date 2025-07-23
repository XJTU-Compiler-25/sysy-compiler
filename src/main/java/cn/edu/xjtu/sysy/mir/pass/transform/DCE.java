package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.PurenessAnalysis;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashSet;
import java.util.Iterator;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

// dead code elimination
public class DCE extends AbstractTransform {
    public DCE(Pipeline<Module> pipeline) { super(pipeline); }

    private CFGAnalysis.Result cfg;
    private PurenessAnalysis.Result pureRes;

    @Override
    public void visit(Module module) {
        cfg = getCFG();
        pureRes = getResult(PurenessAnalysis.class);
        super.visit(module);
    }

    @Override
    public void visit(Function function) {
        removeUnusedInstructions(function);
        removeUnreachableBlocks(function);
        removeUnusedBlockArguments(function);
        removeUnusedLocalVars(function);
    }

    private void removeUnusedInstructions(Function function) {
        var blocks = getCFG().getRPOBlocks(function);
        var reachable = new HashSet<Instruction>();
        for (var block : blocks) {
            var instrs = block.instructions;
            // 终结指令是可达的
            reachable.add(block.terminator);
            // 有副作用的指令是可达的
            for (var it : instrs) {
                if (it instanceof Store
                        || (it instanceof Call call && !pureRes.isPure(call.getFunction()))
                        || it instanceof CallExternal)
                    reachable.add(it);
            }

            var worklist = new Worklist<>(reachable);

            while (!worklist.isEmpty()) {
                var inst = worklist.poll();
                for (var use : inst.used) {
                    if (use.value instanceof Instruction instr) {
                        reachable.add(instr);
                        worklist.add(instr);
                    }
                }
            }
        }

        for (var block : blocks) {
            for (var iter = block.instructions.iterator(); iter.hasNext(); ) {
                var inst = iter.next();
                if (!reachable.contains(inst)) {
                    iter.remove();
                    inst.dispose();
                }
            }
        }
    }

    private void removeUnreachableBlocks(Function function) {
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        for (var iterator = function.blocks.iterator(); iterator.hasNext(); ) {
            var block = iterator.next();
            if (!reachable.contains(block)) {
                iterator.remove();
                block.dispose();
            }
        }
    }

    private void dfs(BasicBlock block, HashSet<BasicBlock> visited) {
        for (var succ : cfg.getSuccBlocksOf(block)) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }

    private void removeUnusedBlockArguments(Function function) {
        getCFG().getRPOBlocks(function).forEach(this::removeUnusedBlockArguments);
    }

    private void removeUnusedBlockArguments(BasicBlock block) {
        // 删除无用的 block argument
        for (var iter = block.args.entrySet().iterator(); iter.hasNext(); ) {
            var entry = iter.next();
            var var = entry.getKey();
            var blockArg = entry.getValue();
            if (!blockArg.notUsed()) continue;

            iter.remove();
            for (var term : cfg.getPredTermsOf(block)) {
                switch (term) {
                    case Br it -> {
                        if (it.getTrueTarget() == block) {
                            var tp = it.trueParams.remove(var);
                            if (tp != null) tp.dispose();
                        }
                        if (it.getFalseTarget() == block) {
                            var fp = it.falseParams.remove(var);
                            if (fp != null) fp.dispose();
                        }
                    }
                    case Jmp it -> {
                        var p = it.params.remove(var);
                        if (p != null) p.dispose();
                    }
                    default -> unreachable();
                }
            }
        }
    }

    private void removeUnusedLocalVars(Function function) {
        function.localVars.removeIf(Value::notUsed);
    }

}
