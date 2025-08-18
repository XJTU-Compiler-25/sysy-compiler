package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.AliasAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfoAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.LiveRangeAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.LoopAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.SCEV;
import cn.edu.xjtu.sysy.mir.pass.transform.CFGSimplify;
import cn.edu.xjtu.sysy.mir.pass.transform.ConstFold;
import cn.edu.xjtu.sysy.mir.pass.transform.DCE;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterLIR;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.ExitSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.InstCombine;
import cn.edu.xjtu.sysy.mir.pass.transform.RegisterAllocator;
import cn.edu.xjtu.sysy.mir.pass.transform.SCCP;

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
                    //GlobalOpt::new,
                    EnterSSA::new,
                    SCCP::new,
                    ConstFold::new,
                    InstCombine::new,
                    DCE::new,
                    //GVN::new,
                    //GCM::new,
                    DCE::new,
                    CFGSimplify::new,
                    EnterLIR::new,
                    //LIRInstCombine::new,
                    RegisterAllocator::new,
                    ExitSSA::new
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
