package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.symbol.Types;

public final class SimpleExitSSA extends ModulePass<Void> {

    private static final LIRInstrHelper helper = new LIRInstrHelper();

    @Override
    public void visit(Function function) {
        for (var block : function.blocks) {
            switch (block.terminator) {
                case Instruction.AbstractBr br -> {
                    var tp = br.getTrueTarget();
                    helper.changeBlock(tp);
                    br.trueParams.forEach((destArg, value) -> {
                        var srcValue = value.value;
                        if (srcValue instanceof ImmediateValue.Undefined) return;

                        if (destArg.type == Types.Float) block.insertAtLast(helper.fmv(destArg, srcValue));
                        else block.insertAtFirst(helper.imv(destArg, srcValue));
                    });
                    br.trueParams.clear();

                    var fp = br.getFalseTarget();
                    helper.changeBlock(fp);
                    br.falseParams.forEach((destArg, value) -> {
                        var srcValue = value.value;
                        if (srcValue instanceof ImmediateValue.Undefined) return;

                        if (destArg.type == Types.Float) block.insertAtLast(helper.fmv(destArg, srcValue));
                        else block.insertAtFirst(helper.imv(destArg, srcValue));
                    });
                    br.falseParams.clear();

                    helper.changeBlock(null);
                }
                case Instruction.Jmp jmp -> {
                    helper.changeBlock(block);
                    jmp.params.forEach((destArg, value) -> {
                        var srcValue = value.value;
                        if (srcValue instanceof ImmediateValue.Undefined) return;

                        if (destArg.type == Types.Float) block.insertAtLast(helper.fmv(destArg, srcValue));
                        else block.insertAtLast(helper.imv(destArg, srcValue));
                    });
                    jmp.params.clear();

                    helper.changeBlock(null);
                }
                default -> { }
            }
        }
    }

}
