package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Function extends User {
    public Module module;

    public String name;
    public Type.Function funcType;
    public ArrayList<BasicBlock> blocks = new ArrayList<>();
    public BasicBlock entry;
    private int tempValueCounter = 0;
    public ArrayList<Var> params = new ArrayList<>();

    // 以下都为分析用的字段
    public ArrayList<Var> localVars = new ArrayList<>();

    public boolean isAtMostCalledOnce;

    // 是否没有副作用
    public boolean isPure;

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

    public String newBlockLabel() {
        return "bb" + blocks.size();
    }

    public void addBlock(BasicBlock block) {
        blocks.add(block);
        block.label = newBlockLabel();
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
                " (entry = " + entry.label + ", locals = {" +
                localVars.stream().map(it -> it.name + ": " + it.varType)
                        .collect(Collectors.joining(", ")) +
                "}) \n" +
                blocks.stream().map(BasicBlock::toString)
                        .collect(Collectors.joining("\n"));
    }

    // 获取调用该函数的函数列表
    public List<Function> getCallers() {
        var result = new ArrayList<Function>(usedBy.size());
        for (var use : usedBy) if (use.user instanceof Instruction.Call it) result.add(it.getFunction());
        return result;
    }

    public List<Instruction.Call> getCallerInstrs() {
        var result = new ArrayList<Instruction.Call>(usedBy.size());
        for (var use : usedBy) if (use.user instanceof Instruction.Call it) result.add(it);
        return result;
    }

    // 如果跳转目标支配跳转源，则认为是一个 back edge
    public List<Instruction.Terminator> getBackEdges() {
        var result = new ArrayList<Instruction.Terminator>();
        for (var block : blocks) {
            switch(block.terminator) {
                case Instruction.Jmp it -> {
                    if (it.getTarget().dominates(it.getBlock())) result.add(it);
                }
                case Instruction.Br it -> {
                    var source = it.getBlock();
                    if (it.getTrueTarget().dominates(source)) result.add(it);
                    if (it.getFalseTarget().dominates(source)) result.add(it);
                }
                default -> { }
            }
        }
        return result;
    }

}
