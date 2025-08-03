package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.DomInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.*;

@SuppressWarnings("unchecked")
public final class EnterSSA extends AbstractTransform {
    public EnterSSA(Pipeline<Module> pipeline) { super(pipeline); }

    private DomInfo domInfo;

    @Override
    public void visit(Module module) {
        domInfo = getResult(DominanceAnalysis.class);
        super.visit(module);
    }

    @Override
    public void visit(Function function) {
        hoistAllocas(function);
        findCandidate(function);
        insertBlockArgument();
        renaming(function);
    }

    // 将所有 alloca 提升到函数入口块
    private void hoistAllocas(Function function) {
        var allocas = new ArrayList<Instruction.Alloca>();
        for (var block : function.blocks) {
            for (var iter = block.instructions.iterator(); iter.hasNext(); ) {
                var instr = iter.next();
                if (instr instanceof Instruction.Alloca alloca) {
                    allocas.add(alloca);
                    iter.remove();
                }
            }
        }

        var entryInstrs = function.entry.instructions;
        for (var alloca : allocas) {
            entryInstrs.addFirst(alloca);
            alloca.setBlock(function.entry);
        }
    }

    private final ArrayList<Instruction.Alloca> promotableAllocas = new ArrayList<>();
    private void findCandidate(Function function) {
        promotableAllocas.clear();
        // 所有 alloca 都在函数 entry 块
        for (var iterator = function.entry.instructions.iterator(); iterator.hasNext(); ) {
            var instr = iterator.next();
            if (instr instanceof Instruction.Alloca alloca) {
                // 标量值的 alloca 可以提升
                if (alloca.allocatedType instanceof Type.Scalar
                        // 从未被取过地址
                        && alloca.usedBy.stream().noneMatch(it -> it.user instanceof Instruction.GetElemPtr)) {
                    promotableAllocas.add(alloca);
                    iterator.remove();
                }
            }
        }
    }

    private final HashMap<BlockArgument, Instruction.Alloca> blockArgToAlloca = new HashMap<>();
    private void insertBlockArgument() {
        blockArgToAlloca.clear();

        for (var alloca : promotableAllocas) {
            var defBlocks = new HashSet<BasicBlock>();
            for (var use : alloca.usedBy) if (use.user instanceof Instruction.Store store) defBlocks.add(store.getBlock());

            var blocksToInsert = new HashSet<BasicBlock>();
            var worklist = new Worklist<>(defBlocks);

            while (!worklist.isEmpty()) {
                var currentBlock = worklist.poll();

                for (var dfBlock : domInfo.getDF(currentBlock)) {
                    if (!blocksToInsert.contains(dfBlock)) {
                        blocksToInsert.add(dfBlock);
                        // phi 也是一个 def
                        worklist.add(dfBlock);
                    }
                }
            }

            for (var block : blocksToInsert) {
                var arg = block.addBlockArgument(alloca.allocatedType);
                // 记录 block argument 和 alloca 的对应关系
                blockArgToAlloca.put(arg, alloca);
                for (var predTerm : getCFG().getPredTermsOf(block))
                    predTerm.putParam(block, arg, ImmediateValues.undefined());
            }
        }
    }

    private void renaming(Function function) {
        var entry = function.entry;
        var entryIncomingVals = new HashMap<Instruction.Alloca, Value>();
        for (var var : promotableAllocas) entryIncomingVals.put(var, ImmediateValues.undefined());

        // 从 entry 向后继方向 DFS
        renamingRecursive(entry, new HashSet<>(), entryIncomingVals);
    }

    private void renamingRecursive(BasicBlock block, HashSet<BasicBlock> visited, HashMap<Instruction.Alloca, Value> incomingVals) {
        if (visited.contains(block)) return;
        visited.add(block);

        // 块开头时值改为 block argument
        for (var arg : block.args) incomingVals.put(blockArgToAlloca.get(arg), arg);

        for (var iterator = block.instructions.iterator(); iterator.hasNext(); ) {
            var instr = iterator.next();
            switch (instr) {
                case Instruction.Store store -> {
                    if (store.address.value instanceof Instruction.Alloca var) {
                        incomingVals.put(var, store.storeVal.value);
                        store.dispose();
                        iterator.remove();
                    }
                }
                case Instruction.Load load -> {
                    if (load.address.value instanceof Instruction.Alloca var) {
                        load.replaceAllUsesWith(incomingVals.get(var));
                        load.dispose();
                        iterator.remove();
                    }
                }
                default -> { }
            }
        }

        switch (block.terminator) {
            case Instruction.Jmp jmp -> {
                jmp.params.forEach(pair ->
                        pair.second().replaceValue(incomingVals.get(blockArgToAlloca.get(pair.first().value))));
                renamingRecursive(jmp.getTarget(), visited, new HashMap<>(incomingVals));
            }
            case Instruction.Br br -> {
                br.trueParams.forEach(pair ->
                        pair.second().replaceValue(incomingVals.get(blockArgToAlloca.get(pair.first().value))));
                br.falseParams.forEach(pair ->
                        pair.second().replaceValue(incomingVals.get(blockArgToAlloca.get(pair.first().value))));
                renamingRecursive(br.getTrueTarget(), visited, new HashMap<>(incomingVals));
                renamingRecursive(br.getFalseTarget(), visited, new HashMap<>(incomingVals));
            }
            default -> {}
        }
    }

}
