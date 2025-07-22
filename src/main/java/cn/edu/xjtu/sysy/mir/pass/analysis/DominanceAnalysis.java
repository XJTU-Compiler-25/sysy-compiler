package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.*;

public final class DominanceAnalysis extends ModuleVisitor<DominanceAnalysis.Result> {
    public DominanceAnalysis(Pipeline<Module> pipeline) { super(pipeline); }

    public record Result(
            Map<BasicBlock, BasicBlock> idomMap,
            Map<BasicBlock, Set<BasicBlock>> dfMap
    ) {
        public BasicBlock getIDom(BasicBlock block) {
            return idomMap.get(block);
        }

        public Set<BasicBlock> getDF(BasicBlock block) {
            return dfMap.getOrDefault(block, Set.of());
        }

        public boolean strictlyDominates(BasicBlock domer, BasicBlock domee) {
            return domer != domee && dominates(domer, domee);
        }

        public boolean dominates(BasicBlock domer, BasicBlock domee) {
            // 自己支配自己
            if (domer == domee) return true;
            // entry 不被任何块支配
            if (getIDom(domee) == null) return false;

            // 通过支配树判断
            while (domee != null) {
                if (domee == domer) return true;
                domee = getIDom(domee);
            }
            return false;
        }
    }

    @Override
    public Result process(Module module) {
        var cfg = getCFG();
        var idomMap = new HashMap<BasicBlock, BasicBlock>();
        var dfMap = new HashMap<BasicBlock, Set<BasicBlock>>();
        for (Function function : module.getFunctions()) {
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

            // 计算支配边界
            for (var block : blocks) dfMap.put(block, new HashSet<>());

            for (var block : blocks) {
                var preds = cfg.getPredBlocksOf(block);
                // 支配边界是控制流合并的结果，只有当有多个支配者时块才可能是支配边界
                if (preds.size() > 1) {
                    var idom = idomMap.get(block);
                    for (var pred : preds) {
                        var cur = pred;
                        // block 是 从 pred 到 (block 的 idom) 的 每个节点 的 支配边界
                        while (cur != idom) {
                            dfMap.get(cur).add(block);
                            var ci = idomMap.get(cur);
                            if (ci == null) break;
                            else cur = ci;
                        }
                    }
                }
            }
        }
        return new Result(idomMap, dfMap);
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
