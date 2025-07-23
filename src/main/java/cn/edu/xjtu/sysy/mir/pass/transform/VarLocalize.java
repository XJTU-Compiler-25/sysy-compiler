package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.symbol.Type;

import java.util.*;

// 如果安全，将全局变量降低到局部变量
public final class VarLocalize extends AbstractTransform {
    public VarLocalize(Pipeline<Module> pipeline) { super(pipeline); }

    private static final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(Module module) {
        var candidates = new HashSet<Var>();

        outer:
        for (var iterator = module.getGlobalVars().iterator(); iterator.hasNext(); ) {
            var globalVar = iterator.next();
            // 不降低数组变量
            if (!(globalVar.varType instanceof Type.Scalar)) continue;

            // 删除未使用的全局变量
            if (globalVar.notUsed()) {
                iterator.remove();
                continue;
            }

            Function func = null;
            // 检查是否只有一个函数使用了该全局变量
            for (var use : globalVar.usedBy) {
                var instr = (Instruction) use.user;
                var funcOfInstr = instr.getBlock().getFunction();
                if (func == null) func = funcOfInstr;
                else if (func != funcOfInstr) continue outer;
            }

            var entry = func.entry;
            var localVar = func.addNewLocalVar(globalVar.name, globalVar.varType);
            var initValue = module.globalVarInitValues.get(globalVar);
            helper.changeBlock(entry);
            helper.moveToHead();
            if (!(initValue instanceof ImmediateValue.ZeroInit)) helper.store(localVar, initValue);
            helper.removeBlock();
            globalVar.replaceAllUsesWith(localVar);
            iterator.remove();
        }
    }

}

