package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfoAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfo;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashSet;

// dead code elimination
@SuppressWarnings("unchecked")
public class DCE extends AbstractTransform {
    public DCE(Pipeline<Module> pipeline) { super(pipeline); }

    private FuncInfo funcInfo;

    @Override
    public Class<? extends Pass<Module, ?>>[] invalidates() {
        return new Class[] { CFGAnalysis.class };
    }

    @Override
    public void visit(Module module) {
        funcInfo = getResult(FuncInfoAnalysis.class);

        var modified = false;
        do {
            modified = false;
            modified |= removeUnreachableBlocks(module);
            modified |= removeUnusedInstructions(module);
            modified |= removeUnusedBlockArguments(module);
        } while (modified);
    }

    public boolean removeUnusedInstructions(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) modified |= removeUnusedInstructions(function);
        return modified;
    }

    public boolean removeUnusedInstructions(Function function) {
        var modified = false;
        var blocks = function.blocks;
        var reachable = new HashSet<Instruction>();
        for (var block : blocks) {
            var instrs = block.instructions;
            // 终结指令是可达的
            reachable.add(block.terminator);
            // 有副作用的指令是可达的
            for (var it : instrs) {
                // 局部数组以外的 Store 指令、非纯函数调用和外部调用都是有副作用的
                if ((it instanceof Store store && !(store.address.value instanceof Alloca))
                        || (it instanceof Call call && !funcInfo.isPure(call.getFunction()))
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
                    modified = true;
                }
            }
        }
        return modified;
    }

    public static boolean removeUnreachableBlocks(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) modified |= removeUnreachableBlocks(function);
        return modified;
    }

    public static boolean removeUnreachableBlocks(Function function) {
        var modified = false;
        var reachable = new HashSet<BasicBlock>();
        reachable.add(function.entry);

        // 从入口块开始，遍历所有可达的块
        dfs(function.entry, reachable);

        for (var iterator = function.blocks.iterator(); iterator.hasNext(); ) {
            var block = iterator.next();
            if (!reachable.contains(block)) {
                iterator.remove();
                block.dispose();
                modified = true;
            }
        }
        return modified;
    }

    private static void dfs(BasicBlock block, HashSet<BasicBlock> visited) {
        for (var succ : CFGAnalysis.getSuccBlocksOf(block)) {
            if (visited.add(succ)) dfs(succ, visited);
        }
    }

    public static boolean removeUnusedBlockArguments(Module module) {
        var modified = false;
        for (var function : module.getFunctions()) modified |= removeUnusedBlockArguments(function);
        return modified;
    }

    public static boolean removeUnusedBlockArguments(Function function) {
        var modified = false;
        for (var block : function.blocks) {
            if (block == function.entry) continue; // 不删入口块的参数

            for (var iter = block.args.iterator(); iter.hasNext(); ) {
                var blockArg = iter.next();
                if (blockArg.usedBy.stream().anyMatch(it -> !(it.user instanceof Instruction.Terminator)))
                    continue;

                // 这个参数只被传过来，但是没有被使用（所有使用者都是 Terminator）时可以被删掉
                modified = true;
                iter.remove();
                for (var term : CFGAnalysis.getPredTermsOf(block)) term.removeParam(block, blockArg);
            }
        }
        return modified;
    }

}
