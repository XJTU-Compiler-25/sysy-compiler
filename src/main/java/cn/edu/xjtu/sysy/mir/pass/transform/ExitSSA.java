package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static cn.edu.xjtu.sysy.riscv.ValueUtils.phiElimFloatReg;
import static cn.edu.xjtu.sysy.riscv.ValueUtils.phiElimIntReg;

// phi elimination pass
public final class ExitSSA extends ModulePass<Void> {

    private static final InstructionHelper hp1 = new InstructionHelper();
    private static final LIRInstrHelper hp2 = new LIRInstrHelper();

    private Function currentFunction;
    @Override
    public void visit(Function function) {
        currentFunction = function;

        splitCriticalEdges();
        removePhi();
    }

    private void splitCriticalEdges() {
        for (var block : new ArrayList<>(currentFunction.blocks)) {
            var terminator = block.terminator;
            switch (terminator) {
                case Instruction.AbstractBr br -> {
                    splitEdge(block, br.getTrueTarget());
                    splitEdge(block, br.getFalseTarget());
                }
                case Instruction.Jmp jmp -> splitEdge(block, jmp.getTarget());
                default -> { }
            }
        }
    }

    private void splitEdge(BasicBlock from, BasicBlock to) {
        // 关键边条件：from 有多个 successor 或 to 有多个 predecessor
        if (CFGAnalysis.getSuccBlocksOf(from).size() > 1 || CFGAnalysis.getPredBlocksOf(to).size() > 1) {
            // 插入中间空块
            var mid = new BasicBlock(currentFunction);
            currentFunction.addBlock(mid);

            // 调整 CFG
            hp1.changeBlock(mid);
            hp1.insertJmp(to);
        }
    }

    private void removePhi() {
        // 先实现将 call conv 传递到 entry args
        processEntryArgs();

        // 然后将每个块的 phi 指令转换为对应的 move 指令
        for (var block : currentFunction.blocks) {
            block.args.clear();
            switch (block.terminator) {
                case Instruction.AbstractBr br -> {
                    processBranchPhi(br.getTrueTarget(), br.trueParams);
                    processBranchPhi(br.getFalseTarget(), br.falseParams);
                }
                case Instruction.Jmp jmp -> {
                    processBranchPhi(block, jmp.params);
                }
                default -> { }
            }
        }
    }

    private void processEntryArgs() {
        var stackState = currentFunction.stackState;
        var entry = currentFunction.entry;
        hp2.changeBlock(entry);

        var entryParams = entry.args;
        var intParams = new ArrayList<BlockArgument>();
        var floatParams = new ArrayList<BlockArgument>();
        for (var param : entryParams) {
            if (param.type != Types.Float) intParams.add(param);
            else floatParams.add(param);
        }

        for (int i = 0; i < intParams.size(); i++) {
            var param = intParams.get(i);
            var type = param.type;
            if (i < 8) {
                var reg = Register.A(i);
                var dummy = hp2.dummyDef(type);
                dummy.position = reg;
                var mv = hp2.imv(param, dummy);
                entry.insertAtFirst(mv);
            } else {
                var dummy = hp2.dummyDef(type);
                dummy.position = new StackPosition(stackState.allocate(type));
                var load = hp2.imv(param, dummy);
                entry.insertAtFirst(load);
            }
        }
        for (int i = 0; i < floatParams.size(); i++) {
            var param = floatParams.get(i);
            var type = param.type;
            if (i < 8) {
                var reg = Register.FA(i);
                var dummy = hp2.dummyDef(type);
                dummy.position = reg;
                var mv = hp2.fmv(param, dummy);
                entry.insertAtFirst(mv);
            } else {
                var dummy = hp2.dummyDef(type);
                dummy.position = new StackPosition(stackState.allocate(type));
                var load = hp2.fmv(param, dummy);
                entry.insertAtFirst(load);
            }
        }
    }

    private void processBranchPhi(BasicBlock target, Map<BlockArgument, Use> params) {
        if (params.isEmpty()) return;
        hp2.changeBlock(target);

        // 使用 phi-elim 临时寄存器避免循环依赖
        var tempValues = new HashMap<Value, Value>();

        // 如果 value 会导致寄存器循环依赖，分配 phi-elim 临时寄存器
        params.forEach((arg, use) -> {
            var value = use.value;
            var isInt = value.type != Types.Float;

            // 用 dummy 占用 phi-elim 寄存器
            var temp = hp2.dummyDef(value.type);
            temp.position = isInt ? phiElimIntReg : phiElimFloatReg;
            tempValues.put(value, temp);

            // 先搬到临时 Value
            var mvToTemp = isInt ? hp2.imv(temp, value) : hp2.fmv(temp, value);
            target.insertAtFirst(temp);
            target.insertAtFirst(mvToTemp);
        });

        params.forEach((arg, use) -> {
            var value = use.value;
            var isInt = value.type != Types.Float;

            var temp = tempValues.get(value);
            var mvToArg = isInt ? hp2.imv(arg, temp) : hp2.fmv(arg, temp);
            target.insertAtFirst(mvToArg);
        });

        params.clear();
    }

}
