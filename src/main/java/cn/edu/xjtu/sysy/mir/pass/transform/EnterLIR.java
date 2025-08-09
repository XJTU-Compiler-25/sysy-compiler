package cn.edu.xjtu.sysy.mir.pass.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.Dummy;
import cn.edu.xjtu.sysy.mir.node.LIRInstrHelper;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleTransformer;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.symbol.Types;

public class EnterLIR extends ModuleTransformer {
    LIRInstrHelper helper = new LIRInstrHelper();

    /** 插入epilogue块和callee saved */
    @Override
    public void visit(Function function) {
        // 插入callee saved
        helper.changeBlock(function.entry);
        var dummies = Register.calleeSaved().map(reg -> {
            var dummy = helper.dummyDef(reg.getType());
            dummy.constraint = reg;
            return dummy;
        }).toArray(Dummy[]::new);
        function.entry.instructions.addFirst(helper.dummyDef(dummies));

        var epilogue = function.epilogue;
        function.addBlock(epilogue);
        helper.changeBlock(epilogue);
        if (!function.funcType.returnType.equals(Types.Void)) {
            var arg = epilogue.addBlockArgument(function.funcType.returnType);
            epilogue.terminator = helper.ret(arg);
        } else {
            epilogue.terminator = helper.ret();
        }
        var dummyUse = helper.dummyUse(dummies);
        epilogue.addInstruction(dummyUse);
        function.blocks.forEach(this::visit);
    }
    
    @Override
    public void visit(BasicBlock block) {
        if (block == block.getFunction().epilogue) return;
        helper.changeBlock(block);
        var instrs = new ArrayList<>(block.instructions);
        instrs.forEach(this::visit);
        visit(block.terminator);
        instrs = new ArrayList<>(block.instructions);
        instrs.forEach(this::insertLi);
        insertLi(block.terminator);
    }

    @Override
    public void visit(Instruction instruction) {
        switch (instruction) {
            case Instruction.AbstractCall it -> insertCallerSaved(it);
            case Instruction.GetElemPtr it -> visit(it);
            case Instruction.Ret it -> visit(it);
            case Instruction.RetV it -> visit(it);
            default -> {}
        }
    }

    public void visit(Instruction.GetElemPtr instr) {
        //TODO: 细化GetElemPtr的语义
    }
    
    public void insertCallerSaved(Instruction.AbstractCall call) {
        // 插入caller saved
        var dummies = Register.calleeSaved().map(reg -> {
            var dummy = helper.dummyDef(reg.getType());
            dummy.constraint = reg;
            return dummy;
        }).toArray(Dummy[]::new);
        call.insertBefore(helper.dummyDef(dummies));

        var floatArgs = Arrays.stream(call.args).filter(
            arg -> arg.value.type.equals(Types.Float)
        ).collect(Collectors.toList());

        var intArgs = Arrays.stream(call.args).filter(
            arg -> !arg.value.type.equals(Types.Float)
        ).collect(Collectors.toList());

        int offset = 0;
        for (int i = 0; i < floatArgs.size(); i++) {
            var arg = floatArgs.get(i);
            var fcpy = helper.fcpy(arg.value);
            call.insertBefore(fcpy);
            arg.replaceValue(fcpy);
            var reg = Register.FA(i);
            if (reg != null) {
                fcpy.constraint = reg;
                continue;
            }
            fcpy.constraint = new StackPosition(offset);
            offset += 4;
        }

        for (int i = 0; i < intArgs.size(); i++) {
            var arg = intArgs.get(i);
            if (i <= 7) {
                var icpy = helper.icpy(arg.value);
                call.insertBefore(icpy);
                arg.replaceValue(icpy);
                var reg = Register.A(i);
                icpy.constraint = reg;
                continue;
            }
            if (arg.value.type.equals(Types.Int)) {
                var icpy = helper.icpy(arg.value);
                call.insertBefore(icpy);
                arg.replaceValue(icpy);
                icpy.constraint = new StackPosition(offset);
                offset += 4;
            }
        }

        offset = offset / 8 * 8 + 8;

        for (int i = 8; i < intArgs.size(); i++) {
            var arg = intArgs.get(i);
            if (!arg.value.type.equals(Types.Int)) {
                var icpy = helper.icpy(arg.value);
                call.insertBefore(icpy);
                arg.replaceValue(icpy);
                icpy.constraint = new StackPosition(offset);
                offset += 8;
            }
        }

        call.insertAfter(helper.dummyUse(dummies));
    }

    /**
     * 将ret改为jmp到epilogue块
     * @param ret
     */
    public void visit(Instruction.Ret ret) {
        var block = ret.getBlock();
        var func = block.getFunction();
        var jmp = helper.jmp(func.epilogue);
        jmp.putParam(func.epilogue, func.epilogue.args.getFirst(), ret.retVal.value);
        block.terminator = jmp;
        ret.dispose();
    }

    /**
     * 将ret改为jmp到epilogue块
     * @param ret
     */
    public void visit(Instruction.RetV ret) {
        var block = ret.getBlock();
        var func = block.getFunction();
        block.terminator = helper.jmp(func.epilogue);
        ret.dispose();
    }

    /**
     * 插入li指令
     * @param instruction
     */
    public void insertLi(Instruction instruction) {
        instruction.usedList.forEach(use -> {
            switch (use.value) {
                case ImmediateValue.FloatConst it -> {
                    var li = helper.fli(it.value);
                    instruction.insertBefore(li);
                    var i2f = helper.i2f(li);
                    instruction.insertBefore(i2f);
                    use.replaceValue(i2f);
                }
                default -> {}
            }

        });
    }
}
