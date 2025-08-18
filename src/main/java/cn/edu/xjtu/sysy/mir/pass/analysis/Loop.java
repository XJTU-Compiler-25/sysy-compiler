package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Instruction;

import java.util.HashSet;
import java.util.Set;

public final class Loop {
    public BasicBlock header;
    public Set<BasicBlock> blocks;
    public Loop parent;
    public Set<Loop> children = new HashSet<>();
    public int depth;

    public Loop(BasicBlock header, Set<BasicBlock> blocks) {
        this.header = header;
        this.blocks = blocks;
    }

    public boolean contains(BasicBlock block) {
        return blocks.contains(block);
    }

    public boolean isChildOf(Loop loop) {
        return parent == loop;
    }

    public boolean hasChild(Loop loop) {
        return children.contains(loop);
    }

    public BasicBlock getPreheader() {
        var preds = CFGAnalysis.getPredTermsOf(header);
        if (preds.size() == 1 && preds.iterator().next() instanceof Instruction.Jmp jmp) return jmp.getBlock();
        return null;
    }

    public Set<BasicBlock> getLatches() {
        var result = new HashSet<BasicBlock>();
        for (var pred : CFGAnalysis.getPredBlocksOf(header)) {
            if (blocks.contains(pred)) result.add(pred);
        }
        return result;
    }

    public BasicBlock getLatch() {
        var latches = getLatches();
        if (latches.size() == 1) return latches.iterator().next();
        return null;
    }

    public Set<BasicBlock> getExitings() {
        var result = new HashSet<BasicBlock>();
        for (var block : blocks) {
            var succs = CFGAnalysis.getSuccBlocksOf(block);
            for (var succ : succs) if (!blocks.contains(succ)) result.add(block);
        }
        return result;
    }

    public Set<BasicBlock> getExits() {
        var result = new HashSet<BasicBlock>();
        for (var block : blocks) {
            for (var succ : CFGAnalysis.getSuccBlocksOf(block)) {
                if (!blocks.contains(succ)) result.add(succ);
            }
        }
        return result;
    }

}
