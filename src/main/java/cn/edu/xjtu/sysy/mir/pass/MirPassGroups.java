package cn.edu.xjtu.sysy.mir.pass;

import cn.edu.xjtu.sysy.PassGroup;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.Module;

public final class MirPassGroups {

    private MirPassGroups() { }

    public static PassGroup<Module> makePassGroup(ErrManager em) {
        return new PassGroup<>(em,
                DominanceAnalysis::new,
                EnterSSA::new,
                ConstFold::new,
                DeadCodeElimination::new
        );
    }
}
