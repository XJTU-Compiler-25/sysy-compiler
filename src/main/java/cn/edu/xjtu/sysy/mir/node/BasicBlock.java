package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Basic Block 继承 Value 可以借助 Value 的 Def-Use 去便利地收集 pred 和 succ blocks
 */
public final class BasicBlock extends Value {

    private final Function function;
    public ArrayList<Instruction> instructions = new ArrayList<>();
    public Instruction.Terminator terminator;
    public final ArrayList<BlockArgument> args = new ArrayList<>();

    // 以下都为分析用的字段

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

    public void setTerminator(Instruction.Terminator terminator) {
        this.terminator = terminator;
    }

    public BlockArgument addBlockArgument(Type type) {
        var arg = new BlockArgument(this, type);
        args.add(arg);
        return arg;
    }

    @Override
    public String shortName() {
        return "bb" + id;
    }

}
