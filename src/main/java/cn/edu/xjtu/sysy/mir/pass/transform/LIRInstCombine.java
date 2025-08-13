package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.LIRInstrHelper;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

public class LIRInstCombine extends ModulePass {
    LIRInstrHelper helper = new LIRInstrHelper();
    
    @Override
    public void visit(BasicBlock bb) {
        bb.instructions.forEach(this::visit);
        visit(bb.terminator);
    }

    @Override
    public void visit(Instruction instr) {
        switch (instr) {
            case Instruction.Br br -> visit(br);
            case Instruction.IAdd it -> visit(it);
            case Instruction.ISub it -> visit(it);
            case Instruction.And it -> visit(it);
            case Instruction.Or it -> visit(it);
            case Instruction.Xor it -> visit(it);
            case Instruction.Shl it -> visit(it);
            case Instruction.Shr it -> visit(it);
            case Instruction.AShr it -> visit(it);
            default -> {}
        }
    }

    public void visit(Instruction.IAdd instr) {
        if (instr.lhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var addi = helper.addi(instr.rhs.value, it.value);
            instr.replaceWith(addi);
        } else if (instr.rhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var addi = helper.addi(instr.lhs.value, it.value);
            instr.replaceWith(addi);
        }
    }

    public void visit(Instruction.ISub instr) {
        if (instr.rhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var addi = helper.addi(instr.lhs.value, -it.value);
            instr.replaceWith(addi);
        }
    }

    public void visit(Instruction.And instr) {
        if (instr.lhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var andi = helper.andi(instr.rhs.value, it.value);
            instr.replaceWith(andi);
        } else if (instr.rhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var andi = helper.andi(instr.lhs.value, it.value);
            instr.replaceWith(andi);
        }
    }

    public void visit(Instruction.Or instr) {
        if (instr.lhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var ori = helper.ori(instr.rhs.value, it.value);
            instr.replaceWith(ori);
        } else if (instr.rhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var ori = helper.ori(instr.lhs.value, it.value);
            instr.replaceWith(ori);
        }
    }

    public void visit(Instruction.Xor instr) {
        if (instr.lhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var xori = helper.xori(instr.rhs.value, it.value);
            instr.replaceWith(xori);
        } else if (instr.rhs.value instanceof ImmediateValue.IntConst it && -2048 <= it.value && it.value < 2048) {
            var xori = helper.xori(instr.lhs.value, it.value);
            instr.replaceWith(xori);
        }
    }

    public void visit(Instruction.Shl instr) {
        if (instr.rhs.value instanceof ImmediateValue.IntConst it && 0 <= it.value && it.value < 32) {
            var slli = helper.slli(instr.lhs.value, it.value);
            instr.replaceWith(slli);
        }
    }

    public void visit(Instruction.Shr instr) {
        if (instr.rhs.value instanceof ImmediateValue.IntConst it && 0 <= it.value && it.value < 32) {
            var srli = helper.srli(instr.lhs.value, it.value);
            instr.replaceWith(srli);
        }
    }

    public void visit(Instruction.AShr instr) {
        if (instr.rhs.value instanceof ImmediateValue.IntConst it && 0 <= it.value && it.value < 32) {
            var srai = helper.srai(instr.lhs.value, it.value);
            instr.replaceWith(srai);
        }
    }

    public void visit(Instruction.Br br) {
        var cond = br.getCondition();
        var block = br.getBlock();
        switch (cond) {
            case Instruction.ILt it -> {
                var branch = helper.blt(it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                br.trueParams.forEach((arg, use) -> branch.putTrueParam(arg, use.value));
                br.falseParams.forEach((arg, use) -> branch.putFalseParam(arg, use.value));
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) {
                    it.dispose();
                    it.getBlock().instructions.remove(it);
                }
            }
            case Instruction.ILe it -> {
                var branch = helper.ble(it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                br.trueParams.forEach((arg, use) -> branch.putTrueParam(arg, use.value));
                br.falseParams.forEach((arg, use) -> branch.putFalseParam(arg, use.value));
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) {
                    it.dispose();
                    it.getBlock().instructions.remove(it);
                }
            }
            case Instruction.IGe it -> {
                var branch = helper.bge(it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                br.trueParams.forEach((arg, use) -> branch.putTrueParam(arg, use.value));
                br.falseParams.forEach((arg, use) -> branch.putFalseParam(arg, use.value));
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) {
                    it.dispose();
                    it.getBlock().instructions.remove(it);
                }
            }
            case Instruction.IGt it -> {
                var branch = helper.bgt(it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                br.trueParams.forEach((arg, use) -> branch.putTrueParam(arg, use.value));
                br.falseParams.forEach((arg, use) -> branch.putFalseParam(arg, use.value));
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) {
                    it.dispose();
                    it.getBlock().instructions.remove(it);
                }
            }
            case Instruction.IEq it -> {
                var branch = helper.beq(it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                br.trueParams.forEach((arg, use) -> branch.putTrueParam(arg, use.value));
                br.falseParams.forEach((arg, use) -> branch.putFalseParam(arg, use.value));
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) {
                    it.dispose();
                    it.getBlock().instructions.remove(it);
                }
            }
            case Instruction.INe it -> {
                var branch = helper.bne(it.lhs.value, it.rhs.value, br.getTrueTarget(), br.getFalseTarget());
                br.trueParams.forEach((arg, use) -> branch.putTrueParam(arg, use.value));
                br.falseParams.forEach((arg, use) -> branch.putFalseParam(arg, use.value));
                block.terminator = branch;
                br.dispose();
                if (it.usedBy.isEmpty()) {
                    it.dispose();
                    it.getBlock().instructions.remove(it);
                }
            }
            default -> {}
        }
    }
}
