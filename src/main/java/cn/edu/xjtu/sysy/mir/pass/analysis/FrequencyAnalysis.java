package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleAnalysis;
import cn.edu.xjtu.sysy.util.MathUtils;

import java.util.HashMap;

public final class FrequencyAnalysis extends ModuleAnalysis<FrequencyInfo> {

    public static FrequencyInfo run(Module module) {
        return new FrequencyAnalysis().process(module);
    }

    private CFG cfg;
    private LoopInfo loopInfo;
    private HashMap<BasicBlock, Integer> frequencyMap;
    @Override
    public FrequencyInfo process(Module module) {
        cfg = CFGAnalysis.run(module);
        loopInfo = LoopAnalysis.run(module);
        frequencyMap = new HashMap<>();

        for (var function : module.getFunctions()) visit(function);

        return new FrequencyInfo(frequencyMap);
    }

    public void visit(Function function) {
        var entry = function.entry;
        frequencyMap.put(entry, 100);

        // 逆后序遍历，处理一个块时，其前驱必定已处理
        for (var block : cfg.getRPOBlocks(entry)) {
            if (block == entry) continue;

            var freq = 0;

            for (var pred : cfg.getPredBlocksOf(block))
                freq = MathUtils.saturatedAdd(freq, frequencyMap.getOrDefault(pred, 0) / 2);

            freq = MathUtils.saturatedMul(freq, 1 << (4 * loopInfo.getLoopDepthOf(block))); // = 16 ^ loopDepth
            frequencyMap.put(block, freq);
        }
    }

}
