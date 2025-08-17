package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;

import java.util.Comparator;
import java.util.List;
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

    public int getLoopDepthOf(BasicBlock basicBlock) {
        return loopDepthMap.get(basicBlock);
    }

    public Set<Loop> getRootLoopsOf(Function function) {
        return allLoopsMap.get(function).stream().filter(it -> it.depth == 0).collect(Collectors.toSet());
    }

    public Set<Loop> getLoopsContains(BasicBlock basicBlock) {
        return allLoopsMap.get(basicBlock.getFunction()).stream()
                .filter(loop -> loop.blocks.contains(basicBlock))
                .collect(Collectors.toSet());
    }

    // 从内层循环向外层循环排序（内层在前面）
    public List<Loop> getLoopsInToOut(Function function) {
        return allLoopsMap.get(function).stream()
                .sorted(Comparator.comparingInt(l -> -l.depth))
                .collect(Collectors.toList());
    }

    // 从外层循环向内层循环排序（外层在前面）
    public List<Loop> getLoopsOutToIn(Function function) {
        return allLoopsMap.get(function).stream()
                .sorted(Comparator.comparingInt(l -> l.depth))
                .collect(Collectors.toList());
    }
}
