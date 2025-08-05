package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;

import java.util.Map;

public record FrequencyInfo(Map<BasicBlock, Integer> frequencies) {
    public int getFrequency(BasicBlock block) {
        return frequencies.get(block);
    }
}
