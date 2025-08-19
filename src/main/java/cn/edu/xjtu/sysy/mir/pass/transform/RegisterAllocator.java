package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;
import cn.edu.xjtu.sysy.mir.util.ModulePrinter;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

import cn.edu.xjtu.sysy.riscv.Instr;
import static cn.edu.xjtu.sysy.riscv.ValueUtils.*;
import cn.edu.xjtu.sysy.util.Assertions;
import static cn.edu.xjtu.sysy.util.Assertions.fail;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

@SuppressWarnings("unchecked")
public final class RegisterAllocator extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();
    private DomInfo domInfo;
    private LoopInfo loopInfo;
    private LiveRangeInfo liveRangeInfo;

    @Override
    public void visit(Module module) {
        domInfo = getResult(DominanceAnalysis.class);
        loopInfo = getResult(LoopAnalysis.class);
        liveRangeInfo = getResult(LiveRangeAnalysis.class);
        //ModulePrinter.printModule(module);
        for (var f : module.getFunctions()) allocate(f);
    }

    private Function currentFunction;
    private final HashMap<BasicBlock, ArrayList<Instruction>> originalInstructions = new HashMap<>();
    public void allocate(Function function) {
        currentFunction = function;
        originalInstructions.clear();
        for (var block : domInfo.getDFN(currentFunction))
            originalInstructions.put(block, block.getInstructionsAndTerminator());
        
        spill();
        liveRangeInfo = getRefreshedResult(LiveRangeAnalysis.class);
        color();
        coalesce();

    }

    private void spill() {
        spillCostCache.clear();

        var entry = currentFunction.entry;
        var needToSpill = new HashSet<Value>();
        for (var block : domInfo.getDFN(currentFunction)) {
            for (var inst : originalInstructions.get(block)) {

                var liveOutSet = new ArrayList<>(liveRangeInfo.getLiveOut(inst));
                liveOutSet.sort(Comparator.comparingInt(it -> it.id));

                var liveOutInts = new ArrayList<Value>();
                var liveOutFloats = new ArrayList<Value>();
                for (var value : liveOutSet) {
                    if (needToSpill.contains(value)) continue;
                    var type = value.type;
                    if (type == Types.Float) liveOutFloats.add(value);
                    else liveOutInts.add(value);
                }

                var intSpillCount = liveOutInts.size() - usableIntRegCount;
                if (intSpillCount > 0) liveOutInts.stream()
                        //.filter(it -> !(it instanceof Instruction.Dummy))
                        .sorted(Comparator.comparingInt(this::spillCost)).limit(intSpillCount).forEach(needToSpill::add);
                
                var floatSpillCount = liveOutFloats.size() - usableFloatRegCount;
                if (floatSpillCount > 0) liveOutFloats.stream()
                        //.filter(it -> !(it instanceof Instruction.Dummy))
                        .sorted(Comparator.comparingInt(this::spillCost)).limit(floatSpillCount).forEach(needToSpill::add);
            }
        }

        var stackState = currentFunction.stackState;
        for (var value : needToSpill) {
            var type = value.type;
            var pos = new StackPosition(stackState.allocate(type));
            value.position = pos;

            switch (value) {
                case Instruction instr -> {
                    var defBlock = instr.getBlock();
                    helper.changeBlock(defBlock);
                    helper.moveToAfter(instr);
                    var alloca = helper.insertAlloca(type);
                    alloca.position = pos;
                }
                case BlockArgument arg -> {
                    var defBlock = arg.block;
                    helper.changeBlock(defBlock);
                    helper.moveToHead();
                    var alloca = helper.insertAlloca(type);
                    alloca.position = pos;
                }
                default -> unreachable();
            }
        }
        helper.changeBlockToNull();

        // reload 延迟到 codegen
    }

    private final HashMap<Value, Integer> spillCostCache = new HashMap<>();
    private int spillCost(Value value) {
        var cached = spillCostCache.get(value);
        if (cached != null) return cached;

        BasicBlock defBlock = switch (value) {
            case Instruction instr -> instr.getBlock();
            case BlockArgument arg -> arg.block;
            default ->  unreachable();
        };
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

    private final HashMap<Register, Value> inUse = new HashMap<>();
    private void color() {
        inUse.clear();
        saveCostCache.clear();

        // 先序遍历支配树，就能得到冲突图的完美消去序列的倒序，即着色顺序
        for (var block : domInfo.getDFN(currentFunction)) {
            var blockLiveIn = liveRangeInfo.getLiveOut(block.getFirstInstruction());
            for (var v : new ArrayList<>(inUse.values())) {
                if (!blockLiveIn.contains(v)) {
                    var pos = v.position;
                    if (pos instanceof Register reg) inUse.remove(reg);
                }
            }
            for (var arg : block.args) {
                if (arg.position != null) {
                    if (arg.position instanceof Register reg) inUse.put(reg, arg);
                    continue;
                }
                if (arg.notUsed()) continue;
                var reg = allocateRegister(arg);

                arg.position = reg;
                inUse.put(reg, arg);
            }

            for (var inst : originalInstructions.get(block)) {
                var liveIn = liveRangeInfo.getLiveIn(inst);
                var liveOut = liveRangeInfo.getLiveOut(inst);
                for (var v : new ArrayList<>(inUse.values())) {
                    if (!liveIn.contains(v)) {
                        var pos = v.position;
                        if (pos instanceof Register reg) inUse.remove(reg);
                    }
                }

                // 有预着色
                if (inst.position != null) {
                    if (inst.position instanceof Register reg) inUse.put(reg, inst);
                    continue;
                }

                // void 类型的指令不产生值，不需要寄存器
                if (inst.notProducingValue()) continue;
                if (inst.notUsed()) continue;
                // 需要分配寄存器
                var reg = allocateRegister(inst);

                inst.position = reg;
                inUse.put(reg, inst);
            }
        }
    }

    private Register allocateRegister(Value value) {
        var isInt = value.type != Types.Float;

        for (var candidate : isInt ? callerSavedUsableIntRegs : callerSavedUsableFloatRegs) {
            if (!inUse.containsKey(candidate)) return candidate;
        }

        for (var candidate : isInt ? calleeSavedUsableIntRegs : calleeSavedUsableFloatRegs) {
            if (!inUse.containsKey(candidate)) return candidate;
        }

        return fail("unable to allocate register for " + value + " in function " + currentFunction.name);
    }

    // todo
    // 用于选择 callee-saved or caller-saved
    private final HashMap<Instruction, Integer> saveCostCache = new HashMap<>();
    private int saveCost(Instruction value) {
        var cached = saveCostCache.get(value);
        if (cached != null) return cached;

        return 0;
    }

    private void coalesce() {
        // todo
        // 太难了，先等会吧（（
    }

}
