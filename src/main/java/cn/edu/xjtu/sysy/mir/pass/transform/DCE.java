package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashSet;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

// dead code elimination
public class DCE extends ModuleVisitor {

    public DCE(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        removeUnusedInstructions(function);
        removeUnusedBlockArguments(function);
        removeUnusedLocalVars(function);
    }

    private static void removeUnusedInstructions(Function function) {
        var blocks = function.getTopoSortedBlocks();
        var reachable = new HashSet<Instruction>();
        for (var block : blocks) {
            var instrs = block.instructions;
            // 终结指令是可达的
            reachable.add(block.terminator);
            // 有副作用的指令是可达的
            for (var it : instrs) {
                if (it instanceof Store
                        || (it instanceof Call call && !call.getFunction().isPure)
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

    private static void removeUnusedBlockArguments(Function function) {
        function.getTopoSortedBlocks().forEach(DCE::removeUnusedBlockArguments);
    }

    private static void removeUnusedBlockArguments(BasicBlock block) {
        // 删除无用的 block argument
        for (var iter = block.args.entrySet().iterator(); iter.hasNext(); ) {
            var entry = iter.next();
            var var = entry.getKey();
            var blockArg = entry.getValue();
            if (!blockArg.notUsed()) continue;

            iter.remove();
            for (var term : block.predTerms) {
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

    private static void removeUnusedLocalVars(Function function) {
        function.localVars.removeIf(Value::notUsed);
    }

}
