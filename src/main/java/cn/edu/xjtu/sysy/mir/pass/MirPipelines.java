package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.CallGraphAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FrequencyAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfoAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.LoopAnalysis;
import cn.edu.xjtu.sysy.mir.pass.transform.CFGSimplify;
import cn.edu.xjtu.sysy.mir.pass.transform.ConstFold;
import cn.edu.xjtu.sysy.mir.pass.transform.DCE;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterLIR;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.LIRInstCombine;
import cn.edu.xjtu.sysy.mir.pass.transform.SCCP;

public final class MirPipelines {

    private MirPipelines() { }

    public static final Pipeline<Module> DEFAULT = new Pipeline<>(
            //GlobalOpt::new,
            EnterSSA::new,
            SCCP::new,
            ConstFold::new,
            DCE::new,
            //GCM::new,
            DCE::new,
            EnterLIR::new,
            LIRInstCombine::new
    );

    public static final Pipeline<Module> UNOPTIMIZED = new Pipeline<>(
            EnterSSA::new
    );

}
