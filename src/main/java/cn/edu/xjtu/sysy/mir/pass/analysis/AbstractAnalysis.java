package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

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
            public List<Instruction> getOrderedInstrs(BasicBlock block) {
                List<Instruction> instrs = new ArrayList<>(block.instructions);
                instrs.add(block.terminator);
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
            public List<Instruction> getOrderedInstrs(BasicBlock block) {
                List<Instruction> instrs = new ArrayList<>(block.instructions);
                instrs.add(block.terminator);
                Collections.reverse(instrs);
                return instrs;
            }
        };

        /** 按照当前方向获取后续块 */
        public abstract List<BasicBlock> getSuccBlocksOf(BasicBlock block);
        
        /** 按照当前方向获取前导块。
         * TODO: 还没有cache */
        public abstract List<BasicBlock> getPredBlocksOf(BasicBlock block);
        
        /** 按照当前方向获取排序好的基本块，
         * 并对基本块标记是否在一个size大于1的强连通分量中或者存在自环 
         * i.e. 这个基本块可能在一个循环里。
         * see {@link BasicBlock#isStronglyConnected}
         */
        public abstract List<BasicBlock> getOrderedBlocks(List<BasicBlock> blocks); 
        
        /** 按照当前方向获取排序好的指令列表 */
        public abstract List<Instruction> getOrderedInstrs(BasicBlock block);

        /** 对基本块进行拓扑排序 */
        private static List<BasicBlock> topoSort(List<BasicBlock> cfg) {
            List<BasicBlock> result = new ArrayList<>();
            Map<BasicBlock, Integer> dfn = new HashMap<>();
            Map<BasicBlock, Integer> low = new HashMap<>();
            Stack<BasicBlock> stack = new Stack<>();
            dfs(cfg.getFirst(), dfn, low, stack, result);
            
            return result;
        }
        
        /** tarjan算法求解强连通分量 */
        private static void dfs(BasicBlock block,
                            Map<BasicBlock, Integer> dfn, Map<BasicBlock, Integer> low, 
                            Stack<BasicBlock> stack, List<BasicBlock> result) {                
            dfn.put(block, dfn.size());
            low.put(block, low.size());
            stack.push(block);
            result.add(block);                    
            for (BasicBlock succ : block.getSuccBlocks()) {
                // 自环的情况
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
                // 如果是大小为1的强连通分量，忽略它
                if (v.equals(block)) return;
                // 否则进行标记（存在循环）
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

    public T getFlowBefore(Instruction instr) {
        return flowBeforeInstr.get(instr);
    }

    public T getFlowAfter(BasicBlock block) {
        return flowAfterBlock.get(block);
    }

    public T getFlowAfter(Instruction instr) {
        return flowAfterInstr.get(instr);
    }

    /** 初始情况，也就是格的下界 */
    protected abstract T initial();

    /** 拷贝，将src的值拷贝到dst */
    protected abstract void copy(T dst, T src);

    /** 合并，在控制流交汇的地方选择取交或者取并 */
    protected abstract void merge(T dst, T src1, T src2);

    /** 单条语句的转换函数 */
    protected abstract void flowThrough(Instruction instr, T in, T out);

    /** 初始化每个基本块 */
    protected void init(List<BasicBlock> blocks) {
        for (var b : blocks) {
            flowBeforeBlock.put(b, initial());
            flowAfterBlock.put(b, initial());
        }
    }

    /** 处理控制流交汇（TODO: 基本块参数相关的部分还没写） */
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

    /** 处理基本块 */
    protected boolean flowThrough(BasicBlock b) {
        T in = flowBeforeBlock.get(b);
        T out = flowAfterBlock.get(b);
        /* 如果b在一个size > 1的强连通分量或者在自环中
         * i.e. b可能在一个循环结构中
         * 执行是否已经到达不动点的检测
         */
        if (b.isStronglyConnected) {
            T newOut = initial();
            flowThrough(b, in, newOut);
            if (newOut.equals(out)) {
                return false;
            }
            copy(out, newOut);
            return true;
        }
        // 在顺序结构中，直接返回true
        flowThrough(b, in, out);
        return true;
    }

    /** 基本块的转换函数 */
    protected void flowThrough(BasicBlock block, T in, T out) {
        var instrs = direction.getOrderedInstrs(block);
        var inFlow = in;
        var outFlow = initial();
        // 按顺序遍历每条指令，进行分析
        for (var instr : instrs) {
            flowBeforeInstr.put(instr, inFlow);
            flowThrough(instr, inFlow, outFlow);
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
        var worklist = new Worklist<>(blocks);
        while (!worklist.isEmpty()) {
            var e = worklist.poll();
            meet(e);
            boolean changed = flowThrough(e);
            if (changed) {
                for (var succ : direction.getSuccBlocksOf(e)) worklist.add(succ);
            }
        }
    }

    public void printResult(Module module) {
        module.functions.values().forEach(this::printResult);
    }

    public void printResult(Function function) {
        for (var block : function.blocks) {
            System.out.printf("%s:%n", block.label);
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
