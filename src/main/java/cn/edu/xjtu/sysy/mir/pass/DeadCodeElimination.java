package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public class DeadCodeElimination extends ModuleVisitor {

    public DeadCodeElimination(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        // 删除未使用的指令
        removeUnusedInstructions(function);
        // 简化控制流，以便后续能识别出更多的死代码
        new CFGSimplify(errManager).visit(function);
        // 删除未使用的块参数，需要在删除空基本块后运行，否则可能在跳转关系变化后漏删
        removeUnusedBlockArguments(function);
        // 删除无用的局部变量
        removeUnusedLocalVars(function);
    }

    private static void removeUnusedInstructions(Function function) {
        function.blocks.forEach(DeadCodeElimination::removeUnusedInstructions);
    }

    private static void removeUnusedInstructions(BasicBlock block) {
        var instrs = block.instructions;
        var reachable = new HashSet<Instruction>();
        // 终结指令是可达的
        reachable.add(block.terminator);
        // 有副作用的指令是可达的
        for (var it : instrs) {
            if (it instanceof Store || (it instanceof Call call && !call.function.isPure)
                    || it instanceof CallExternal) reachable.add(it);
        }

        var worklist = new ArrayDeque<>(reachable);

        while (!worklist.isEmpty()) {
            var inst = worklist.poll();
            for (var use : inst.used) {
                if (use.value instanceof Instruction instr && reachable.add(instr))
                    worklist.add(instr);
            }
        }

        for (var iter = instrs.iterator(); iter.hasNext(); ) {
            var inst = iter.next();
            if (!reachable.contains(inst)) {
                iter.remove();
                inst.dispose();
            }
        }
    }

    private static void removeUnusedBlockArguments(Function function) {
        function.blocks.forEach(DeadCodeElimination::removeUnusedBlockArguments);
    }

    private static void removeUnusedBlockArguments(BasicBlock block) {
        // 删除无用的 block argument
        for (var iter = block.args.entrySet().iterator(); iter.hasNext(); ) {
            var entry = iter.next();
            var var = entry.getKey();
            var blockArg = entry.getValue();
            if (!blockArg.hasNoUse()) continue;

            iter.remove();
            for (var use : block.usedBy) {
                var term = (Instruction.Terminator) use.user;
                switch (term) {
                    case Br it -> {
                        var tp = it.trueParams.remove(var);
                        if (tp != null) tp.dispose();
                        var fp = it.falseParams.remove(var);
                        if (fp != null) fp.dispose();
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
        function.localVars.removeIf(it -> !it.isParam && it.hasNoUse());
    }

}
