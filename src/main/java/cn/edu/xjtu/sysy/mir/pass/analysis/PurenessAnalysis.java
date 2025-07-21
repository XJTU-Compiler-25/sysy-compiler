package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Var;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

// 分析函数的纯性
public class PurenessAnalysis extends ModuleVisitor {
    public PurenessAnalysis() {}
    public PurenessAnalysis(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Module module) {
        var functions = module.getFunctions();
        // 需要遍历 caller 的函数
        var worklist = new Worklist<Function>();

        // 先默认所有函数是纯的
        for (var function : functions) function.isPure = true;

        // 第一趟遍历，仅通过函数本身的指令是否有副作用来判断纯性
        outer: for (var function : functions) {
            for (var block : function.getTopoSortedBlocks()) {
                for (var instruction : block.instructions) {
                    switch (instruction) {
                        // 外部函数都有副作用
                        case CallExternal it -> {
                            function.isPure = false;
                            worklist.add(function);
                            continue outer;
                        }
                        case GetElemPtr it -> {
                            // GetElemPtr 指令本身没有副作用，但如果取的是全局变量或者函数参数的地址，则函数不纯
                            var addr = it.basePtr.value;
                            if (addr instanceof Var var && (var.isGlobal || var.isParam)) {
                                function.isPure = false;
                                worklist.add(function);
                                continue outer;
                            }
                        }
                        case Load it -> {
                            // Load 指令本身没有副作用，但如果加载的是全局变量或者函数参数，则函数不纯
                            var addr = it.address.value;
                            if (addr instanceof Var var && (var.isGlobal || var.isParam)) {
                                function.isPure = false;
                                worklist.add(function);
                                continue outer;
                            }
                        }
                        case Store it -> {
                            // 修改全局变量或者修改外部传入的指针，都具有副作用
                            var addr = it.address.value;
                            if (addr instanceof Var var && (var.isGlobal || var.isParam)) {
                                function.isPure = false;
                                worklist.add(function);
                                continue outer;
                            }

                        }
                        default -> { }
                    }
                }
            }
        }

        // 第二趟遍历，根据调用图，让不纯函数感染调用者为不纯函数
        while (!worklist.isEmpty()) {
            var function = worklist.poll();
            // 遍历函数的调用者
            for (var caller : function.callers) {
                if (caller.isPure) {
                    caller.isPure = false;
                    worklist.add(caller);
                } // 如果调用者已经被标记为不纯，则不需要处理并遍历其调用者，因为肯定已经做过了
            }
        }
    }

}
