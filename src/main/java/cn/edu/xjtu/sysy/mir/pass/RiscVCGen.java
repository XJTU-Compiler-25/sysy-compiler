/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.riscv.RiscVWriter;

public class RiscVCGen {
    public RiscVWriter asm = new RiscVWriter();
/*
    public void visit(Module module) {
        module.functions.values().forEach(this::visit);
        asm.emitAll();
    }
     
    public void visit(Function func) {
        asm.label(new Label(func.shortName()));
        var entry = func.entry;
        var entryParams = entry.args;
        var floatParams = func.params.stream().filter(
            param -> param.type.equals(Types.Float)
        ).collect(Collectors.toList());
        var intParams = func.params.stream().filter(
            param -> !param.type.equals(Types.Float)
        ).collect(Collectors.toList());
        HashMap<Var, Integer> paramMap = new HashMap<>();
        int size = 0;
        for (int i = 8; i < floatParams.size(); i++) {
            paramMap.put(floatParams.get(i), size);
            size += 4;
        }
        for (int i = 8; i < intParams.size(); i++) {
            var param = intParams.get(i);
            if (param.type.equals(Types.Int)) {
                paramMap.put(param, size);
                size += 4;
            }
        }
        size = size / 8 * 8 + 8;
        for (int i = 8; i < intParams.size(); i++) {
            var param = intParams.get(i);
            if (!param.type.equals(Types.Int)) {
                paramMap.put(param, size);
                size += 8;
            }
        }
        for (var param : entryParams.keySet()) {
            var arg = entryParams.get(param);
            switch(param.type) {
                case Type.Float it -> {
                    int idx = floatParams.indexOf(param);
                    var reg = Register.FA(idx);
                    if (reg == null) {
                        asm.flw(arg.getFloatRegister(), Register.Int.FP, paramMap.get(param));
                    } else {
                        asm.fmv(arg.getFloatRegister(), reg);
                    }
                }
                default -> {
                    int idx = intParams.indexOf(param);
                    var reg = Register.A(idx);
                    if (reg == null) {
                        asm.lw(arg.getIntRegister(), Register.Int.FP, paramMap.get(param));
                    } else {
                        asm.mv(arg.getIntRegister(), reg);
                    }
                }
            }
        }
        func.blocks.forEach(this::visit);
    }

    private Label genLabel(String name) {
        return new Label(name);
    }

    public void visit(BasicBlock block) {
        asm.label(genLabel(block.shortName()));
        block.instructions.forEach(this::visit);
    }

    public void visit(Instruction instr) {
        switch(instr) {
            case Instruction.Br i -> {
                var cond = i.getCondition();
                asm.bnez(cond.getIntRegister(), genLabel(i.getTrueTarget().shortName()));
            }
            case Instruction.BrBinary it -> {
                switch (it.op) {
                    case BEQ -> asm.beq(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BNE -> asm.bne(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BLT -> asm.blt(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BLE -> asm.ble(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BGT -> asm.bgt(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BGE -> asm.bge(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BLTU -> asm.bltu(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BLEU -> asm.bleu(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BGTU -> asm.bgtu(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                    case BGEU -> asm.bgeu(
                        it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister(), 
                        genLabel(it.getTrueTarget().shortName())
                    );
                }
            }
            case Instruction.Jmp it -> {
                asm.j(genLabel(it.getTarget().shortName()));
            }
            case Instruction.Call it -> {
                asm.call(genLabel(it.shortName()));
            }
            case Instruction.CallExternal it -> {
                asm.call(genLabel(it.shortName()));
            }
            case Instruction.Alloca it -> {
                //
            }
            case Instruction.Load it -> {
                
            }
            case Instruction.Store it -> {

            }
            case Instruction.GetElemPtr it -> {

            }
            case Instruction.I2F i8 -> {
                
            }
            case Instruction.F2I i9 -> {}
            case Instruction.BitCastI2F i10 -> {}
            case Instruction.BitCastF2I i11 -> {}
            case Instruction.IAdd it -> {
                asm.addw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.ISub it -> {
                asm.subw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.IMul it -> {
                asm.mulw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.IDiv it -> {
                asm.divw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.IMod it -> {
                asm.remw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.INeg it -> {
                asm.neg(it.getIntRegister(), it.lhs.value.getIntRegister());
            }
            case Instruction.FAdd it -> {
                asm.fadd(it.getFloatRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FSub it -> {
                asm.fsub(it.getFloatRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FMul it -> {
                asm.fmul(it.getFloatRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FDiv it -> {
                asm.fdiv(it.getFloatRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FMod it -> {
                unreachable();
            }
            case Instruction.FNeg it -> {
                asm.fneg(it.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.Shl it -> {
                asm.sll(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.Shr it -> {
                asm.srl(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.AShr it -> {
                asm.sra(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.And it -> {
                asm.and(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.Or it -> {
                asm.or(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.Xor it -> {
                asm.xor(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
            }
            case Instruction.Not it -> {
                asm.xori(it.getIntRegister(), it.rhs.value.getIntRegister(), 1);
            }
            case Instruction.IEq it -> {
                asm.subw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
                asm.seqz(it.getIntRegister(), it.getIntRegister());
            }
            case Instruction.INe it -> {
                asm.subw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
                asm.snez(it.getIntRegister(), it.getIntRegister());
            }
            case Instruction.IGt it -> {
                asm.subw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
                asm.sgtz(it.getIntRegister(), it.getIntRegister());
            }
            case Instruction.ILt it -> {
                asm.subw(it.getIntRegister(), it.lhs.value.getIntRegister(), it.rhs.value.getIntRegister());
                asm.sltz(it.getIntRegister(), it.getIntRegister());
            }
            case Instruction.IGe it -> {
                asm.subw(it.getIntRegister(), it.rhs.value.getIntRegister(), it.lhs.value.getIntRegister());
                asm.sltz(it.getIntRegister(), it.getIntRegister());
            }
            case Instruction.ILe it -> {
                asm.subw(it.getIntRegister(), it.rhs.value.getIntRegister(), it.lhs.value.getIntRegister());
                asm.sgtz(it.getIntRegister(), it.getIntRegister());
            }
            case Instruction.FEq it -> {
                asm.feq(it.getIntRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FNe it -> {
                asm.feq(it.getIntRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
                asm.xori(it.getIntRegister(), it.getIntRegister(), 1);
            }
            case Instruction.FGt it -> {
                asm.fle(it.getIntRegister(), it.rhs.value.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.FLt it -> {
                asm.flt(it.getIntRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FGe it -> {
                asm.flt(it.getIntRegister(), it.rhs.value.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.FLe it -> {
                asm.fle(it.getIntRegister(), it.lhs.value.getFloatRegister(), it.rhs.value.getFloatRegister());
            }
            case Instruction.FSqrt it -> {
                asm.fsqrt(it.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.FAbs it -> {
                asm.fabs(it.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.FMin it -> {
                asm.fmin(it.getFloatRegister(), it.rhs.value.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.FMax it -> {
                asm.fmax(it.getFloatRegister(), it.rhs.value.getFloatRegister(), it.lhs.value.getFloatRegister());
            }
            case Instruction.ILi it -> {
                var intval = (ImmediateValue.IntConst) it.imm.value;
                asm.li(it.getIntRegister(), intval.value);
            }
            case Instruction.FLi it -> {
                var fval = (ImmediateValue.FloatConst) it.imm.value;
                asm.li(it.getIntRegister(), Float.floatToRawIntBits(fval.value));
            }
            case Instruction.IMv it -> {
                asm.mv(it.getIntRegister(), it.src.value.getIntRegister());
            }
            case Instruction.FMv it -> {
                asm.fmv(it.getFloatRegister(), it.src.value.getFloatRegister());
            }
            case Instruction.FMulAdd i51 -> {
                unreachable();
            }
            case Instruction.Dummy i52 -> {
                // do nothing
            }
            case null -> {}
            default -> {}
            
        }
    }
*/
}
