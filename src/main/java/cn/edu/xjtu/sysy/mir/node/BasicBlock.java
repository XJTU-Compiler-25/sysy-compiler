package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.symbol.Types;

/**
 * Basic Block 继承 Value 可以借助 Value 的 Def-Use 去便利地收集 pred 和 succ blocks
 */
public final class BasicBlock extends Value {
    public int order;

    private final Function function;
    public final HashMap<Var, BlockArgument> args = new HashMap<>();
    public ArrayList<Instruction> instructions = new ArrayList<>();
    public Instruction.Terminator terminator;

    // 以下都为分析用的字段

    // 在几层循环里面
    public int loopDepth = 0;

    public BasicBlock(Function function, int loopDepth) {
        this(function);
        this.loopDepth = loopDepth;
    }

    public BasicBlock(Function function) {
        super(Types.Void);
        this.function = function;
    }

    public void dispose() {
        instructions.forEach(Instruction::dispose);
        terminator.dispose();
    }

    public Function getFunction() {
        return function;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addInstruction(int idx, Instruction instruction) {
        instructions.add(idx, instruction);
    }

    public void setTerminator(Instruction.Terminator terminator) {
        this.terminator = terminator;
    }

    public BlockArgument addBlockArgument(Var var) {
        var arg = new BlockArgument(this, var);
        args.put(var, arg);
        return arg;
    }

    public BlockArgument getBlockArgument(Var var) {
        return args.get(var);
    }

    @Override
    public String shortName() {
        return "bb" + order;
    }

    public String toString() {
        return shortName() + " (" +
                args.values().stream().map(it -> it.var.name)
                        .collect(Collectors.joining(", ")) +
                "):\n" + instructions.stream().map(it -> it.toString() + "\n")
                        .collect(Collectors.joining()) + terminator;
    }
}
