package cn.edu.xjtu.sysy.mir.pass.transform;

import java.util.ArrayList;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.Dummy;
import cn.edu.xjtu.sysy.mir.node.LIRInstrHelper;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Var;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.symbol.Types;

public class EnterLIR extends AbstractTransform {
    LIRInstrHelper helper = new LIRInstrHelper();
    public EnterLIR(Pipeline<Module> pipeline) {
        super(pipeline);
    }

    Var curRetVal = null;

    /** 插入epilogue块和callee saved */
    @Override
    public void visit(Function function) {
        // 插入callee saved
        helper.changeBlock(function.entry);
        var dummies = Register.calleeSaved().map(reg -> {
            var dummy = helper.dummyDef(reg.getType());
            // TODO:为dummy结点进行预着色
            function.entry.instructions.addFirst(dummy);
            return dummy;
        }).toArray(Dummy[]::new);
        var epilogue = function.addNewBlock();
        function.epilogue = epilogue;
        helper.changeBlock(epilogue);
        if (!function.funcType.returnType.equals(Types.Void)) {
            curRetVal = function.addNewLocalVar("@.retval", function.funcType.returnType);
            var arg = epilogue.addBlockArgument(curRetVal);
            helper.ret(arg);
        } else {
            helper.ret();
        }
        var dummyUse = helper.dummyUse(dummies);
        System.out.println(dummyUse);
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
    }

    @Override
    public void visit(Instruction instruction) {
        insertLi(instruction);
        switch (instruction) {
            case Instruction.Call it -> insertCallerSaved(it);
            case Instruction.CallExternal it -> insertCallerSaved(it);
            case Instruction.Ret it -> visit(it);
            case Instruction.RetV it -> visit(it);
            default -> {}
        }
    }
    
    public void insertCallerSaved(Instruction call) {
        // 插入caller saved
        var dummies = Register.calleeSaved().map(reg -> {
            var dummy = helper.dummyDef(reg.getType());
            // TODO:为dummy结点进行预着色
            call.frontInsert(dummy);
            return dummy;
        }).toArray(Dummy[]::new);
        call.backInsert(helper.dummyUse(dummies));
    }

    /**
     * 将ret改为jmp到epilogue块
     * @param ret
     */
    public void visit(Instruction.Ret ret) {
        var block = ret.getBlock();
        var func = block.getFunction();
        var jmp = helper.jmp(func.epilogue);
        jmp.putParam(func.epilogue, curRetVal, ret.retVal.value);
        ret.dispose();
    }

    /**
     * 将ret改为jmp到epilogue块
     * @param ret
     */
    public void visit(Instruction.RetV ret) {
        var block = ret.getBlock();
        var func = block.getFunction();
        helper.jmp(func.epilogue);
        ret.dispose();
    }

    /**
     * 插入li指令
     * @param instruction
     */
    public void insertLi(Instruction instruction) {
        var block = instruction.getBlock();
        instruction.usedList.forEach(use -> {
            switch (use.value) {
                case ImmediateValue.IntConst it -> {
                    var li = helper.ili(it.value);
                    instruction.frontInsert(li);
                    use.replaceValue(li);
                }
                case ImmediateValue.FloatConst it -> {
                    var li = helper.fli(it.value);
                    instruction.frontInsert(li);
                    var i2f = helper.i2f(li);
                    instruction.frontInsert(i2f);
                    use.replaceValue(i2f);
                }
                default -> {}
            }

        });
    }
}
