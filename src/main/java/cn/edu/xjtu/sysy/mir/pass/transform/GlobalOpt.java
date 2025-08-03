package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.symbol.Type;

import java.util.*;

public final class GlobalOpt extends AbstractTransform {
    public GlobalOpt(Pipeline<Module> pipeline) { super(pipeline); }

    private static final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(Module module) {
        removeUncalledFunction(module);
        removeUnusedGlobalVars(module);
        localize(module);
    }

    public void removeUncalledFunction(Module module) {
        var callgraph = getCallGraph();

        for (var iterator = module.getFunctions().iterator(); iterator.hasNext(); ) {
            var func = iterator.next();
            if (func.name.equals("main")) continue; // main 函数不能被删掉

            if (callgraph.getFunctionsCalled(func).isEmpty()) {
                iterator.remove();
                func.dispose();
            }
        }
    }

    // aggressively，因为我们编译的东西都不会被外部调用
    // 1. 完全没被 use 的全局变量
    // 2. 从来没被 load 过的全局变量
    public static void removeUnusedGlobalVars(Module module) {
        for (var iterator = module.getGlobalVars().iterator(); iterator.hasNext(); ) {
            var var = iterator.next();

            if (var.notUsed()) {
                iterator.remove();
                module.globalVarInitValues.remove(var);
            } else if (var.usedBy.stream().noneMatch(it -> it.user instanceof Instruction.Load)) {
                iterator.remove();
                module.globalVarInitValues.remove(var);
            }
        }
    }

    // 如果安全，将全局变量降低到局部变量，只降低标量，不然到时候数组又给 Globalize 提上来就搞笑了
    public static void localize(Module module) {
        var candidates = new HashSet<GlobalVar>();

        outer: for (var iterator = module.getGlobalVars().iterator(); iterator.hasNext(); ) {
            var globalVar = iterator.next();
            // 不降低数组变量
            if (!(globalVar.varType instanceof Type.Scalar)) continue;

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
            var localVar = helper.alloca(globalVar.varType);
            if (!(initValue instanceof ImmediateValue.ZeroInit)) helper.store(localVar, initValue);
            helper.changeBlockToNull();
            globalVar.replaceAllUsesWith(localVar);
            iterator.remove();
        }
    }

}

