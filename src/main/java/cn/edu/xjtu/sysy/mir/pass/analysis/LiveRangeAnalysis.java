package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.BlockArgument;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.GlobalVar;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.util.ModulePrinter;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;
import cn.edu.xjtu.sysy.util.Pair;
import cn.edu.xjtu.sysy.util.Worklist;

// 这是一个在 enter LIR 之后才会运行的分析
// 所以反向数据流分析直接从 epilogue 开始反向扫描
public final class LiveRangeAnalysis extends ModulePass<LiveRangeInfo> {

    private HashMap<Instruction, Set<Value>> liveIn;
    private HashMap<Instruction, Set<Value>> liveOut;

    private HashMap<BasicBlock, Set<Value>> liveInBlock;
    private HashMap<BasicBlock, Set<Value>> liveOutBlock;
    //private HashMap<Value, Set<Instruction>> instsValueLiveBefore;

    private CFG cfg;
    //private final HashMap<Instruction, Set<? extends Instruction>> preds = new HashMap<>();
    //private final HashMap<Instruction, Set<? extends Instruction>> succs = new HashMap<>();

    @Override
    public LiveRangeInfo process(Module module) {
        liveOut = new HashMap<>();
        liveIn = new HashMap<>();
        liveInBlock = new HashMap<>();
        liveOutBlock = new HashMap<>();
        //instsValueLiveBefore = new HashMap<>();

        cfg = getResult(CFGAnalysis.class);
        //preds.clear();
        //succs.clear();
        /* 
        for (var f : module.getFunctions()) {
            collectPredSucc(f);
            markFunction(f);
        }
        collectAliveAfter();*/

        module.functions.values().forEach(func -> visit(func));

        //ModulePrinter.printModule(module);
 
        /*module.functions.values().forEach(func -> {
            cfg.getRPOBlocks(func).forEach(block -> {
                block.instructions.forEach(inst -> {
                    System.out.println();
                    System.out.println(liveOut.get(inst).stream().map(v -> v.shortName()).collect(Collectors.toSet()));
                    System.out.println(inst);
                    System.out.println(liveIn.get(inst).stream().map(v -> v.shortName()).collect(Collectors.toSet()));
                    System.out.println();
                });
                var inst = block.terminator;
                System.out.println();
                System.out.println(liveOut.get(inst).stream().map(v -> v.shortName()).collect(Collectors.toSet()));
                System.out.println(inst);
                System.out.println(liveIn.get(inst).stream().map(v -> v.shortName()).collect(Collectors.toSet()));
                System.out.println();
            });
        });*/
        return new LiveRangeInfo(liveIn, liveOut);
    }

    // 初始情况，也就是格的下界
    private Set<Value> initial() {
        return new HashSet<>();
    }

    // 拷贝，将src的值拷贝到dst
    private void copy(Set<Value> dst, Set<Value> src) {
        dst.clear();
        dst.addAll(src);
    }

    // 合并，在控制流交汇的地方选择取交或者取并
    private void merge(Set<Value> dst, Set<Value> src1, Set<Value> src2) {
        dst.clear();
        dst.addAll(src1);
        dst.addAll(src2);
    }

    // 单条语句的转换函数
    private void flowThrough(Instruction instr, Set<Value> in, Set<Value> out) {
        out.clear();
        out.addAll(in);
        gen(instr).forEach(out::add);
        kill(instr).forEach(out::remove);
    }

    private Stream<Value> gen(Instruction instr) {
        return instr.used.stream().map(it -> it.value).filter(it ->
            it instanceof Instruction || it instanceof BlockArgument
        );
    }

    private Stream<Value> kill(Instruction instr) {
        if (instr.notProducingValue()) return Stream.empty();
        return Stream.of(instr);
    }

    protected boolean meet(BasicBlock block, Set<Value> in) {
        if (!liveInBlock.containsKey(block)) {
            liveInBlock.put(block, in);
            return true;
        }
        var inout = liveInBlock.get(block);
        Set<Value> tmp = initial();
        merge(tmp, inout, in);
        if (tmp.equals(inout)) {
            return false;
        }
        liveInBlock.put(block, tmp);
        return true;
    }

    protected void flowThrough(BasicBlock b) {
        var in = liveInBlock.get(b);
        flowThrough(b, in);
    }

    protected void flowThrough(BasicBlock block, Set<Value> in) {
        var instrs = new ArrayList<>(block.instructions.reversed());
        instrs.addFirst(block.terminator);
        var inFlow = in;
        var outFlow = initial();
        // 按顺序遍历每条指令，进行分析
        for (var instr : instrs) {
            liveIn.put(instr, inFlow);
            flowThrough(instr, inFlow, outFlow);
            liveOut.put(instr, outFlow);

            inFlow = outFlow;
            outFlow = initial();
        }
        copy(outFlow, inFlow);
        block.args.forEach(outFlow::remove);
        liveOutBlock.put(block, outFlow);
    }

    @Override
    public void visit(Function function) {
        var worklist =
                new Worklist<>(Collections.singleton(Pair.pair(function.epilogue, initial())));
        HashSet<BasicBlock> visited = new HashSet<>();
        while (!worklist.isEmpty()) {
            var e = worklist.poll();
            var cur = e.first();
            var in = e.second();
            visited.add(cur);
            boolean changed = meet(cur, in);
            if (changed) {
                flowThrough(cur);
                for (var succ : cfg.getPredBlocksOf(cur))
                    worklist.add(Pair.pair(succ, liveOutBlock.get(cur)));
            }
        }
        for (var block : function.blocks) {
            if (!visited.contains(block)) System.out.println(block);
        }
        Assertions.requires(visited.size() == function.blocks.size());
    }

    /* 
    private void collectPredSucc(Function function) {
        var blocks = cfg.getRPOBlocks(function);

        for (var block : blocks) {
            var predTerms = cfg.getPredTermsOf(block);
            Instruction lastInst = null;
            for (var inst : block.instructions) {
                if (lastInst == null) preds.put(inst, predTerms);
                else preds.put(inst, Set.of(lastInst));
                lastInst = inst;
            }
        }

        for (var block : blocks) {
            var succFirsts = cfg.getSuccBlocksOf(block).stream().map(BasicBlock::getFirstInstruction).collect(Collectors.toSet());
            Instruction lastInst = null;
            for (var inst : block.instructions) {
                if (lastInst != null) succs.put(lastInst, Set.of(inst));
                lastInst = inst;
            }
            succs.put(block.terminator, succFirsts);
        }
    }

    private void markFunction(Function function) {
        for (var inst : function.getAllInstructions()) {
            liveIn.put(inst, new HashSet<>());
            liveOut.put(inst, new HashSet<>());
        }

        var epilogue = function.epilogue;
        liveOut.put(epilogue.terminator, Set.of());

        var visited = new HashSet<BasicBlock>();
        var worklist = new Worklist<BasicBlock>();
        worklist.add(epilogue);
        while (!worklist.isEmpty()) {
            var block = worklist.poll();
            for (var arg : block.args) markAliveBefore(arg, null);
            for (var inst : block.getInstructionsAndTerminator()) markAliveBefore(inst, inst);
            for (var succ : cfg.getPredBlocksOf(block)) {
                if (visited.add(succ)) worklist.add(succ);
            }
        }
    }

    private void markAliveBefore(Value value, Instruction stop) {
        for (var use : value.usedBy) {
            if (use.user instanceof Instruction inst) markAliveBefore(inst, value, stop);
        }
    }

    private void markAliveBefore(Instruction inst, Value value, Instruction stop) {
        if (inst == stop) return;

        if (!liveIn.get(inst).add(value)) return;

        instsValueLiveBefore.computeIfAbsent(value, _ -> new HashSet<>()).add(inst);
        for (var pred : preds.getOrDefault(inst, Set.of()))
            markAliveBefore(pred, value, stop);
    }

    private void collectAliveAfter() {
        succs.forEach((inst, succs) -> {
            var after = new HashSet<Value>();
            liveOut.put(inst, after);
            for (var succ : succs) after.addAll(liveIn.get(succ));
        });
    }
        */
}
