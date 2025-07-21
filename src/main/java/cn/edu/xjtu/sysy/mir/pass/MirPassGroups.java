package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.PassGroup;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.mir.pass.transform.*;

public final class MirPassGroups {

    private MirPassGroups() { }

    public static PassGroup<Module> makePassGroup(ErrManager em) {
        return new PassGroup<>(em,
                CFGAnalysis::new,
                CallGraphAnalysis::new,
                OnceAnalysis::new,
                PurenessAnalysis::new,
                DominanceAnalysis::new,
                EnterSSA::new,
                InstCombine::new,
                DCE::new,
                SCCP::new,
                CFGSimplify::new
                // CommonSubexprElimination::new

        );
    }
}
