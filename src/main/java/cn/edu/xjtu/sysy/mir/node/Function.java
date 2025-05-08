package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.ArrayList;

public final class Function {
    public String name;
    public Type returnType;
    public BasicBlock entry;
    public final ArrayList<BasicBlock> blocks = new ArrayList<>();

    public BasicBlock newBlock() {
        var block = new BasicBlock();
        blocks.add(block);
        return block;
    }
}
