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
        for (var f : module.getFunctions()) allocate(f);
        ModulePrinter.printModule(module);
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

        for (var value : needToSpill) {
            spill(value);
        }
        helper.changeBlockToNull();

        // reload 延迟到 codegen
    }

    private void spill(Value value) {
        var stackState = currentFunction.stackState;
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

    //private final Map<BasicBlock, Map<Register, Value>> inUse = new HashMap<>();
    private void color() {
        //inUse.clear();
        saveCostCache.clear();
        //currentFunction.blocks.forEach(block -> inUse.put(block, new HashMap<>()));
        // 先序遍历支配树，就能得到冲突图的完美消去序列的倒序，即着色顺序
        for (var block : domInfo.getDFN(currentFunction)) {
            /*System.out.println(block.shortName());
            var blockLiveIn = liveRangeInfo.getLiveOut(block.getFirstInstruction());
            for (var v : new ArrayList<>(inUse.get(block).values())) {
                if (!blockLiveIn.contains(v)) {
                    if (v.id == 51) {
                        System.out.println(11);
                        System.out.println(blockLiveIn);
                    }
                    var pos = v.position;
                    if (pos instanceof Register reg) inUse.get(block).remove(reg);
                }
            }*/
            for (var arg : block.args) {
                if (arg.position != null) {
                    //if (arg.position instanceof Register reg) inUse.get(block).put(reg, arg);
                    if (arg.position instanceof Register reg) {
                        var map = getInUseExceptSelf(arg);
                        var val = map.get(reg);
                        if (val != null) {
                            spill(arg);
                            arg.type = val.type;
                        }
                    }
                    continue;
                }
                if (arg.notUsed()) continue;
                var reg = allocateRegister(arg);

                arg.position = reg;
                //inUse.get(block).put(reg, arg);
            }

            for (var inst : originalInstructions.get(block)) {
                var liveIn = liveRangeInfo.getLiveIn(inst);
                var liveOut = liveRangeInfo.getLiveOut(inst);
                /*for (var v : new ArrayList<>(inUse.get(block).values())) {
                    if (!liveIn.contains(v)) {
                        var pos = v.position;
                        if (pos instanceof Register reg) inUse.get(block).remove(reg);
                    }
                }*/

                // 有预着色
                if (inst.position != null) {
                    //if (inst.position instanceof Register reg) inUse.get(block).put(reg, inst);
                    if (inst.position instanceof Register reg) {
                        var map = getInUseExceptSelf(inst);
                        var val = map.get(reg);
                        if (val != null) {
                            spill(inst);
                            inst.type = val.type;
                        }
                    }
                    continue;
                }

                if (inst instanceof Instruction.Jmp jmp) {
                    //inUse.get(jmp.getTarget()).putAll(inUse.get(block));
                    continue;
                }

                if (inst instanceof Instruction.AbstractBr br) {
                    //inUse.get(br.getTrueTarget()).putAll(inUse.get(block));
                    //inUse.get(br.getFalseTarget()).putAll(inUse.get(block));
                    continue;
                }

                // void 类型的指令不产生值，不需要寄存器
                if (inst.notProducingValue()) continue;
                if (inst.notUsed()) continue;

                // 需要分配寄存器
                var reg = allocateRegister(inst);

                inst.position = reg;
               // inUse.get(block).put(reg, inst);
            }
        }
    }

    private Set<Register> getInUse(Value val) {
        return switch(val) {
            case Instruction it -> getInUse(it);
            case BlockArgument it -> getInUse(it);
            default -> unreachable();
        };
    }

    private Set<Register> getInUse(Instruction inst) {
        return liveRangeInfo.getLiveIn(inst).stream().filter(in -> in.position instanceof Register)
            .map(in -> (Register) in.position).collect(Collectors.toSet());
    }

    private Set<Register> getInUse(BlockArgument arg) {
        return liveRangeInfo.getLiveOut(arg.block.getFirstInstruction()).stream()
            .filter(in -> in.position instanceof Register)
            .map(in -> (Register) in.position).collect(Collectors.toSet());
    }

    private Map<Register, Value> getInUseExceptSelf(Instruction inst) {
        return liveRangeInfo.getLiveIn(inst).stream().filter(in -> in != inst && in.position instanceof Register)
            .collect(Collectors.toMap((x -> (Register)x.position), (x -> x)));
    }

    private Map<Register, Value> getInUseExceptSelf(BlockArgument arg) {
        return liveRangeInfo.getLiveOut(arg.block.getFirstInstruction()).stream()
            .filter(in -> in != arg && in.position instanceof Register)
            .collect(Collectors.toMap((x -> (Register)x.position), (x -> x)));
    }

    private Register allocateRegister(Value value) {
        var isInt = value.type != Types.Float;
        var inUse = getInUse(value);
        for (var candidate : isInt ? callerSavedUsableIntRegs : callerSavedUsableFloatRegs) {
            if (!inUse.contains(candidate)) return candidate;
        }

        for (var candidate : isInt ? calleeSavedUsableIntRegs : calleeSavedUsableFloatRegs) {
            if (!inUse.contains(candidate)) return candidate;
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
