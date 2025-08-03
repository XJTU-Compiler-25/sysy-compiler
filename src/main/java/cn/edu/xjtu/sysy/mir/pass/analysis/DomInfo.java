package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record DomInfo(
        // 一个块的直接支配者
        Map<BasicBlock, BasicBlock> idomMap,
        // 被一个块直接支配的块集合
        Map<BasicBlock, Set<BasicBlock>> idomeeMap,
        // 一个块的支配边界
        Map<BasicBlock, Set<BasicBlock>> dfMap,
        // 一个块在支配树上的深度
        Map<BasicBlock, Integer> domDepthMap
) {
    public BasicBlock getIDom(BasicBlock block) {
        return idomMap.get(block);
    }

    public Set<BasicBlock> getDF(BasicBlock block) {
        return dfMap.getOrDefault(block, Set.of());
    }

    public int getDomDepth(BasicBlock block) {
        return domDepthMap.getOrDefault(block, -1);
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

    public BasicBlock domLCA(BasicBlock a, BasicBlock b) {
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
