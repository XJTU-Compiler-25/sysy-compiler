package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

import java.util.*;

public final class LoopAnalysis extends ModulePass<LoopInfo> {

    private CFG cfg;
    private DomInfo domInfo;
    private HashMap<Function, Set<Loop>> loopsMap;
    private HashMap<BasicBlock, Integer> loopDepthMap;

    @Override
    public LoopInfo process(Module module) {
        cfg = getResult(CFGAnalysis.class);
        domInfo = getResult(DominanceAnalysis.class);
        loopsMap = new HashMap<>();
        loopDepthMap = new HashMap<>();

        for (var function : module.getFunctions()) visit(function);

        return new LoopInfo(loopsMap, loopDepthMap);
    }

    public void visit(Function function) {
        var loops = new HashSet<Loop>();
        loopsMap.put(function, loops);

        var blocks = cfg.getRPOBlocks(function);

        // 先找出所有回边，将指向同一个 header block 的 latches 都集合成一组
        var backedges = new HashMap<BasicBlock, Set<BasicBlock>>();
        for (var jumpSource : blocks) {
            switch(jumpSource.terminator) {
                case Instruction.Jmp it -> {
                    var target = it.getTarget();
                    if (domInfo.dominates(target, jumpSource)) {
                        var latches = backedges.computeIfAbsent(target, k -> new HashSet<>());
                        latches.add(jumpSource);
                    }
                }
                case Instruction.Br it -> {
                    var trueTarget = it.getTrueTarget();
                    var falseTarget = it.getFalseTarget();
                    // 这里应该之前需要分割一下关键边
                    if (domInfo.dominates(trueTarget, jumpSource)) {
                        var latches = backedges.computeIfAbsent(trueTarget, k -> new HashSet<>());
                        latches.add(jumpSource);
                    }
                    if (domInfo.dominates(falseTarget, jumpSource)) {
                        var latches = backedges.computeIfAbsent(falseTarget, k -> new HashSet<>());
                        latches.add(jumpSource);
                    }
                }
                default -> { }
            }
        }

        var natLoops = new HashMap<BasicBlock, Set<BasicBlock>>();
        // 识别自然循环并合并
        for (var backedge : backedges.entrySet()) {
            var header = backedge.getKey();
            var latches = backedge.getValue();
            var blocksInLoop = natLoops.computeIfAbsent(header, k -> new HashSet<>());

            for (var latch : latches) {
                var loopBlocks = new HashSet<BasicBlock>();
                loopBlocks.add(header);
                loopBlocks.add(latch);
                var stack = new ArrayDeque<BasicBlock>();
                if (header != latch) stack.addFirst(latch);
                while (!stack.isEmpty()) {
                    var block = stack.removeFirst();
                    for (var pred : cfg.getPredBlocksOf(block)) {
                        if (pred == header) continue;
                        if (loopBlocks.add(pred)) stack.addLast(pred);
                    }
                }
                blocksInLoop.addAll(loopBlocks);
            }
        }

        for (var natLoop : natLoops.entrySet()) loops.add(new Loop(natLoop.getKey(), natLoop.getValue()));

        // 构建循环森林
        for (var loop : loops) {
            var loopBlocks = loop.blocks;
            Loop parent = null;
            for (var candidate : loops) {
                if (candidate == loop) continue;
                var candidateBlocks = candidate.blocks;
                if (candidateBlocks.containsAll(loopBlocks)) {
                    if (parent == null || parent.blocks.size() > candidateBlocks.size())
                        parent = candidate;
                }
            }
            loop.parent = parent;
            if (parent != null) parent.children.add(loop);
        }

        // 计算每个 block 的 loop depth
        for (var block : blocks) {
            var depth = 0;
            // 由于 loop 都是完美嵌套，只需要找出这个 block 在几个循环里面
            for (var loop : loops) if (loop.contains(block)) depth++;
            loopDepthMap.put(block, depth);
        }
    }

}
