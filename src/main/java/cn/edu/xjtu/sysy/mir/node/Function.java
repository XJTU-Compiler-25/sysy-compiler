package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;

import static cn.edu.xjtu.sysy.util.Pair.pair;

public final class Function extends Value {
    public Module module;

    public String name;
    public Type.Function funcType;
    public BasicBlock entry = new BasicBlock(this);
    public HashSet<BasicBlock> blocks = new HashSet<>();
    // 函数参数是入口块的参数，是保插入序的
    public ArrayList<Pair<String, BlockArgument>> params = new ArrayList<>();

    // 以下都为分析用的字段

    public Function(Module module, String name, Type.Function funcType) {
        super(Types.Void);
        this.module = module;
        this.name = name;
        this.funcType = funcType;
        blocks.add(entry);
    }

    public Module getModule() {
        return module;
    }

    public void addBlock(BasicBlock block) {
        blocks.add(block);
    }

    public void removeBlock(BasicBlock block) {
        blocks.remove(block);
    }

    public BlockArgument addNewParam(String name, Type type) {
        var param = entry.addBlockArgument(Types.ptrOf(type));
        params.add(pair(name, param));
        return param;
    }

    public BlockArgument getParam(String name) {
        for (var param : params) if (param.first().equals(name)) return param.second();
        return null;
    }

    @Override
    public String shortName() {
        return "@" + name;
    }

    public void dispose() {
        for (var block : blocks) block.dispose();
    }

}
