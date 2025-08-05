package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.LIRInstrHelper;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.symbol.Type;

public class ExitSSA extends AbstractTransform {

    LIRInstrHelper helper = new LIRInstrHelper();
    public ExitSSA(Pipeline<Module> pipeline) {
        super(pipeline);
    }

    @Override
    public void visit(Instruction instr) {
        switch (instr) {
            case Instruction.Br br -> visit(br);
            case Instruction.BrBinary br -> visit(br);
            case Instruction.Jmp br -> visit(br);
            default -> {}
        }
    }

    public void visit(Instruction.Br br) {
        var block = br.getBlock();
        var func = block.getFunction();
        var trueParams = br.trueParams;
        var trueTarget = br.getTrueTarget();
        var trueArgs = trueTarget.args;
        var trueBlock = func.addNewBlock();
        br.replaceTrueTarget(trueBlock);
        helper.changeBlock(trueBlock);
        for (var key : trueArgs.keySet()) {
            var param = trueParams.get(key);
            var arg = trueArgs.get(key);
            trueArgs.remove(key);
            trueParams.remove(key);
            switch (arg.type) {
                case Type.Float _ -> helper.fmv(arg, param.value);
                default -> helper.imv(arg, param.value);
            }
        }
        helper.jmp(trueTarget);

        var falseParams = br.falseParams;
        var falseTarget = br.getFalseTarget();
        var falseArgs = falseTarget.args;
        var falseBlock = func.addNewBlock();
        br.replaceFalseTarget(falseBlock);
        helper.changeBlock(falseBlock);
        for (var key : falseArgs.keySet()) {
            var param = falseParams.get(key);
            var arg = falseArgs.get(key);
            falseArgs.remove(key);
            falseParams.remove(key);
            switch (arg.type) {
                case Type.Float _ -> helper.fmv(arg, param.value);
                default -> helper.imv(arg, param.value);
            }
        }
        helper.jmp(falseTarget);
    }

    public void visit(Instruction.BrBinary br) {
        var block = br.getBlock();
        var func = block.getFunction();
        var trueParams = br.trueParams;
        var trueTarget = br.getTrueTarget();
        var trueArgs = trueTarget.args;
        var trueBlock = func.addNewBlock();
        br.replaceTrueTarget(trueBlock);
        helper.changeBlock(trueBlock);
        for (var key : trueArgs.keySet()) {
            var param = trueParams.get(key);
            var arg = trueArgs.get(key);
            trueArgs.remove(key);
            trueParams.remove(key);
            switch (arg.type) {
                case Type.Float _ -> helper.fmv(arg, param.value);
                default -> helper.imv(arg, param.value);
            }
        }
        helper.jmp(trueTarget);

        var falseParams = br.falseParams;
        var falseTarget = br.getFalseTarget();
        var falseArgs = falseTarget.args;
        var falseBlock = func.addNewBlock();
        br.replaceFalseTarget(falseBlock);
        helper.changeBlock(falseBlock);
        for (var key : falseArgs.keySet()) {
            var param = falseParams.get(key);
            var arg = falseArgs.get(key);
            falseArgs.remove(key);
            falseParams.remove(key);
            switch (arg.type) {
                case Type.Float _ -> helper.fmv(arg, param.value);
                default -> helper.imv(arg, param.value);
            }
        }
        helper.jmp(falseTarget);
    }

    public void visit(Instruction.Jmp jmp) {
        var params = jmp.params;
        var target = jmp.getTarget();
        var args = target.args;
        for (var key : args.keySet()) {
            var param = params.get(key);
            var arg = args.get(key);
            params.remove(key);
            args.remove(key);
            switch (arg.type) {
                case Type.Float _ -> helper.fmv(arg, param.value);
                default -> helper.imv(arg, param.value);
            }
        }
    }
}
