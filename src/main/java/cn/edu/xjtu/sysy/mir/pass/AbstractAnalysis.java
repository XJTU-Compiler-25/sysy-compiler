package cn.edu.xjtu.sysy.mir.pass;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Use;
import cn.edu.xjtu.sysy.mir.node.Module;

public abstract class AbstractAnalysis<T> extends ModuleVisitor {

    public enum AnalysisDirection {
        FORWARD {
            @Override
            public List<BasicBlock> getSuccBlocksOf(BasicBlock block) {
                return block.getSuccBlocks();
            }

            @Override
            public List<BasicBlock> getPredBlocksOf(BasicBlock block) {
                return block.getPredBlocks();
            }

            @Override
            public List<BasicBlock> getOrderedBlocks(List<BasicBlock> blocks)  {
                return topoSort(blocks);
            }

            @Override
            public List<Instruction> getOrderedInstrs(List<Instruction> instrs) {
                return instrs;
            }
        },
        BACKWARD {
            @Override
            public List<BasicBlock> getSuccBlocksOf(BasicBlock block) {
                return block.getPredBlocks();
            }
            @Override
            public List<BasicBlock> getPredBlocksOf(BasicBlock block) {
                return block.getSuccBlocks();
            }

            @Override
            public List<BasicBlock> getOrderedBlocks(List<BasicBlock> blocks)  {
                List<BasicBlock> order = topoSort(blocks);
                Collections.reverse(order);
                return order;
            }

            @Override
            public List<Instruction> getOrderedInstrs(List<Instruction> instrs) {
                return instrs.reversed();
            }
        };

        public abstract List<BasicBlock> getSuccBlocksOf(BasicBlock block);
        public abstract List<BasicBlock> getPredBlocksOf(BasicBlock block);
        public abstract List<BasicBlock> getOrderedBlocks(List<BasicBlock> blocks); 
        public abstract List<Instruction> getOrderedInstrs(List<Instruction> instrs);

        private static List<BasicBlock> topoSort(List<BasicBlock> cfg) {
            List<BasicBlock> result = new ArrayList<>();
            Map<BasicBlock, Integer> dfn = new HashMap<>();
            Map<BasicBlock, Integer> low = new HashMap<>();
            Stack<BasicBlock> stack = new Stack<>();
            dfs(cfg.getFirst(), dfn, low, stack, result);
            
            return result;
        }
        
        private static void dfs(BasicBlock block,
                            Map<BasicBlock, Integer> dfn, Map<BasicBlock, Integer> low, 
                            Stack<BasicBlock> stack, List<BasicBlock> result) {                
            dfn.put(block, dfn.size());
            low.put(block, low.size());
            stack.push(block);
            result.add(block);                    
            for (BasicBlock succ : block.getSuccBlocks()) {
                if (succ.equals(block)) {
                    block.isStronglyConnected = true;
                }
                if (!dfn.containsKey(succ)) {
                    dfs(succ, dfn, low, stack, result);
                    low.put(block, Integer.min(low.get(block), low.get(succ)));
                } else if (stack.contains(succ)) {
                    low.put(block, Integer.min(low.get(block), dfn.get(succ)));
                }
            }
            if (dfn.get(block).equals(low.get(block))) {
                var v = stack.pop();
                if (v.equals(block)) return;
                v.isStronglyConnected = true;
                while (!v.equals(block)) {
                    v = stack.pop();
                    v.isStronglyConnected = true;
                }
            }
        }
    }

    protected final Map<Instruction, T> flowBeforeInstr;
    protected final Map<Instruction, T> flowAfterInstr;
    protected final Map<BasicBlock, T> flowBeforeBlock;
    protected final Map<BasicBlock, T> flowAfterBlock;
    protected final AnalysisDirection direction;
    
    
    public AbstractAnalysis(ErrManager errManager, AnalysisDirection direction) {
        super(errManager);
        this.direction = direction;
        this.flowBeforeInstr = new HashMap<>();
        this.flowAfterInstr = new HashMap<>();
        this.flowBeforeBlock = new HashMap<>();
        this.flowAfterBlock = new HashMap<>();
    }

    public T getFlowBefore(BasicBlock block) {
        return flowBeforeBlock.get(block);
    }

    public T getFlowBefore(Use<Instruction> instr) {
        return flowBeforeInstr.get(instr);
    }

    public T getFlowAfter(BasicBlock block) {
        return flowAfterBlock.get(block);
    }

    public T getFlowAfter(Use<Instruction> instr) {
        return flowAfterInstr.get(instr);
    }

    protected abstract T initial();

    protected abstract void copy(T dst, T src);

    protected abstract void merge(T dst, T src1, T src2);

    protected abstract void flowThrough(Use<Instruction> instr, T in, T out);

    protected void init(List<BasicBlock> blocks) {
        for (var b : blocks) {
            flowBeforeBlock.put(b, initial());
            flowAfterBlock.put(b, initial());
        }
    }

    protected void meet(BasicBlock block) {
        var inBlocks = direction.getPredBlocksOf(block);
        if (inBlocks.isEmpty()) return;
        var inout = this.flowBeforeBlock.get(block);

        boolean copy = true;
        for (var e : inBlocks) {
            var in = this.flowAfterBlock.get(e);
            if (copy) {
                copy = false;
                copy(inout, in);
            } else {
                T tmp = initial();
                merge(tmp, inout, in);
                copy(inout, tmp);
            }
        }
    }

    protected boolean flowThrough(BasicBlock b) {
        T in = flowBeforeBlock.get(b);
        T out = flowAfterBlock.get(b);
        if (b.isStronglyConnected) {
            T newOut = initial();
            flowThrough(b, in, newOut);
            if (newOut.equals(out)) {
                return false;
            }
            copy(out, newOut);
            return true;
        }
        flowThrough(b, in, out);
        return true;
    }

    protected void flowThrough(BasicBlock b, T in, T out) {
        var instrs = direction.getOrderedInstrs(b.instructions);
        var inFlow = in;
        var outFlow = initial();
        for (var instr : instrs) {
            flowBeforeInstr.put(instr, inFlow);
            flowThrough(instr.getBlock(), inFlow, outFlow);
            flowAfterInstr.put(instr, outFlow);

            inFlow = outFlow;
            outFlow = initial();
        }
        copy(out, outFlow);
    }

    @Override
    public void visit(Function function) {
        var blocks = direction.getOrderedBlocks(function.blocks);
        init(blocks);
        Queue<BasicBlock> worklist = new ArrayDeque<>(blocks);
        Set<BasicBlock> inWorklist = new HashSet<>(blocks);
        while (!worklist.isEmpty()) {
            var e = worklist.poll();
            inWorklist.remove(e);
            meet(e);
            boolean changed = flowThrough(e);
            if (changed) {
                for (var succ : direction.getSuccBlocksOf(e)) {
                    if (inWorklist.add(e)) worklist.add(succ);
                }
            }
        }
    }

    public void printResult(Module module) {
        module.functions.values().forEach(this::printResult);
    }

    public void printResult(Function function) {
        for (var block : function.blocks) {
            System.out.println("%s:".formatted(block.label));
            for (var instr : block.instructions) {
                System.out.println(flowBeforeInstr.get(instr));
                System.out.println(instr);
                System.out.println(flowAfterInstr.get(instr));
                System.out.println();
            }
            System.out.println();
        }
    }
}
