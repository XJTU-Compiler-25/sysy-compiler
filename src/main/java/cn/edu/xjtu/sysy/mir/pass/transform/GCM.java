package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;

import java.util.*;

// Global Code Motion
// 不处理 load store
public final class GCM extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    private FuncInfo funcInfo;
    private LoopInfo loopInfo;
    private DomInfo domInfo;
    @Override
    public void visit(Module module) {
        funcInfo = getResult(FuncInfoAnalysis.class);
        loopInfo = getResult(LoopAnalysis.class);
        domInfo = getResult(DominanceAnalysis.class);

        for (var function : module.getFunctions()) visit(function);
    }

    private boolean isPinned(Instruction inst) {
        return switch (inst) {
            case Terminator _, Load _, Store _, Alloca _ -> true;
            // 纯函数不会读写函数参数和全局变量，因此可以浮动
            case Call it -> !funcInfo.isPure(it.getCallee());
            case CallExternal _ -> true;
            default -> false;
        };
    }

    private Function currentFunc;
    private final HashSet<Instruction> visited = new HashSet<>();
    @Override
    public void visit(Function function) {
        currentFunc = function;

        var toSchedule = new ArrayList<Instruction>();
        for (var block : domInfo.getDFN(function)) {
            for (var inst : block.instructions) {
                if (!isPinned(inst)) toSchedule.add(inst);
            }
        }

        visited.clear();
        for (var inst : toSchedule) {
            scheduleEarly(inst);
        }


        visited.clear();
        Collections.reverse(toSchedule);
        for (var inst : toSchedule) {
            scheduleLate(inst);
        }
    }

    private void scheduleEarly(Instruction inst) {
        if (visited.contains(inst) || isPinned(inst)) return;
        visited.add(inst);

        var targetBB = currentFunc.entry;
        for (var use : inst.used) {
            switch (use.value) {
                case Instruction opInst -> {
                    scheduleEarly(opInst);
                    var operandBB = opInst.getBlock();
                    if (domInfo.getDomDepth(operandBB) > domInfo.getDomDepth(targetBB)) targetBB = operandBB;
                }
                case BlockArgument arg -> {
                    var operandBB = arg.block;
                    if (domInfo.getDomDepth(operandBB) > domInfo.getDomDepth(targetBB)) targetBB = operandBB;
                }
                default -> {}
            }
        }

        var originBB = inst.getBlock();
        if (originBB != targetBB) {
            originBB.removeInstruction(inst);
            targetBB.insertAtLast(inst);
        }
    }

    private void scheduleLate(Instruction inst) {
        if (visited.contains(inst) || isPinned(inst)) return;
        visited.add(inst);

        var usedBy = inst.usedBy;
        // 没有用户，保持在 Early 位置即可，无需移动
        if (usedBy.isEmpty()) return;

        for (var use : usedBy) {
            if (use.user instanceof Instruction userInst) scheduleLate(userInst);
        }

        BasicBlock lca = null;
        for (var use : usedBy) {
            if (!(use.user instanceof Instruction user)) continue;

            var userBB = user.getBlock();
            lca = (lca == null) ? userBB : domInfo.domLCA(lca, userBB);
        }

        if (lca == null) return;

        var earlyBB = inst.getBlock();
        var bestBB = lca;
        var curBB = lca;

        while (curBB != null && domInfo.dominates(earlyBB, curBB)) {
            if (loopInfo.getLoopDepthOf(curBB) < loopInfo.getLoopDepthOf(bestBB)) bestBB = curBB;
            if (curBB == earlyBB) break;
            curBB = domInfo.getIDom(curBB);
        }

        int insertPlace = -1;
        var instructionsInBestBB = bestBB.instructions;
        for (int i = 0; i < instructionsInBestBB.size(); i++) {
            if (instructionsInBestBB.get(i).used(inst)) {
                insertPlace = i;
                break;
            }
        }

        var originBB = inst.getBlock();
        if (originBB == bestBB) {
            if (insertPlace != -1) {
                int currentPlace = originBB.indexOf(inst);
                if (currentPlace > insertPlace) {
                    originBB.removeInstruction(inst);
                    originBB.insertAt(insertPlace, inst);
                }
            } // = -1 的时候放在哪都无所谓，也不用专门插到 last 了
        } else {
            originBB.removeInstruction(inst);
            if (insertPlace != -1) bestBB.insertAt(insertPlace, inst);
            else bestBB.insertAtLast(inst);
        }
    }

}
