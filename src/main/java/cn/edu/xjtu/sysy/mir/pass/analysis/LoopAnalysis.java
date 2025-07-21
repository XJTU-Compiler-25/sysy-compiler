package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;

import java.util.ArrayList;
import java.util.List;

public final class LoopAnalysis extends ModuleVisitor {
    public LoopAnalysis(ErrManager errManager) {
        super(errManager);
    }

    public void visit(Function function) {

    }

}
