package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.*;

public final class LoopAnalysis extends ModuleVisitor<LoopInfo> {
    public LoopAnalysis(Pipeline<Module> pipeline) { super(pipeline); }

    private CFG cfg;
    private DomInfo domInfo;
    private HashMap<Function, Set<Loop>> loopsMap;
    private HashMap<BasicBlock, Integer> loopDepthMap;
    @Override
    public LoopInfo process(Module module) {
        cfg = getCFG();
        domInfo = getDomTree();
        loopsMap = new HashMap<>();
        loopDepthMap = new HashMap<>();

        for (var function : module.getFunctions()) visit(function);

        return new LoopInfo(loopsMap, loopDepthMap);
    }

    @Override
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
                        var latches = backedges.computeIfAbsent(target, _ -> new HashSet<>());
                        latches.add(jumpSource);
                    }
                }
                case Instruction.Br it -> {
                    var trueTarget = it.getTrueTarget();
                    var falseTarget = it.getFalseTarget();
                    if (domInfo.dominates(trueTarget, jumpSource)) {
                        var latches = backedges.computeIfAbsent(trueTarget, _ -> new HashSet<>());
                        latches.add(jumpSource);
                    } else if (domInfo.dominates(falseTarget, jumpSource)) {
                        var latches = backedges.computeIfAbsent(falseTarget, _ -> new HashSet<>());
                        latches.add(jumpSource);
                    }
                }
                default -> { }
            }
        }

        // 根据回边识别所有循环，同一个 header 的所有 latches 都属于同一个循环
        // 否则可能出现 continue 也组成一个循环
        for (var backedge : backedges.entrySet()) {
            var header = backedge.getKey();
            var latches = backedge.getValue();

            for (var latch : latches) {

            }
        }

        // 构建循环树


        // 计算每个 block 的 loop depth
        for (var block : blocks) {
            var depth = 0;
            // 由于 loop 都是完美嵌套，只需要找出这个 block 在几个循环里面
            for (var loop : loops) if (loop.contains(block)) depth++;
            loopDepthMap.put(block, depth);
        }
    }
}
