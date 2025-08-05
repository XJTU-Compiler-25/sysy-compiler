package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.HashMap;

public final class FrequencyAnalysis extends ModuleVisitor<FrequencyInfo> {
    public FrequencyAnalysis(Pipeline<Module> pipeline) {
        super(pipeline);
    }

    private CFG cfg;
    private LoopInfo loopInfo;
    private HashMap<BasicBlock, Integer> frequencyMap;
    @Override
    public FrequencyInfo process(Module module) {
        cfg = getCFG();
        loopInfo = getLoopInfo();
        frequencyMap = new HashMap<>();

        super.process(module);

        return new FrequencyInfo(frequencyMap);
    }

    @Override
    public void visit(Function function) {
        var entry = function.entry;
        frequencyMap.put(entry, 100);

        // 逆后序遍历，处理一个块时，其前驱必定已处理
        for (var block : cfg.getRPOBlocks(entry)) {
            if (block == entry) continue;

            var freq = 0;

            for (var pred : cfg.getPredBlocksOf(block))
                freq = saturatedAdd(freq, frequencyMap.getOrDefault(pred, 0) / 2);

            freq = saturatedMul(freq, 1 << (4 * loopInfo.getLoopDepthOf(block))); // = 16 ^ loopDepth
            frequencyMap.put(block, freq);
        }
    }

    private static int saturatedAdd(int a, int b) {
        var sum = (long) a + (long) b;
        return (int) Math.min(sum, Integer.MAX_VALUE);
    }

    private static int saturatedMul(int a, int b) {
        var product = (long) a * (long) b;
        return (int) Math.min(product, Integer.MAX_VALUE);
    }

}
