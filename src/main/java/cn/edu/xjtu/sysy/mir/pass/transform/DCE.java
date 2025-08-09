package cn.edu.xjtu.sysy.mir.pass.transform;

import java.util.HashSet;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.Alloca;
import cn.edu.xjtu.sysy.mir.node.Instruction.Call;
import cn.edu.xjtu.sysy.mir.node.Instruction.CallExternal;
import cn.edu.xjtu.sysy.mir.node.Instruction.Store;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfoAnalysis;
import cn.edu.xjtu.sysy.util.Worklist;

// dead code elimination
// 是一个 aggressively dce，因为预先假设都不可访问
// dce 是一个 instruction level 的 pass，基本块相关的优化放在 cfg simplify，这样，dce 不会改变 cfg
public class DCE extends ModuleTransformer {

    private FuncInfo funcInfo;

    @Override
    public void visit(Module module) {
        funcInfo = FuncInfoAnalysis.run(module);

        var modified = false;
        do {
            modified = false;
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
                if (!blockArg.notUsed()) continue;

                modified = true;
                iter.remove();
                for (var term : CFGAnalysis.getPredTermsOf(block)) term.removeParam(block, blockArg);
            }
        }
        return modified;
    }

}
