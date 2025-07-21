package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.HashSet;

public final class DominanceAnalysis extends ModuleVisitor {
    public DominanceAnalysis(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Module module) {
        for (Function function : module.getFunctions()) calcDominance(function);
    }

    private static void calcDominance(Function function) {
        var entry = function.entry;
        var blocks = function.getTopoSortedBlocks();
        // 计算支配性
        boolean changed = true;
        while (changed) {
            changed = false;
            for (var block : blocks) {
                // entry 的 dom 不需要更新，小剪枝
                if (block == entry) continue;
                var preds = block.preds;

                if (preds.isEmpty()) continue;
                var newDom = preds.iterator().next();
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
            var preds = block.preds;
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
