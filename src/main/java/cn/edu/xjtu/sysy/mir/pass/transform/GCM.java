package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// Global Code Motion
// 不处理 load store
public final class GCM extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    private FuncInfo funcInfo;
    private CFG cfg;
    private LoopInfo loopInfo;
    private DomInfo domInfo;
    @Override
    public void visit(Module module) {
        funcInfo = getResult(FuncInfoAnalysis.class);
        cfg = getResult(CFGAnalysis.class);
        loopInfo = getResult(LoopAnalysis.class);
        domInfo = getResult(DominanceAnalysis.class);

        for (var function : module.getFunctions()) run(function);
    }

    private Function currentFunc;
    private void run(Function function) {
        currentFunc = function;
        var blocks = domInfo.getDFN(function);
        var insts = new ArrayList<Instruction>();
        for (var block : blocks) {
            insts.addAll(block.instructions);
            insts.add(block.terminator);
        }

        scheduleEarly(insts);
        scheduleLate(insts.reversed());
        place(blocks);
    }

    private boolean isPinned(Instruction inst) {
        return switch (inst) {
            case Terminator _, Load _, Store _, Alloca _ -> true;
            // 纯函数不会读写函数参数和全局变量，因此可以浮动
            case Call it -> !funcInfo.isPure(it.getFunction());
            case CallExternal _ -> true;
            default -> false;
        };
    }

    // 以 pinned 指令为源点
    private final HashMap<Instruction, BasicBlock> bbEarly = new HashMap<>();
    private final HashSet<Instruction> visited = new HashSet<>();
    private void scheduleEarly(ArrayList<Instruction> insts) {
        visited.clear();
        bbEarly.clear();
        for (var inst : insts) {
            if (isPinned(inst)) {
                visited.add(inst);
                bbEarly.put(inst, inst.getBlock());
                for (var use : inst.used) {
                    if (use.value instanceof Instruction operand) scheduleEarly(operand);
                }
            }
        }
    }

    private void scheduleEarly(Instruction inst) {
        if (!visited.add(inst) || isPinned(inst)) return;

        var earlyBB = currentFunc.entry;
        for (var use : inst.used) {
            switch (use.value) {
                case Instruction it -> {
                    scheduleEarly(it);
                    var opBB = bbEarly.get(it);
                    if (domInfo.getDomDepth(opBB) > domInfo.getDomDepth(earlyBB)) earlyBB = opBB;
                }
                // block arg 相当于一个 pinned 指令
                case BlockArgument it -> {
                    var opBB = it.block;
                    if (domInfo.getDomDepth(opBB) > domInfo.getDomDepth(earlyBB)) earlyBB = opBB;
                }
                default -> { }
            }
        }
        bbEarly.put(inst, earlyBB);
    }

    private final HashMap<Instruction, BasicBlock> bbBest = new HashMap<>();
    private void scheduleLate(List<Instruction> insts) {
        visited.clear();
        bbBest.clear();
        for (var inst : insts) {
            if (isPinned(inst)) {
                visited.add(inst);
                for (var use : inst.usedBy) {
                    if (use.user instanceof Instruction user) scheduleLate(user);
                }
            }
        }
    }

    private void scheduleLate(Instruction inst) {
        if (!visited.add(inst) || isPinned(inst)) return;

        BasicBlock lca = null;
        for (var use : inst.usedBy) {
            if (!(use.user instanceof Instruction user)) continue;
            scheduleLate(user);
            var opBB = user.getBlock();
            lca = lca == null ? opBB : domInfo.domLCA(lca, opBB);
        }
        var earlyBB = bbEarly.get(inst);
        if (lca == null) lca = earlyBB;
        var bestBB = lca;
        var curBB = lca;
        while (domInfo.getDomDepth(curBB) >= domInfo.getDomDepth(earlyBB)) {
            if (loopInfo.getLoopDepthOf(curBB) < loopInfo.getLoopDepthOf(bestBB)) bestBB = curBB;
            curBB = domInfo.getIDom(curBB);
            if (curBB == null) break;
        }
        bbBest.put(inst, bestBB);
    }

    private void place(List<BasicBlock> blocks) {
        var toInsert = new ArrayList<Instruction>();
        for (var block : blocks) {
            for (var iter = block.instructions.iterator(); iter.hasNext(); ) {
                var inst = iter.next();
                if (isPinned(inst)) continue;

                var bestBB = bbBest.get(inst);
                if (bestBB == block) continue;
                iter.remove();
                toInsert.add(inst);
            }
        }

        for (var inst : toInsert) {
            helper.changeBlock(bbBest.get(inst));
            helper.insert(inst);
            helper.changeBlockToNull();
        }
    }


}
