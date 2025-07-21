package cn.edu.xjtu.sysy.mir.util;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;

import java.util.*;

public final class CFGUtils {

    private CFGUtils() {}

    /** 对基本块进行拓扑排序，得到逆后序，用于前向数据流分析 */
    public static List<BasicBlock> getReversePostOrderedBlocks(BasicBlock entry) {
        var postOrder = new ArrayList<BasicBlock>();
        var visited = new HashSet<BasicBlock>();
        dfs(entry, visited, postOrder, 0);
        return postOrder.reversed();
    }

    /** 对基本块进行拓扑排序，得到后序，用于后向数据流分析 */
    public static List<BasicBlock> getPostOrderedBlocks(BasicBlock entry) {
        var postOrder = new ArrayList<BasicBlock>();
        var visited = new HashSet<BasicBlock>();
        dfs(entry, visited, postOrder, 0);
        return postOrder;
    }

    private static void dfs(BasicBlock block, HashSet<BasicBlock> visited, ArrayList<BasicBlock> postOrder, int order) {
        block.order = order;
        visited.add(block);
        for (var successor : block.succs) if (!visited.contains(successor)) dfs(successor, visited, postOrder, order + 1);
        postOrder.add(block);
    }

}
