package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

public final class GlobalOpt extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(Module module) {
        removeUnusedGlobalVars(module);
        localize(module);
    }

    // aggressively，因为我们编译的东西都不会被外部调用
    // 1. 完全没被 use 的全局变量
    // 2. 从来没被 load 过的全局变量
    public static void removeUnusedGlobalVars(Module module) {
        var toRemove = new ArrayList<GlobalVar>();

        for (var iterator = module.getGlobalVars().iterator(); iterator.hasNext(); ) {
            var var = iterator.next();

            if (var.notUsed()) {
                iterator.remove();
                module.globalVarInitValues.remove(var);
            } else if (var.usedBy.stream().noneMatch(it ->
                    it.user instanceof Instruction.Load || it.user instanceof Instruction.GetElemPtr)) {
                iterator.remove();
                module.globalVarInitValues.remove(var);
                toRemove.add(var);
            }
        }

        for (var func : module.getFunctions()) {
            for (var block : func.blocks) {
                for (var iter = block.instructions.iterator(); iter.hasNext(); ) {
                    var instr = iter.next();
                    switch (instr) {
                        case Instruction.Store store -> {
                            if (store.getAddress() instanceof GlobalVar globalVar && toRemove.contains(globalVar)) {
                                iter.remove();
                                instr.dispose();
                            }
                        }
                        case Instruction.Load load -> {
                            if (load.getAddress() instanceof GlobalVar globalVar && toRemove.contains(globalVar)) {
                                iter.remove();
                                instr.dispose();
                            }
                        }
                        default -> { }
                    }
                }
            }
        }
    }

    // 如果安全，将全局变量降低到局部变量，只降低标量，不然到时候数组又给 Globalize 提上来就搞笑了
    public static void localize(Module module) {
        outer: for (var iterator = module.getGlobalVars().iterator(); iterator.hasNext(); ) {
            var globalVar = iterator.next();
            var type = globalVar.varType;
            // 不降低数组变量
            if (!(type instanceof Type.Scalar)) continue;

            Function func = null;
            // 检查是否只有一个函数使用了该全局变量
            for (var use : globalVar.usedBy) {
                var instr = (Instruction) use.user;
                var funcOfInstr = instr.getBlock().getFunction();
                if (func == null) func = funcOfInstr;
                else if (func != funcOfInstr) continue outer;

                if (instr instanceof Instruction.Store) continue outer;
            }

            var entry = func.entry;
            var initValue = module.globalVarInitValues.get(globalVar);
            helper.changeBlock(entry);
            helper.moveToHead();
            var localVar = helper.insertAlloca(globalVar.varType);
            if (initValue instanceof ImmediateValue.ZeroInit) {
                if (type == Types.Int) helper.insertStore(localVar, ImmediateValues.iZero);
                else if (type == Types.Float) helper.insertStore(localVar, ImmediateValues.fZero);
                else unreachable();
            } else helper.insertStore(localVar, initValue);
            helper.changeBlockToNull();
            globalVar.replaceAllUsesWith(localVar);
            iterator.remove();
        }
    }

}

