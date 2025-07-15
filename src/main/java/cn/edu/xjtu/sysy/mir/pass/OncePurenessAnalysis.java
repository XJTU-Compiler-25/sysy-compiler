package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Var;

// 分析函数的纯性及是否最多被调用一次
public class OncePurenessAnalysis extends ModuleVisitor {
    public OncePurenessAnalysis(ErrManager errManager) {
        super(errManager);
    }

    private Function currentFunc;

    @Override
    public void visit(Module module) {
        var functions = module.functions.values();
        for (var function : functions) {
            function.isPure = true;
        }

        for (var function : functions) {
            visit(function);
        }
    }

    @Override
    public void visit(Function function) {
        currentFunc = function;

        super.visit(function);
    }

    @Override
    public void visit(Instruction instruction) {
        switch (instruction) {
            case Call it -> {
                if (!it.function.isPure) currentFunc.isPure = false;
            }
            case CallExternal it -> {
                currentFunc.isPure = false;
            }
            case Store it -> {
                // 修改全局变量或者修改外部传入的指针，都具有副作用
                if (it.address.value instanceof Var var && (var.isGlobal || var.isParam))
                    currentFunc.isPure = false;
            }
            default -> { }
        }
    }
}
