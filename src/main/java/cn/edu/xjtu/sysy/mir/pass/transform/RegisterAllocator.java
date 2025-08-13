package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.MathUtils;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.*;

import static cn.edu.xjtu.sysy.riscv.ValueUtils.*;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

@SuppressWarnings("unchecked")
public final class RegisterAllocator extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    private CFG cfg;
    private DomInfo domInfo;
    private LoopInfo loopInfo;
    private LiveRangeInfo liveRangeInfo;

    @Override
    public void visit(Module module) {
        cfg = getResult(CFGAnalysis.class);
        domInfo = getResult(DominanceAnalysis.class);
        loopInfo = getResult(LoopAnalysis.class);
        liveRangeInfo = getResult(LiveRangeAnalysis.class);

        for (var f : module.getFunctions()) allocate(f);
    }

    private Function currentFunction;
    public void allocate(Function function) {
        currentFunction = function;

        spill();
        color();
        coalesce();
        elimination();
    }

    private void spill() {
        spillCostCache.clear();

        var needToSpill = new HashSet<Instruction>();
        for (var inst : currentFunction.getAllInstructions()) {
            var liveOutSet = liveRangeInfo.getLiveOut(inst);
            var liveOutInts = new ArrayList<Instruction>();
            var liveOutFloats = new ArrayList<Instruction>();
            for (var value : liveOutSet) {
                if (!(value instanceof Instruction instr)) continue;
                if (!needToSpill.contains(instr)) continue;
                var type = instr.type;
                if (type == Types.Int) liveOutInts.add(instr);
                else if (type == Types.Float) liveOutFloats.add(instr);
                else unreachable();
            }

            var intSpillCount = liveOutInts.size() - usableIntRegCount;
            if (intSpillCount > 0) liveOutInts.stream()
                    .sorted(Comparator.comparingInt(this::spillCost)).limit(intSpillCount).forEach(needToSpill::add);

            var floatSpillCount = liveOutFloats.size() - usableFloatRegCount;
            if (floatSpillCount > 0) liveOutFloats.stream()
                    .sorted(Comparator.comparingInt(this::spillCost)).limit(floatSpillCount).forEach(needToSpill::add);
        }

        var entry = currentFunction.entry;
        var stackState = currentFunction.stackState;
        for (var instr : needToSpill) {
            helper.changeBlock(entry);
            helper.moveToHead();
            var type = instr.type;
            var alloca = helper.insertAlloca(type);
            alloca.position = new StackPosition(stackState.allocate(type));

            var defBlock = instr.getBlock();
            helper.changeBlock(defBlock);
            helper.moveToAfter(instr);
            helper.insertStore(alloca, instr);
        }
        helper.changeBlockToNull();

        // reload 延迟到 codegen
    }

    private final HashMap<Instruction, Integer> spillCostCache = new HashMap<>();
    private int spillCost(Instruction value) {
        var cached = spillCostCache.get(value);
        if (cached != null) return cached;

        var defBlock = value.getBlock();
        var defLoopDepth = loopInfo.getLoopDepthOf(defBlock);
        var storeCost = (int) Math.pow(10, defLoopDepth);

        var totalLoadCost = 0;
        for (var use : value.usedBy) {
            if (!(use.user instanceof Instruction useInst)) continue;
            var useBlock = useInst.getBlock();
            var useLoopDepth = loopInfo.getLoopDepthOf(useBlock);
            totalLoadCost = MathUtils.saturatedAdd(totalLoadCost, (int) Math.pow(10, useLoopDepth));
        }

        var spillCost = storeCost + totalLoadCost;
        spillCostCache.put(value, spillCost);
        return spillCost;
    }

    private void color() {
        saveCostCache.clear();

        var inUse = new HashSet<Register>();
        // 先序遍历支配树，就能得到冲突图的完美消去序列的倒序，即着色顺序
        for (var block : domInfo.getDFN(currentFunction)) {
            var bbLiveIn = liveRangeInfo.getLiveIn(block);
            for (var value : bbLiveIn) {
                if (!(value instanceof Instruction inst)) continue;
                var pos = inst.position;
                if (pos instanceof Register reg) inUse.add(reg);
            }

            for (var inst : block.instructions) {
                var liveOut = liveRangeInfo.getLiveOut(inst);
                for (var use : inst.used) {
                    if (!(use.value instanceof Instruction useInst)) continue;
                    if (!liveOut.contains(useInst)) {
                        var pos = inst.position;
                        if (pos instanceof Register reg) inUse.remove(reg);
                    }
                }

                // 有预着色
                if (inst.position != null) {
                    if (inst.position instanceof Register reg) inUse.add(reg);
                    continue;
                }

                // void 类型的指令不产生值，不需要寄存器
                if (inst.type == Types.Void) continue;

                // 需要分配寄存器
                var isInt = inst.type == Types.Int;
                Register reg = null;
                for (var candidate : isInt ? callerSavedUsableIntRegs : callerSavedUsableFloatRegs) {
                    if (!inUse.contains(candidate)) {
                        reg = candidate;
                        break;
                    }
                }

                if (reg == null) {
                    for (var candidate : isInt ? calleeSavedUsableIntRegs : calleeSavedUsableFloatRegs) {
                        if (!inUse.contains(candidate)) {
                            reg = candidate;
                            break;
                        }
                    }
                }

                if (reg == null) unreachable();

                inst.position = reg;
                inUse.add(reg);
            }

            var bbLiveOut = liveRangeInfo.getLiveOut(block);
            for (var use : block.terminator.used) {
                if (!(use.value instanceof Instruction useInst)) continue;
                if (!bbLiveOut.contains(useInst)) {
                    var pos = useInst.position;
                    if (pos instanceof Register reg) inUse.remove(reg);
                }
            }
        }
    }

    // 用于选择 callee-saved or caller-saved
    private final HashMap<Instruction, Integer> saveCostCache = new HashMap<>();
    private int saveCost(Instruction value) {
        var cached = saveCostCache.get(value);
        if (cached != null) return cached;

        return 0;
    }

    private void coalesce() {

    }

    private void elimination() {

    }

}
