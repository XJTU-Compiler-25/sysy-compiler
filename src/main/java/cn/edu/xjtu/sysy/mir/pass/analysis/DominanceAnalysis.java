package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.*;

public final class DominanceAnalysis extends ModuleVisitor<DomInfo> {
    public DominanceAnalysis(Pipeline<Module> pipeline) { super(pipeline); }

    @Override
    public DomInfo process(Module module) {
        var cfg = getCFG();
        var idomMap = new HashMap<BasicBlock, BasicBlock>();
        var idomeeMap = new HashMap<BasicBlock, Set<BasicBlock>>();
        var dfMap = new HashMap<BasicBlock, Set<BasicBlock>>();
        var domDepthMap = new HashMap<BasicBlock, Integer>();

        for (var function : module.getFunctions()) {
            var entry = function.entry;
            var blocks = cfg.getRPOBlocks(function);

            // 计算支配性
            boolean changed = true;
            while (changed) {
                changed = false;
                for (var block : blocks) {
                    // entry 的 dom 不需要更新，小剪枝
                    if (block == entry) continue;
                    var preds = cfg.getPredBlocksOf(block);

                    if (preds.isEmpty()) continue;
                    var newDom = preds.iterator().next();
                    // 自己的 dom 就是所有 pred 在支配树上共同祖先
                    for (var pred : preds) newDom = lca(pred, newDom, idomMap);

                    if (idomMap.get(block) != newDom) {
                        idomMap.put(block, newDom);
                        changed = true;
                    }
                }
            }

            // 收集被直接支配者集合
            for (var block : blocks) {
                if (block == entry) continue;

                var idom = idomMap.get(block);
                idomeeMap.computeIfAbsent(idom, _ -> new HashSet<>()).add(block);
            }

            var worklist = new Worklist<BasicBlock>();
            worklist.add(entry);
            while (!worklist.isEmpty()) {
                var block = worklist.poll();

                // entry 支配深度为 0
                if (block == entry) domDepthMap.put(block, 0);
                // 支配深度比直接支配者的深度大 1
                else domDepthMap.put(block, domDepthMap.get(idomMap.get(block)) + 1);

                for (var idomee : idomeeMap.computeIfAbsent(block, _ -> new HashSet<>()))
                    worklist.add(idomee);
            }

            // 计算支配边界
            for (var block : blocks) {
                var preds = cfg.getPredBlocksOf(block);
                // 支配边界是控制流合并的结果，只有当有多个支配者时块才可能是支配边界
                if (preds.size() > 1) {
                    var idom = idomMap.get(block);
                    for (var pred : preds) {
                        var cur = pred;
                        // block 是 从 pred 到 (block 的 idom) 的 每个节点 的 支配边界
                        while (cur != idom) {
                            dfMap.computeIfAbsent(cur, _ -> new HashSet<>()).add(block);
                            var ci = idomMap.get(cur);
                            if (ci == null) break;
                            else cur = ci;
                        }
                    }
                }
            }
        }

        return new DomInfo(idomMap, idomeeMap, dfMap, domDepthMap);
    }

    private BasicBlock lca(BasicBlock a, BasicBlock b, HashMap<BasicBlock, BasicBlock> idomMap) {
        if (a == b) return a;
        if (a == null) return b;
        if (b == null) return a;

        var visited = new HashSet<BasicBlock>();
        while (a != null || b != null) {
            if (a != null) {
                if (!visited.add(a)) return a;
                a = idomMap.get(a);
            }
            if (b != null) {
                if (!visited.add(b)) return b;
                b = idomMap.get(b);
            }
        }

        return null;
    }

}
