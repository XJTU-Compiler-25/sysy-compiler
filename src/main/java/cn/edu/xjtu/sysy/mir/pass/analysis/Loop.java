package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;

import java.util.HashSet;
import java.util.Set;

public final class Loop {
    public BasicBlock header;
    public Set<BasicBlock> blocks;
    public Loop parent;
    public Set<Loop> children = new HashSet<>();

    // 运行 loop canonicalize 的时候会识别或创建出来
    public BasicBlock preheader;
    public BasicBlock latch;
    public BasicBlock exit;

    public Loop(BasicBlock header, Set<BasicBlock> blocks) {
        this.header = header;
        this.blocks = blocks;
    }

    public boolean contains(BasicBlock block) {
        return blocks.contains(block);
    }
}
