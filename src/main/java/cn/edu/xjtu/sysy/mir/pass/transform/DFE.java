package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;

// dead function elimination
public final class DFE extends ModulePass<Void> {

    @Override
    public void visit(Module module) {
        var callgraph = getResult(CallGraphAnalysis.class);

        for (var iterator = module.getFunctions().iterator(); iterator.hasNext(); ) {
            var func = iterator.next();
            if (func.name.equals("main")) continue; // main 函数不能被删掉

            if (callgraph.getFunctionsCalled(func).isEmpty()) {
                iterator.remove();
                func.dispose();
            }
        }
    }

}
