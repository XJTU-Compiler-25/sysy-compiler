package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.symbol.Types;

public final class SimpleEnterLIR extends ModulePass<Void> {

    private final LIRInstrHelper helper = new LIRInstrHelper();

    @Override
    public void visit(Function function) {
        var epilogue = function.epilogue;
        var retType = function.funcType.returnType;
        if (retType != Types.Void) {
            var arg = epilogue.addBlockArgument(retType);
            epilogue.setTerminator(helper.ret(arg));
        } else epilogue.setTerminator(helper.ret());

        function.blocks.forEach(this::visit);

        function.addBlock(epilogue);
    }

    @Override
    public void visit(BasicBlock block) {
        helper.changeBlock(block);
        visit(block.terminator);
    }

    public void visit(Instruction.Terminator instruction) {
        switch (instruction) {
            case Ret ret -> {
                var block = ret.getBlock();
                var func = block.getFunction();
                var epilogue = func.epilogue;
                var jmp = helper.jmp(epilogue);
                jmp.putParam(epilogue, epilogue.args.getFirst(), ret.getRetVal());
                block.terminator = jmp;
                ret.dispose();
            }
            case RetV ret -> {
                var block = ret.getBlock();
                var func = block.getFunction();
                block.terminator = helper.jmp(func.epilogue);
                ret.dispose();
            }
            default -> {}
        }
    }

}
