package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class DominanceAnalysis extends ModuleVisitor {
    public DominanceAnalysis(ErrManager errManager) {
        super(errManager);
    }

    // 由于 pred 是立即计算的，还是稍微 cache 一下
    private static final HashMap<BasicBlock, List<BasicBlock>> predCache = new HashMap<>();
    private static List<BasicBlock> getPredBlocks(BasicBlock block) {
        if (predCache.containsKey(block)) return predCache.get(block);
        var preds = block.getPredBlocks();
        predCache.put(block, preds);
        return preds;
    }

    @Override
    public void visit(Module module) {
        for (Function function : module.functions.values()) calcDominance(function);
        predCache.clear();
    }

    private static void calcDominance(Function function) {
        predCache.clear();
        var entry = function.entry;
        var blocks = function.blocks;
        // 计算支配性
        boolean changed = true;
        while (changed) {
            changed = false;
            for (var block : blocks) {
                // entry 的 dom 不需要更新，小剪枝
                if (block == entry) continue;
                var preds = getPredBlocks(block);

                if (preds.isEmpty()) continue;
                var newDom = preds.getFirst();
                // 自己的 dom 就是所有 pred 在支配树上共同祖先
                for (var pred : preds) newDom = lca(pred, newDom);

                if (block.idom != newDom) {
                    block.idom = newDom;
                    changed = true;
                }
            }
        }

        // 计算支配边界
        for (var block : blocks) block.df = new HashSet<>();

        for (var block : blocks) {
            var preds = getPredBlocks(block);
            // 支配边界是控制流合并的结果，只有当有多个支配者时块才可能是支配边界
            if (preds.size() > 1) {
                var idom = block.idom;
                for (var pred : preds) {
                    var cur = pred;
                    // block 是 从 pred 到 (block 的 idom) 的 每个节点 的 支配边界
                    while (cur != idom) {
                        cur.df.add(block);
                        var ci = cur.idom;
                        if (ci == null) break;
                        else cur = ci;
                    }
                }
            }
        }
    }

    private static BasicBlock lca(BasicBlock a, BasicBlock b) {
        if (a == b) return a;
        if (a == null) return b;
        if (b == null) return a;

        var visited = new HashSet<BasicBlock>();
        while (a != null || b != null) {
            if (a != null) {
                if (!visited.add(a)) return a;
                a = a.idom;
            }
            if (b != null) {
                if (!visited.add(b)) return b;
                b = b.idom;
            }
        }

        return null;
    }

}
