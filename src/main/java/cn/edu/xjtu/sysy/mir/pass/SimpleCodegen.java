package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.riscv.node.Global;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.Register.Float;
import cn.edu.xjtu.sysy.riscv.node.AsmWriter;
import cn.edu.xjtu.sysy.riscv.node.MachineBasicBlock;
import cn.edu.xjtu.sysy.riscv.node.MachineFunc;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import static cn.edu.xjtu.sysy.riscv.Register.Int.*;
import static cn.edu.xjtu.sysy.riscv.Register.Float.*;
import static cn.edu.xjtu.sysy.util.Assertions.*;

public final class SimpleCodegen extends ModulePass<Void> {

    private static final Int[] iArgs = { A0, A1, A2, A3, A4, A5, A6, A7 };
    private static final Float[] fArgs = { FA0, FA1, FA2, FA3, FA4, FA5, FA6, FA7 };

    private static final Int iTemp0 = T0;
    private static final Int iTemp1 = T1;
    private static final Int iTemp2 = T2;
    private static final Float fTemp0 = FT1;
    private static final Float fTemp1 = FT2;
    private static final Float fTemp2 = FT3;

    private Function curFunc;
    private MachineFunc curMFunc;
    private BasicBlock nextBlock;

    // 为每个值预分配的栈地址
    private final HashMap<Value, Integer> spillLoc = new HashMap<>();
    private int frameSize = 0;

    private final AsmWriter asm = new AsmWriter();
    private final HashMap<GlobalVar, Global> globals = new HashMap<>();
    private final HashMap<Function, MachineFunc> functions = new HashMap<>();

    private static final Int[] iCacheRegs = { S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11 };
    private static final Float[] fCacheRegs = { FS0, FS1, FS2, FS3, FS4, FS5, FS6, FS7, FS8, FS9, FS10, FS11 };

    // LRU states
    private final ArrayDeque<Value> iCache = new ArrayDeque<>(iCacheRegs.length);
    private final ArrayDeque<Value> fCache = new ArrayDeque<>(fCacheRegs.length);
    private final HashMap<Value, Register> valueReg = new HashMap<>();

    public String output() {
        var sb = new StringBuilder();
        for (var global : globals.values()) sb.append(global.toString()).append('\n');
        for (var func : functions.values()) sb.append(func.toString()).append('\n');
        return sb.toString();
    }

    @Override
    public void visit(Module module) {
        globals.clear();
        var initVals = module.globalVarInitValues;
        for (var var : module.getGlobalVars()) {
            var global = new Global(var.shortName(), var.varType, initVals.get(var));
            globals.put(var, global);
        }
        for (var func : module.getFunctions()) {
            var rvFunc = new MachineFunc(func.shortName(), func.funcType);
            functions.put(func, rvFunc);
            visit(func);
        }
    }

    @Override
    public void visit(Function function) {
        curFunc = function;
        curMFunc = functions.get(function);

        spillLoc.clear();
        valueReg.clear();
        iCache.clear();
        fCache.clear();

        layoutStackFrame();
        emitPrologue();

        var epilogue = function.epilogue;
        var blocks = function.blocks.stream()
                .filter(it -> it != epilogue)
                .sorted(Comparator.comparingInt(it -> it.id))
                .toArray(BasicBlock[]::new);

        for (int i = 0, size = blocks.length; i < size; i++) {
            var block = blocks[i];
            if (block == epilogue) continue;

            var machineBlock = new MachineBasicBlock(block.shortName());
            curMFunc.addBlock(machineBlock);
            asm.changeBlock(machineBlock);

            nextBlock = i + 1 < size ? blocks[i + 1] : epilogue;
            visit(block);
        }

        emitEpilogue();
    }


    private void moveTo(Value value, Int dest) {
        if (valueReg.containsKey(value)) {
            var reg = (Int) valueReg.get(value);
            if (dest != reg) asm.mv(dest, reg);
        } else if (value instanceof ImmediateValue.IntConst ic) asm.li(dest, ic.value);
        else if (value instanceof GlobalVar gv) asm.la(dest, gv.shortName());
        else {
            int offset = spillLoc.get(value);
            var type = value.type;
            if (type == Types.Int) lw(dest, offset);
            else if (type instanceof Type.Pointer || type instanceof Type.Array) ld(dest, offset);
        }
    }

    private void moveTo(Value value, Float dest) {
        if (valueReg.containsKey(value)) {
            var reg = (Float) valueReg.get(value);
            if (dest != reg) asm.fmv(dest, reg);
        } else if (value instanceof ImmediateValue.FloatConst fc) asm.li(iTemp0, fc.value).fmv_w_x(dest, iTemp0);
        else if (value instanceof ImmediateValue.IntConst ic) asm.li(iTemp0, ic.value).fcvt_s_l(dest, iTemp0);
        else if (value instanceof GlobalVar gv) asm.la(iTemp0, gv.shortName()).fmv_w_x(dest, iTemp0);
        else {
            int offset = spillLoc.get(value);
            flw(dest, offset);
        }
    }

    private Int iAlloc(Value value) {
        requires(value.type != Types.Float);
        if (valueReg.containsKey(value)) return (Int) valueReg.get(value);
        if (iCache.size() >= iCacheRegs.length) iSpill();
        iCache.addFirst(value);
        for (var reg : iCacheRegs) {
            if (!valueReg.containsValue(reg)) {
                valueReg.put(value, reg);
                return reg;
            }
        }
        return unreachable();
    }

    private Float fAlloc(Value value) {
        requires(value.type == Types.Float);
        if (valueReg.containsKey(value)) return (Float) valueReg.get(value);
        if (fCache.size() >= fCacheRegs.length) fSpill();
        fCache.addFirst(value);
        for (var reg : fCacheRegs) {
            if (!valueReg.containsValue(reg)) {
                valueReg.put(value, reg);
                return reg;
            }
        }
        return unreachable();
    }

    private void iSpill() {
        var value = iCache.removeLast();
        var reg = (Int) valueReg.remove(value);
        int offset = spillLoc.get(value);
        sd(reg, offset);
    }

    private void fSpill() {
        var value = fCache.removeLast();
        var reg = (Float) valueReg.remove(value);
        int offset = spillLoc.get(value);
        fsw(reg, offset);
    }

    private void layoutStackFrame() {
        var function = curFunc;
        var stackFrame = function.stackState;

        // RA
        stackFrame.allocate(8, 8);
        // old FP
        stackFrame.allocate(8, 8);
        // int callee-saved
        for (int i = 0; i < iCacheRegs.length; i++) stackFrame.allocate(8, 8);
        // float callee-saved
        for (int i = 0; i < fCacheRegs.length; i++) stackFrame.allocate(8, 8);

        for (var bb : function.blocks) {
            for (var arg : bb.args) {
                int offset = stackFrame.allocate(arg.type);
                spillLoc.put(arg, -offset);
            }

            for (var inst : bb.instructions) {
                if (inst instanceof Alloca alloca) {
                    int offset = stackFrame.allocate(alloca.allocatedType);
                    spillLoc.put(inst, -offset);
                }

                else if (inst.producingValue()) {
                    if (!spillLoc.containsKey(inst)) {
                        int offset = stackFrame.allocate(inst.type);
                        spillLoc.put(inst, -offset);
                    }
                }
            }
        }
        // Final ABI alignment
        stackFrame.pad(16);
        frameSize = stackFrame.size();
    }

    private void emitPrologue() {
        if (frameSize == 0) return;

        var machinePrologue = new MachineBasicBlock("prologue");
        curMFunc.addBlock(machinePrologue);
        asm.changeBlock(machinePrologue);

        if (isImmOk(frameSize)) {
            asm.addi(SP, SP, -frameSize)
                    .sd(RA, SP, frameSize - 8)
                    .sd(FP, SP, frameSize - 16)
                    .addi(FP, SP, frameSize);
            // Save callee-saved registers
            int offset = frameSize - 24;
            for (int i = 0; i < iCacheRegs.length; i++) {
                var reg = iCacheRegs[i];
                asm.sd(reg, SP, offset);
                offset -= 8;
            }
            for (int i = 0; i < fCacheRegs.length; i++) {
                var reg = fCacheRegs[i];
                asm.fsw(reg, SP, offset);
                offset -= 8;
            }
        } else {
            asm.li(iTemp0, frameSize)
                    .sub(SP, SP, iTemp0)
                    .sd(RA, SP, 8)
                    .sd(FP, SP, 0);
            int offset = 16;
            for (int i = 0; i < iCacheRegs.length; i++) {
                var reg = iCacheRegs[i];
                asm.sd(reg, SP, offset);
                offset += 8;
            }
            for (int i = 0; i < fCacheRegs.length; i++) {
                var reg = fCacheRegs[i];
                asm.fsw(reg, SP, offset);
                offset += 8;
            }
        }
    }

    private void emitEpilogue() {
        var epilogue = curFunc.epilogue;
        var machineEpilogue = new MachineBasicBlock(epilogue.shortName());
        curMFunc.addBlock(machineEpilogue);

        if (frameSize == 0) return;

        asm.changeBlock(machineEpilogue);
        var term = epilogue.terminator;
        if (term instanceof Ret it) {
            var retVal = it.getRetVal();
            if (retVal.type != Types.Float) moveTo(retVal, A0);
            else moveTo(retVal, FA0);
        }

        if (isImmOk(frameSize)) {
            // Restore callee-saved registers
            int offset = frameSize - 24;
            for (int i = 0; i < iCacheRegs.length; i++) {
                var reg = iCacheRegs[i];
                asm.ld(reg, SP, offset);
                offset -= 8;
            }
            for (int i = 0; i < fCacheRegs.length; i++) {
                var reg = fCacheRegs[i];
                asm.flw(reg, SP, offset);
                offset -= 8;
            }
            asm.ld(RA, SP, frameSize - 8)
                    .ld(FP, SP, frameSize - 16)
                    .addi(SP, SP, frameSize);
        } else {
            int offset = 16;
            for (int i = 0; i < iCacheRegs.length; i++) {
                var reg = iCacheRegs[i];
                asm.ld(reg, SP, offset);
                offset += 8;
            }
            for (int i = 0; i < fCacheRegs.length; i++) {
                var reg = fCacheRegs[i];
                asm.flw(reg, SP, offset);
                offset += 8;
            }
            asm.ld(RA, SP, 8)
                    .ld(FP, SP, 0)
                    .li(iTemp0, frameSize)
                    .add(SP, SP, iTemp0);
        }

        asm.ret();
    }

    @Override
    public void visit(BasicBlock block) {
        for (var instruction : block.getInstructionsAndTerminator()) def(instruction);
    }

    private void def(Instruction inst) {
        switch(inst) {
            case Alloca _ -> { } // 已经在 layout 里面处理了
            case Store it -> {
                moveTo(it.getAddress(), iTemp0);
                var val = it.getStoreVal();
                var valType = val.type;
                if (valType == Types.Int) {
                    moveTo(val, iTemp1);
                    asm.sw(iTemp1, iTemp0, 0);
                } else if (valType == Types.Float) {
                    moveTo(val, fTemp2);
                    asm.fsw(fTemp0, iTemp0, 0);
                } else if (valType instanceof Type.Pointer || valType instanceof Type.Array) {
                    moveTo(val, iTemp1);
                    asm.sd(iTemp1, iTemp0, 0);
                } else unreachable();
            }
            case Load it -> {
                var addr = it.getAddress();
                moveTo(addr, iTemp0);
                var destType = it.type;
                if (destType == Types.Int) {
                    var destReg = iAlloc(it);
                    asm.lw(destReg, iTemp0, 0);
                } else if (destType == Types.Float) {
                    var destReg = fAlloc(it);
                    asm.flw(destReg, iTemp0, 0);
                } else if (destType instanceof Type.Pointer || destType instanceof Type.Array) {
                    var destReg = iAlloc(it);
                    asm.ld(destReg, iTemp0, 0);
                }
                else unreachable();
            }

            case GetElemPtr it -> {
                var indexCount = it.getIndexCount();
                var indices = it.getIndices();
                var basePtr = it.getBasePtr();

                moveTo(basePtr, iTemp0);
                var strides = Types.strides(basePtr.type);
                for (int i = 0; i < indexCount; i++) {
                    var index = indices.get(i);
                    var stride = strides[i];

                    if (index instanceof ImmediateValue.IntConst ic) {
                        var val = ic.value * stride;
                        if (val != 0) {
                            if (isImmOk(val)) asm.addi(iTemp0, iTemp0, val);
                            else asm.li(iTemp1, val).add(iTemp0, iTemp0, iTemp1);
                        }
                    } else {
                        moveTo(index, iTemp1);
                        asm.li(iTemp2, stride)
                                .mul(iTemp1, iTemp1, iTemp2)
                                .add(iTemp0, iTemp0, iTemp1);
                    }
                }
            }

            case AbstractBinary binary -> {
                var lhs = binary.getLhs();
                var rhs = binary.getRhs();
                if(lhs.type != Types.Float) moveTo(lhs, iTemp0);
                else moveTo(rhs, fTemp0);
                if (rhs.type != Types.Float) moveTo(rhs, iTemp1);
                else moveTo(lhs, fTemp1);

                var destIsInt = binary.type != Types.Float;
                Int iDestReg = destIsInt ? iAlloc(binary) : null;
                Float fDestReg = destIsInt ? null : fAlloc(binary);

                switch (binary) {
                    case IAdd _ -> asm.addw(iDestReg, iTemp0, iTemp1);
                    case ISub _ -> asm.subw(iDestReg, iTemp0, iTemp1);
                    case IMul _ -> asm.mulw(iDestReg, iTemp0, iTemp1);
                    case IDiv _ -> asm.divw(iDestReg, iTemp0, iTemp1);
                    case IMod _ -> asm.remw(iDestReg, iTemp0, iTemp1);
                    case FAdd _ -> asm.fadd(fDestReg, fTemp0, fTemp1);
                    case FSub _ -> asm.fsub(fDestReg, fTemp0, fTemp1);
                    case FMul _ -> asm.fmul(fDestReg, fTemp0, fTemp1);
                    case FDiv _ -> asm.fdiv(fDestReg, fTemp0, fTemp1);
                    case FMod _ -> {}

                    case IEq _ -> asm.xor(iDestReg, iTemp0, iTemp1).seqz(iDestReg, iDestReg);
                    case INe _ -> asm.xor(iDestReg, iTemp0, iTemp1).snez(iDestReg, iDestReg);
                    case FEq _ -> asm.feq(iDestReg, fTemp0, fTemp1);
                    case FNe _ -> asm.feq(iDestReg, fTemp0, fTemp1).snez(iDestReg, iDestReg);

                    case IGe _ -> asm.slt(iDestReg, iTemp0, iTemp1).snez(iDestReg, iDestReg);
                    case IGt _ -> asm.slt(iDestReg, iTemp1, iTemp0);
                    case ILe _ -> asm.slt(iDestReg, iTemp1, iTemp0).snez(iDestReg, iDestReg);
                    case ILt _ -> asm.slt(iDestReg, iTemp0, iTemp1);

                    case FGe _ -> asm.fle(iDestReg, fTemp1, fTemp0);
                    case FGt _ -> asm.flt(iDestReg, fTemp1, fTemp0);
                    case FLe _ -> asm.fle(iDestReg, fTemp0, fTemp1);
                    case FLt _ -> asm.flt(iDestReg, fTemp0, fTemp1);

                    case And _ -> asm.and(iDestReg, iTemp0, iTemp1);
                    case Or _ -> asm.or(iDestReg, iTemp0, iTemp1);
                    case Xor _ -> asm.xor(iDestReg, iTemp0, iTemp1);

                    case Shl _ -> asm.sllw(iDestReg, iTemp0, iTemp1);
                    case Shr _ -> asm.srlw(iDestReg, iTemp0, iTemp1);
                    case AShr _ -> asm.sraw(iDestReg, iTemp0, iTemp1);

                    default -> unsupported(binary);
                }
            }

            case AbstractUnary unary -> {
                var op = unary.getOperand();
                if (op.type != Types.Float) moveTo(op, iTemp0);
                else moveTo(op, fTemp0);

                var destIsInt = unary.type != Types.Float;
                Int iDestReg = destIsInt ? iAlloc(unary) : null;
                Float fDestReg = destIsInt ? null : fAlloc(unary);
                switch (unary) {
                    case INeg _ -> asm.negw(iDestReg, iTemp0);
                    case FNeg _ -> asm.fneg(fDestReg, fTemp0);
                    case Not _ -> asm.snez(iDestReg, iTemp0);
                    case I2F _ -> asm.fcvt_s_l(fDestReg, iTemp0);
                    case F2I _ -> asm.fcvt_l_s(iDestReg, fTemp0);
                    case BitCastI2F _ -> asm.fmv_w_x(fDestReg, iTemp0);
                    case BitCastF2I _ -> asm.fmv_x_w(iDestReg, fTemp0);
                    default ->  unsupported(unary);
                }
            }

            case IMv it -> {
                var src = it.getSrc();
                var type = src.type;
                var dst = (BlockArgument) it.dst;
                moveTo(src, iTemp0);
                var offset = spillLoc.get(dst);
                if (type == Types.Int) sw(iTemp0, offset);
                else if (type instanceof Type.Pointer || type instanceof Type.Array) sd(iTemp0, offset);
            }
            case FMv it -> {
                var src = it.getSrc();
                var dst = (BlockArgument) it.dst;
                moveTo(src, fTemp0);
                var offset = spillLoc.get(dst);
                fsw(fTemp0, offset);
            }

            case Jmp it -> {
                var target = it.getTarget();
                if (nextBlock != target) asm.j(target.shortName());
            }
            case Br it -> {
                var cond = it.getCondition();
                moveTo(cond, iTemp0);
                var tp = it.getTrueTarget();
                var fp = it.getFalseTarget();
                asm.bnez(iTemp0, tp.shortName());
                if (nextBlock != fp) asm.j(fp.shortName());
            }

            case AbstractCall it -> {
                var args = it.getArgs().toArray(Value[]::new);

                int intArgCount = 0;
                int floatArgCount = 0;
                for (var arg : args) {
                    if (arg.type != Types.Float) intArgCount++;
                    else floatArgCount++;
                }
                var stackSpaceNeeded = 0;
                if (intArgCount > 8) stackSpaceNeeded += intArgCount - 8;
                if (floatArgCount > 8) stackSpaceNeeded += floatArgCount - 8;
                stackSpaceNeeded *= 8;
                if (stackSpaceNeeded > 0) {
                    if (isImmOk(stackSpaceNeeded)) asm.addi(SP, SP, -stackSpaceNeeded);
                    else asm.li(iTemp0, stackSpaceNeeded).sub(SP, SP, iTemp0);
                }

                intArgCount = 0;
                floatArgCount = 0;
                int currentStackOffset = 0;

                for (var arg : args) {
                    if (arg.type != Types.Float) {
                        if (intArgCount < 8) {
                            moveTo(arg, iArgs[intArgCount++]);
                        } else {
                            moveTo(arg, iTemp0);
                            if (isImmOk(currentStackOffset)) asm.sd(iTemp0, SP, currentStackOffset);
                            else asm.li(iTemp1, currentStackOffset).add(iTemp1, SP, iTemp1).sd(iTemp0, iTemp1, 0);
                            currentStackOffset += 8;
                        }
                    } else { // 浮点数
                        if (floatArgCount < 8) {
                            moveTo(arg, fArgs[floatArgCount++]);
                        } else {
                            moveTo(arg, fTemp0);
                            if (isImmOk(currentStackOffset)) asm.fsw(fTemp0, SP, currentStackOffset);
                            else asm.li(iTemp0, currentStackOffset).add(iTemp0, SP, iTemp0).fsw(fTemp0, iTemp0, 0);
                            currentStackOffset += 8;
                        }
                    }
                }

                asm.call(it.getLabel());

                if (stackSpaceNeeded > 0) {
                    if (isImmOk(stackSpaceNeeded)) asm.addi(SP, SP, stackSpaceNeeded);
                    else asm.li(iTemp0, stackSpaceNeeded).add(SP, SP, iTemp0);
                }
            }

            default -> unsupported(inst);
        }
    }

    private static boolean isImmOk(int value) {
        return value >= -2048 && value <= 2047;
    }

    private void fsw(Float src, int offset) {
        if (isImmOk(offset)) asm.fsw(src, FP, offset);
        else asm.li(iTemp2, offset).add(iTemp2, FP, iTemp2).fsw(src, iTemp2, 0);
    }

    private void sw(Int src, int offset) {
        if (isImmOk(offset)) asm.sw(src, FP, offset);
        else asm.li(iTemp2, offset).add(iTemp2, FP, iTemp2).sw(src, iTemp2, 0);
    }

    private void sd(Int src, int offset) {
        if (isImmOk(offset)) asm.sd(src, FP, offset);
        else asm.li(iTemp2, offset).add(iTemp2, FP, iTemp2).sd(src, iTemp2, 0);
    }

    private void flw(Float dest, int offset) {
        if (isImmOk(offset)) asm.flw(dest, FP, offset);
        else asm.li(iTemp2, offset).add(iTemp2, FP, iTemp2).flw(dest, iTemp2, 0);
    }

    private void lw(Int dest, int offset) {
        if (isImmOk(offset)) asm.lw(dest, FP, offset);
        else asm.li(iTemp2, offset).add(iTemp2, FP, iTemp2).lw(dest, iTemp2, 0);
    }

    private void ld(Int dest, int offset) {
        if (isImmOk(offset)) asm.ld(dest, FP, offset);
        else asm.li(iTemp2, offset).add(iTemp2, FP, iTemp2).ld(dest, iTemp2, 0);
    }

}
