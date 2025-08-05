package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.OnceAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.PurenessAnalysis;
import cn.edu.xjtu.sysy.mir.pass.transform.CFGSimplify;
import cn.edu.xjtu.sysy.mir.pass.transform.DCE;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterLIR;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.ExitSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.InstCombine;
import cn.edu.xjtu.sysy.mir.pass.transform.LIRInstCombine;
import cn.edu.xjtu.sysy.mir.pass.transform.SCCP;

public final class MirPipelines {

    private MirPipelines() { }

    public static final Pipeline<Module> DEFAULT = new Pipeline<>(
            CFGAnalysis::new,
            DominanceAnalysis::new,
            CallGraphAnalysis::new,
            OnceAnalysis::new,
            PurenessAnalysis::new,
            EnterSSA::new,
            InstCombine::new,
            SCCP::new,
            CFGAnalysis::new,
            DCE::new,
            CFGSimplify::new,
            CFGAnalysis::new,
            // CommonSubexprElimination::new
            EnterLIR::new,
            LIRInstCombine::new,
            ExitSSA::new
    );

    public static final Pipeline<Module> UNOPTIMIZED = new Pipeline<>(
            CFGAnalysis::new,
            DominanceAnalysis::new,
            EnterSSA::new
    );

}
