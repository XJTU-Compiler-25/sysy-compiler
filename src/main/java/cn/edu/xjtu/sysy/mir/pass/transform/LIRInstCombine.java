package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.BrBinary;
import cn.edu.xjtu.sysy.mir.node.LIRInstrHelper;
import cn.edu.xjtu.sysy.mir.node.Module;

public class LIRInstCombine extends AbstractTransform {
    LIRInstrHelper helper = new LIRInstrHelper();
    public LIRInstCombine(Pipeline<Module> pipeline) {
        super(pipeline);
    }
    
    // TODO: reg变成imm类型指令
    @Override
    public void visit(Instruction instr) {
        switch (instr) {
            case Instruction.Br br -> visit(br);
            default -> {}
        }
    }

    public void visit(Instruction.Br br) {
        var cond = br.getCondition();
        var block = br.getBlock();
        switch (cond) {
            case Instruction.ILt it -> {
                var branch = new BrBinary(block, Instruction.BrBinary.Op.BLT, it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                branch.putAllTrueParams(br.trueParams);
                branch.putAllFalseParams(br.falseParams);
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) it.dispose();
            }
            case Instruction.ILe it -> {
                var branch = new BrBinary(block, Instruction.BrBinary.Op.BLE, it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                branch.putAllTrueParams(br.trueParams);
                branch.putAllFalseParams(br.falseParams);
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) it.dispose();
            }
            case Instruction.IGe it -> {
                var branch = new BrBinary(block, Instruction.BrBinary.Op.BGE, it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                branch.putAllTrueParams(br.trueParams);
                branch.putAllFalseParams(br.falseParams);
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) it.dispose();
            }
            case Instruction.IGt it -> {
                var branch = new BrBinary(block, Instruction.BrBinary.Op.BGT, it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                branch.putAllTrueParams(br.trueParams);
                branch.putAllFalseParams(br.falseParams);
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) it.dispose();
            }
            case Instruction.IEq it -> {
                var branch = new BrBinary(block, Instruction.BrBinary.Op.BEQ, it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                branch.putAllTrueParams(br.trueParams);
                branch.putAllFalseParams(br.falseParams);
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) it.dispose();
            }
            case Instruction.INe it -> {
                var branch = new BrBinary(block, Instruction.BrBinary.Op.BNE, it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                branch.putAllTrueParams(br.trueParams);
                branch.putAllFalseParams(br.falseParams);
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) it.dispose();
            }
            default -> {}
        }
    }
}
