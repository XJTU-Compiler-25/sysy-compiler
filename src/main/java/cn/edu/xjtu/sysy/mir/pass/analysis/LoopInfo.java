package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record LoopInfo(
        // 函数的所有循环，Key 是 header block
        Map<Function, Set<Loop>> allLoopsMap,
        // 基本块的循环深度
        Map<BasicBlock, Integer> loopDepthMap
) {
    public Set<Loop> getLoopsOf(Function function) {
        return allLoopsMap.get(function);
    }

    public Set<Loop> getRootLoopsOf(Function function) {
        return allLoopsMap.get(function).stream().filter(it -> it.parent == null).collect(Collectors.toSet());
    }

    public Set<Loop> getLoopsContains(BasicBlock basicBlock) {
        return allLoopsMap.get(basicBlock.getFunction()).stream()
                .filter(loop -> loop.blocks.contains(basicBlock))
                .collect(Collectors.toSet());
    }

    public int getLoopDepthOf(BasicBlock basicBlock) {
        return loopDepthMap.get(basicBlock);
    }
}
