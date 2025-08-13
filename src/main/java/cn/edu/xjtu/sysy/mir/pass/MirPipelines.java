package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.mir.pass.transform.ConstFold;
import cn.edu.xjtu.sysy.mir.pass.transform.DCE;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterLIR;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.LIRInstCombine;
import cn.edu.xjtu.sysy.mir.pass.transform.SCCP;
import cn.edu.xjtu.sysy.mir.pass.transform.RegisterAllocator;

public final class MirPipelines {

    private MirPipelines() { }

    public static final Pipeline<Module> DEFAULT = Pipeline.builder(Module.class)
            .addAnalyses(
                    AliasAnalysis::new,
                    CFGAnalysis::new,
                    CallGraphAnalysis::new,
                    DominanceAnalysis::new,
                    FrequencyAnalysis::new,
                    FuncInfoAnalysis::new,
                    LoopAnalysis::new,
                    LiveRangeAnalysis::new,
                    SCEV::new
            )
            .addTransformers(
                    //GlobalOpt::new,
                    EnterSSA::new,
                    SCCP::new,
                    ConstFold::new,
                    DCE::new,
                    //GCM::new,
                    DCE::new,
                    EnterLIR::new,
                    LIRInstCombine::new,
                    RegisterAllocator::new,
                    RiscVCGen::new
            )
            .build();

    public static final Pipeline<Module> UNOPTIMIZED = Pipeline.builder(Module.class)
            .addAnalyses(
                    AliasAnalysis::new,
                    CFGAnalysis::new,
                    CallGraphAnalysis::new,
                    DominanceAnalysis::new,
                    FrequencyAnalysis::new,
                    FuncInfoAnalysis::new,
                    LoopAnalysis::new,
                    LiveRangeAnalysis::new,
                    SCEV::new
            )
            .addTransformers(
                    EnterSSA::new
            )
            .build();

}
