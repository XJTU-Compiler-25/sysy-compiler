package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Var;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

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

        for (var block : function.blocks) {
            for (var instruction : block.instructions) {
                switch (instruction) {
                    case Call it -> {
                        if (!it.function.isPure) currentFunc.isPure = false;
                    }
                    case CallExternal it -> {
                        currentFunc.isPure = false;
                    }
                    case GetElemPtr it -> {
                        // GetElemPtr 指令本身没有副作用，但如果加载的是全局变量或者函数参数，则函数不纯
                        var addr = it.basePtr.value;
                        if (addr instanceof Var var && (var.isGlobal || var.isParam)) {
                            currentFunc.isPure = false;
                            return;
                        }
                    }
                    case Load it -> {
                        // Load 指令本身没有副作用，但如果加载的是全局变量或者函数参数，则函数不纯
                        var addr = it.address.value;
                        if (addr instanceof Var var && (var.isGlobal || var.isParam)) {
                            currentFunc.isPure = false;
                            return;
                        }
                    }
                    case Store it -> {
                        // 修改全局变量或者修改外部传入的指针，都具有副作用
                        var addr = it.address.value;
                        if (addr instanceof Var var && (var.isGlobal || var.isParam)) {
                            currentFunc.isPure = false;
                            return;
                        }

                    }
                    default -> { }
                }
            }
        }
    }
}
