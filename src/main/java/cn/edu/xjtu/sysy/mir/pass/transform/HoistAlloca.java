package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

import java.util.ArrayList;

public final class HoistAlloca extends ModulePass<Void> {

    @Override
    public void visit(Function function) {
        var allocas = new ArrayList<Instruction.Alloca>();
        for (var block : function.blocks) {
            for (var iter = block.instructions.iterator(); iter.hasNext(); ) {
                var instr = iter.next();
                if (instr instanceof Instruction.Alloca alloca) {
                    allocas.add(alloca);
                    iter.remove();
                }
            }
        }

        var entryInstrs = function.entry.instructions;
        for (var alloca : allocas) {
            entryInstrs.addFirst(alloca);
            alloca.setBlock(function.entry);
        }
    }

}
