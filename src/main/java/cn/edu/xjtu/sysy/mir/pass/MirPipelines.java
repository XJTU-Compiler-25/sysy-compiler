package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.mir.pass.transform.*;

public final class MirPipelines {

    private MirPipelines() { }

    public static final Pipeline<Module> DEFAULT = new Pipeline<>(
            CallGraphAnalysis::new,
            OnceAnalysis::new,
            VarLocalize::new,
            CFGAnalysis::new,
            DominanceAnalysis::new,
            PurenessAnalysis::new,
            EnterSSA::new,
            InstCombine::new,
            SCCP::new,
            CFGAnalysis::new,
            DCE::new,
            CFGSimplify::new,
            CFGAnalysis::new
            // CommonSubexprElimination::new

    );

    public static final Pipeline<Module> UNOPTIMIZED = new Pipeline<>(
            CFGAnalysis::new,
            DominanceAnalysis::new,
            EnterSSA::new
    );

}
