package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class Function extends User {
    public Module module;

    public String name;
    public Type.Function funcType;
    public HashSet<BasicBlock> blocks = new HashSet<>();
    public BasicBlock entry;
    public BasicBlock epilogue;
    private int tempValueCounter = 0;
    public ArrayList<Var> params = new ArrayList<>();

    // 以下都为分析用的字段
    public ArrayList<Var> localVars = new ArrayList<>();
    public ArrayList<Loop> loops = new ArrayList<>();

    public Function(Module module, String name, Type.Function funcType) {
        super(Types.Void);
        this.module = module;
        this.name = name;
        this.funcType = funcType;
    }

    public Module getModule() {
        return module;
    }

    public int incTempValueCounter() {
        return tempValueCounter++;
    }

    public BasicBlock addNewBlock() {
        var block = new BasicBlock(this);
        addBlock(block);
        return block;
    }

    public void addBlock(BasicBlock block) {
        blocks.add(block);
        block.order = blocks.size();
    }

    public void removeBlock(BasicBlock block) {
        blocks.remove(block);
    }

    public Var addNewParam(String name, Type type) {
        var param = new Var(name, type, false, true);
        params.add(param);
        return param;
    }

    public Var addNewLocalVar(String name, Type type) {
        var localVar = new Var(name, type, false, false);
        localVars.add(localVar);
        return localVar;
    }

    public void addLoop(Loop loop) {
        loops.add(loop);
    }

    @Override
    public String shortName() {
        return name;
    }

    @Override
    public String toString() {
        return "Function " + name +
                " (" +
                params.stream().map(v -> v.name + ": " + v.varType)
                        .collect(Collectors.joining(", ")) +
                ") -> " + funcType.returnType +
                " (entry = " + entry.order + ", locals = {" +
                localVars.stream().map(it -> it.name + ": " + it.varType)
                        .collect(Collectors.joining(", ")) +
                "}) \n" +
                new CFGAnalysis().process(this).getRPOBlocks(this).stream().map(BasicBlock::toString)
                        .collect(Collectors.joining("\n"));
    }

}
