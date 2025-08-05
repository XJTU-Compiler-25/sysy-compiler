package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.mir.pass.transform.*;

public final class MirPipelines {

    private MirPipelines() { }

    public static final Pipeline<Module> DEFAULT = new Pipeline<>(
            CallGraphAnalysis::new,
            //GlobalOpt::new,
            CFGAnalysis::new,
            FuncInfoAnalysis::new,
            DominanceAnalysis::new,
            EnterSSA::new,
            SCCP::new,
            ConstFold::new,
            DCE::new,
            CFGAnalysis::new,
            LoopAnalysis::new,
            FrequencyAnalysis::new,
            //GCM::new,
            CFGSimplify::new,
            DCE::new,
            CFGAnalysis::new
    );

    public static final Pipeline<Module> UNOPTIMIZED = new Pipeline<>(
            CFGAnalysis::new,
            DominanceAnalysis::new,
            EnterSSA::new
    );

}
