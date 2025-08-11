package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFG;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.DomInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.*;

import static cn.edu.xjtu.sysy.util.Assertions.unsupported;

@SuppressWarnings("unchecked")
public final class EnterSSA extends ModulePass<Void> {

    private CFG cfg;
    private DomInfo domInfo;
    @Override
    public void visit(Module module) {
        cfg = getResult(CFGAnalysis.class);
        domInfo = getResult(DominanceAnalysis.class);
        super.visit(module);
    }

    @Override
    public void visit(Function function) {
        hoistAllocas(function);
        findCandidate(function);
        for (var param : function.params) {
            var arg = param.second();
            arg.type = ((Type.Pointer) arg.type).baseType;
            promotableVar.add(arg);
        }
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

    private final ArrayList<Value> promotableVar = new ArrayList<>();
    private void findCandidate(Function function) {
        promotableVar.clear();
        // 所有 alloca 都在函数 entry 块
        for (var iterator = function.entry.instructions.iterator(); iterator.hasNext(); ) {
            var instr = iterator.next();
            if (instr instanceof Instruction.Alloca alloca) {
                // 标量值的 alloca 可以提升
                if (alloca.allocatedType instanceof Type.Scalar
                        // 从未被取过地址
                        && alloca.usedBy.stream().noneMatch(it -> it.user instanceof Instruction.GetElemPtr)) {
                    promotableVar.add(alloca);
                    iterator.remove();
                }
            }
        }
    }

    private final HashMap<BlockArgument, Value> blockArgToVar = new HashMap<>();
    private void insertBlockArgument() {
        blockArgToVar.clear();

        for (var var : promotableVar) {
            var varType = switch (var) {
                case Instruction.Alloca it -> it.allocatedType;
                case BlockArgument it -> it.type;
                default -> throw new RuntimeException();
            };

            var defBlocks = new HashSet<BasicBlock>();
            for (var use : var.usedBy) if (use.user instanceof Instruction.Store store) defBlocks.add(store.getBlock());

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
                var arg = block.addBlockArgument(varType);
                // 记录 block argument 和 alloca 的对应关系
                blockArgToVar.put(arg, var);
                for (var predTerm : cfg.getPredTermsOf(block))
                    predTerm.putParam(block, arg, ImmediateValues.undefined());
            }
        }
    }

    private void renaming(Function function) {
        var entry = function.entry;
        var entryIncomingVals = new HashMap<Value, Value>();
        for (var var : promotableVar) {
            switch (var) {
                case BlockArgument it -> entryIncomingVals.put(it, it);
                case Instruction.Alloca it -> entryIncomingVals.put(var, ImmediateValues.undefined());
                default -> unsupported(var);
            }
        }

        // 从 entry 向后继方向 DFS
        renamingRecursive(entry, new HashSet<>(), entryIncomingVals);
    }

    private void renamingRecursive(BasicBlock block, HashSet<BasicBlock> visited, HashMap<Value, Value> incomingVals) {
        if (visited.contains(block)) return;
        visited.add(block);

        // 块开头时值改为 block argument
        for (var arg : block.args) incomingVals.put(blockArgToVar.get(arg), arg);

        for (var iterator = block.instructions.iterator(); iterator.hasNext(); ) {
            var instr = iterator.next();
            switch (instr) {
                case Instruction.Store store -> {
                    switch (store.address.value) {
                        case Instruction.Alloca var -> {
                            incomingVals.put(var, store.storeVal.value);
                            store.dispose();
                            iterator.remove();
                        }
                        case BlockArgument var -> {
                            incomingVals.put(var, store.storeVal.value);
                            store.dispose();
                            iterator.remove();
                        }
                        default -> { }
                    }
                }
                case Instruction.Load load -> {
                    switch (load.address.value) {
                        case Instruction.Alloca var -> {
                            load.replaceAllUsesWith(incomingVals.get(var));
                            load.dispose();
                            iterator.remove();
                        }
                        case BlockArgument var -> {
                            load.replaceAllUsesWith(incomingVals.get(var));
                            load.dispose();
                            iterator.remove();
                        }
                        default -> { }
                    }
                }
                default -> { }
            }
        }

        switch (block.terminator) {
            case Instruction.Jmp jmp -> {
                jmp.params.forEach((arg, use) -> use.replaceValue(incomingVals.get(blockArgToVar.get(arg))));
                renamingRecursive(jmp.getTarget(), visited, new HashMap<>(incomingVals));
            }
            case Instruction.Br br -> {
                br.trueParams.forEach((arg, use) -> use.replaceValue(incomingVals.get(blockArgToVar.get(arg))));
                br.falseParams.forEach((arg, use) -> use.replaceValue(incomingVals.get(blockArgToVar.get(arg))));
                renamingRecursive(br.getTrueTarget(), visited, new HashMap<>(incomingVals));
                renamingRecursive(br.getFalseTarget(), visited, new HashMap<>(incomingVals));
            }
            default -> {}
        }
    }

}
