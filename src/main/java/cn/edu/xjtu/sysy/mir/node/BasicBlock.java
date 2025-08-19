package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

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

    @Override
    public String shortName() {
        return "bb" + id;
    }

    public void dispose() {
        instructions.forEach(Instruction::dispose);
        terminator.dispose();
    }

    public Function getFunction() {
        return function;
    }

    public int getInstructionCount() {
        return instructions.size();
    }

    public int indexOf(Instruction instr) {
        return instructions.indexOf(instr);
    }

    public Instruction getInstruction(int idx) {
        return instructions.get(idx);
    }

    public void removeInstruction(Instruction instr) {
        if (instructions.remove(instr)) instr.setBlock(null);
    }

    public Instruction removeInstruction(int idx) {
        var instr = instructions.remove(idx);
        instr.setBlock(null);
        return instr;
    }

    public void insertAtFirst(Instruction instruction) {
        instructions.addFirst(instruction);
        instruction.setBlock(this);
    }

    public void insertAtLast(Instruction instruction) {
        instructions.addLast(instruction);
        instruction.setBlock(this);
    }

    public void insertAt(int idx, Instruction instruction) {
        instructions.add(idx, instruction);
        instruction.setBlock(this);
    }

    public void setTerminator(Instruction.Terminator terminator) {
        this.terminator = terminator;
        terminator.setBlock(this);
    }

    public void removeTerminator() {
        this.terminator = null;
    }

    public Instruction getFirstInstruction() {
        var instrs = this.instructions;
        return instrs.isEmpty() ? terminator : instrs.getFirst();
    }

    public ArrayList<Instruction> getInstructionsAndTerminator() {
        var result = new ArrayList<>(instructions);
        result.add(terminator);
        return result;
    }

    public BlockArgument addBlockArgument(Type type) {
        var arg = new BlockArgument(this, type);
        args.add(arg);
        return arg;
    }
}
