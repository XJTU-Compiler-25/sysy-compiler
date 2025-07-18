package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.PassGroup;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.OncePurenessAnalysis;
import cn.edu.xjtu.sysy.mir.pass.transform.DeadCodeElimination;
import cn.edu.xjtu.sysy.mir.pass.transform.EnterSSA;
import cn.edu.xjtu.sysy.mir.pass.transform.InstCombine;

public final class MirPassGroups {

    private MirPassGroups() { }

    public static PassGroup<Module> makePassGroup(ErrManager em) {
        return new PassGroup<>(em,
                DominanceAnalysis::new,
                EnterSSA::new,
                OncePurenessAnalysis::new,
                InstCombine::new,
                DeadCodeElimination::new
                // CommonSubexprElimination::new

        );
    }
}
