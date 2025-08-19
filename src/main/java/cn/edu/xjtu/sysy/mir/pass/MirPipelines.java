package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.mir.pass.transform.*;
import cn.edu.xjtu.sysy.mir.pass.transform.loop.*;

public final class MirPipelines {

    private MirPipelines() { }

    public static final Pipeline<Module> DEFAULT = Pipeline.builder(Module.class)
            .addAnalyses(
                    AliasAnalysis::new,
                    CFGAnalysis::new,
                    CallGraphAnalysis::new,
                    DominanceAnalysis::new,
                    FuncInfoAnalysis::new,
                    LoopAnalysis::new,
                    LiveRangeAnalysis::new,
                    SCEV::new
            )
            .addTransformers(
                    GlobalOpt::new,
                    HoistAlloca::new,
                    EnterSSA::new,
                    //Inline::new,
                    SCCP::new,
                    ConstFold::new,
                    InstCombine::new,
                    DCE::new,
                    ParamOpt::new,
                    GVN::new,
                    GCM::new,
                    DCE::new,
                    LoopCanonicalize::new,
                    SCCP::new,
                    DCE::new,
                    CFGSimplify::new,
                    EnterLIR::new,
                    //LIRInstCombine::new,
                    RegisterAllocator::new,
                    ExitSSA::new,
                    HoistAlloca::new
            )
            .build();

    public static final Pipeline<Module> UNOPTIMIZED = Pipeline.builder(Module.class)
            .addAnalyses(
                    AliasAnalysis::new,
                    CFGAnalysis::new,
                    CallGraphAnalysis::new,
                    DominanceAnalysis::new,
                    FuncInfoAnalysis::new,
                    LoopAnalysis::new,
                    LiveRangeAnalysis::new,
                    SCEV::new
            )
            .addTransformers(
            )
            .build();

}
