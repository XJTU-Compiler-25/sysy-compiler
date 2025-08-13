package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.*;

import java.util.*;
import java.util.stream.Collectors;

// control flow graph 
public record CFG(
        // 基本块的前驱块
        Map<BasicBlock, Set<BasicBlock>> predMap,
        // 基本块的后继块
        Map<BasicBlock, Set<BasicBlock>> succMap,
        // 基本块的前驱终结指令
        Map<BasicBlock, Set<Instruction.Terminator>> predTermMap
) {
    public Set<BasicBlock> getPredBlocksOf(BasicBlock block) {
        return predMap.getOrDefault(block, Set.of());
    }

    public Set<BasicBlock> getSuccBlocksOf(BasicBlock block) {
        return succMap.getOrDefault(block, Set.of());
    }

    public Set<Instruction.Terminator> getPredTermsOf(BasicBlock block) {
        return predTermMap.getOrDefault(block, Set.of());
    }

    /**
     * 对基本块进行拓扑排序，得到逆后序 (Reverse Post Order)，用于前向数据流分析
     */
    public List<BasicBlock> getRPOBlocks(BasicBlock entry) {
        var postOrder = new ArrayList<BasicBlock>();
        var visited = new HashSet<BasicBlock>();
        dfs(entry, visited, postOrder);
        return postOrder.reversed();
    }

    /**
     * 对基本块进行拓扑排序，得到后序(Post Order)，用于后向数据流分析
     */
    public List<BasicBlock> getPOBlocks(BasicBlock entry) {
        var postOrder = new ArrayList<BasicBlock>();
        var visited = new HashSet<BasicBlock>();
        dfs(entry, visited, postOrder);
        return postOrder;
    }

    private void dfs(BasicBlock block, HashSet<BasicBlock> visited, ArrayList<BasicBlock> postOrder) {
        visited.add(block);
        for (var successor : getSuccBlocksOf(block)) {
            if (!visited.contains(successor)) dfs(successor, visited, postOrder);
        }
        postOrder.add(block);
    }

    public List<BasicBlock> getRPOBlocks(Function function) {
        return getRPOBlocks(function.entry);
    }

    public List<BasicBlock> getPOBlocks(Function function) {
        return getPOBlocks(function.entry);
    }

    public Set<Value> getIncomingValues(BlockArgument arg) {
        var block = arg.block;
        return getPredTermsOf(block).stream()
                .map(term -> term.getParam(block, arg))
                .collect(Collectors.toSet());
    }

}
