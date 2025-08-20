package cn.edu.xjtu.sysy.mir.pass.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.Dummy;
import cn.edu.xjtu.sysy.mir.node.LIRInstrHelper;
import cn.edu.xjtu.sysy.mir.node.StackState;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import static cn.edu.xjtu.sysy.riscv.ValueUtils.calleeSavedUsableRegs;
import static cn.edu.xjtu.sysy.riscv.ValueUtils.callerSavedUsableRegs;
import cn.edu.xjtu.sysy.symbol.Types;

@SuppressWarnings("unchecked")
public class EnterLIR extends ModulePass<Void> {

    private final LIRInstrHelper helper = new LIRInstrHelper();
    private StackState stackState;

    // 插入epilogue块和callee saved
    @Override
    public void visit(Function function) {
        stackState = function.stackState;
        var entry = function.entry;
        helper.changeBlock(entry);

        // 给 entry 中的 alloca 分配空间
        for (var inst : entry.instructions) {
            if (inst instanceof Instruction.Alloca alloca) {
                alloca.position = new StackPosition(stackState.allocate(alloca.allocatedType));
            }
                
        }

        // 插入 callee saved
        var dummies = Arrays.stream(calleeSavedUsableRegs).map(reg -> {
            var dummy = helper.dummyDef(reg.getType());
            dummy.position = reg;
            dummy.precolor = reg;
            return dummy;
        }).toArray(Dummy[]::new);
        for (var dummy : dummies) function.entry.instructions.addFirst(dummy);

        // 插入 epilogue
        var epilogue = function.epilogue;
        function.addBlock(epilogue);
        helper.changeBlock(epilogue);
        if (function.funcType.returnType != Types.Void) {
            var arg = epilogue.addBlockArgument(function.funcType.returnType);
            epilogue.terminator = helper.ret(arg);
        } else epilogue.terminator = helper.ret();

        var dummyUse = helper.dummyUse(dummies);
        epilogue.insertAtLast(dummyUse);
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

        var floatArgs = call.args.stream().filter(
            arg -> arg.value.type == Types.Float
        ).toList();

        var intArgs = call.args.stream().filter(
            arg -> arg.value.type != Types.Float
        ).toList();

        var usedRegs = new HashMap<Register, Instruction>();
        for (int i = 0; i < floatArgs.size(); i++) {
            var arg = floatArgs.get(i);
            var fcpy = helper.fcpy(arg.value);
            call.insertBefore(fcpy);
            arg.replaceValue(fcpy);
            var reg = Register.FA(i);
            if (reg != null) {
                fcpy.position = reg;
                fcpy.precolor = reg;
                usedRegs.put(reg, fcpy);
                continue;
            }
            fcpy.position = new StackPosition(stackState.allocate(Types.Float), true);
        }

        for (int i = 0; i < intArgs.size(); i++) {
            var arg = intArgs.get(i);
            if (i <= 7) {
                var icpy = helper.icpy(arg.value);
                call.insertBefore(icpy);
                arg.replaceValue(icpy);
                icpy.position = Register.A(i);
                icpy.precolor = Register.A(i);
                usedRegs.put(Register.A(i), icpy);
                continue;
            }
            if (arg.value.type == Types.Int) {
                var icpy = helper.icpy(arg.value);
                call.insertBefore(icpy);
                arg.replaceValue(icpy);
                icpy.position = new StackPosition(stackState.allocate(Types.Int), true);
            }
        }

        for (int i = 8; i < intArgs.size(); i++) {
            var arg = intArgs.get(i);
            if (arg.value.type != Types.Int) {
                var icpy = helper.icpy(arg.value);
                call.insertBefore(icpy);
                arg.replaceValue(icpy);
                icpy.position = new StackPosition(stackState.allocate(arg.value.type), true);
            }
        }

        var dummies = Arrays.stream(callerSavedUsableRegs).map(reg -> { 
            if (usedRegs.containsKey(reg)) {
                return usedRegs.get(reg);
            }
            var dummy = helper.dummyDef(reg.getType());
            dummy.position = reg;
            dummy.precolor = reg;
            return dummy;
        }).toArray(Instruction[]::new);
        for (var dummy : dummies) {
            if (dummy instanceof Dummy) call.insertBefore(dummy);
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
        jmp.putParam(func.epilogue, func.epilogue.args.getFirst(), ret.getRetVal());
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
