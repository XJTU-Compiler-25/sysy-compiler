package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

// 这是一个在 enter LIR 之后才会运行的分析
// 所以反向数据流分析直接从 epilogue 开始反向扫描
public final class LiveRangeAnalysis extends ModulePass<LiveRangeInfo> {

    private HashMap<Instruction, Set<Value>> liveIn;
    private HashMap<Instruction, Set<Value>> liveOut;
    private HashMap<Value, Set<Instruction>> instsValueLiveBefore;

    private CFG cfg;
    private final HashMap<Instruction, Set<? extends Instruction>> preds = new HashMap<>();
    private final HashMap<Instruction, Set<? extends Instruction>> succs = new HashMap<>();

    @Override
    public LiveRangeInfo process(Module module) {
        liveOut = new HashMap<>();
        liveIn = new HashMap<>();
        instsValueLiveBefore = new HashMap<>();

        cfg = getResult(CFGAnalysis.class);
        preds.clear();
        succs.clear();

        for (var f : module.getFunctions()) {
            collectPredSucc(f);
            markFunction(f);
        }
        collectAliveAfter();

        return new LiveRangeInfo(liveIn, liveOut, instsValueLiveBefore);
    }

    private void collectPredSucc(Function function) {
        var blocks = cfg.getRPOBlocks(function);

        for (var block : blocks) {
            var predTerms = cfg.getPredTermsOf(block);
            Instruction lastInst = null;
            for (var inst : block.instructions) {
                if (lastInst == null) preds.put(inst, predTerms);
                else preds.put(inst, Set.of(lastInst));
                lastInst = inst;
            }
        }

        for (var block : blocks) {
            var succFirsts = cfg.getSuccBlocksOf(block).stream().map(BasicBlock::getFirstInstruction).collect(Collectors.toSet());
            Instruction lastInst = null;
            for (var inst : block.instructions) {
                if (lastInst != null) succs.put(lastInst, Set.of(inst));
                lastInst = inst;
            }
            succs.put(block.terminator, succFirsts);
        }
    }

    private void markFunction(Function function) {
        for (var inst : function.getAllInstructions()) {
            liveIn.put(inst, new HashSet<>());
            liveOut.put(inst, new HashSet<>());
        }

        var epilogue = function.epilogue;
        liveOut.put(epilogue.terminator, Set.of());

        var visited = new HashSet<BasicBlock>();
        var worklist = new Worklist<BasicBlock>();
        worklist.add(epilogue);
        while (!worklist.isEmpty()) {
            var block = worklist.poll();
            for (var arg : block.args) markAliveBefore(arg, null);
            for (var inst : block.getInstructionsAndTerminator()) markAliveBefore(inst, inst);
            for (var succ : cfg.getPredBlocksOf(block)) {
                if (visited.add(succ)) worklist.add(succ);
            }
        }
    }

    private void markAliveBefore(Value value, Instruction stop) {
        for (var use : value.usedBy) {
            if (use.user instanceof Instruction inst) markAliveBefore(inst, value, stop);
        }
    }

    private void markAliveBefore(Instruction inst, Value value, Instruction stop) {
        if (inst == stop) return;

        if (!liveIn.get(inst).add(value)) return;

        instsValueLiveBefore.computeIfAbsent(value, _ -> new HashSet<>()).add(inst);
        for (var pred : preds.getOrDefault(inst, Set.of()))
            markAliveBefore(pred, value, stop);
    }

    private void collectAliveAfter() {
        succs.forEach((inst, succs) -> {
            var after = new HashSet<Value>();
            liveOut.put(inst, after);
            for (var succ : succs) after.addAll(liveIn.get(succ));
        });
    }

}
