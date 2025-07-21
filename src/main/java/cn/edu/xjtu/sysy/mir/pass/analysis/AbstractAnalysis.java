package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.*;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.mir.util.CFGUtils;
import cn.edu.xjtu.sysy.util.Worklist;

public abstract class AbstractAnalysis<T> extends ModuleVisitor<T> {

    public enum AnalysisDirection {
        FORWARD {
            @Override
            public Set<BasicBlock> getSuccBlocksOf(BasicBlock block) {
                return block.succs;
            }

            @Override
            public Set<BasicBlock> getPredBlocksOf(BasicBlock block) {
                return block.preds;
            }

            @Override
            public List<BasicBlock> getOrderedBlocks(BasicBlock entry)  {
                return CFGUtils.getReversePostOrderedBlocks(entry);
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
            public Set<BasicBlock> getSuccBlocksOf(BasicBlock block) {
                return block.preds;
            }
            @Override
            public Set<BasicBlock> getPredBlocksOf(BasicBlock block) {
                return block.succs;
            }

            @Override
            public List<BasicBlock> getOrderedBlocks(BasicBlock entry)  {
                return CFGUtils.getPostOrderedBlocks(entry);
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
        public abstract Set<BasicBlock> getSuccBlocksOf(BasicBlock block);
        
        /** 按照当前方向获取前导块。*/
        public abstract Set<BasicBlock> getPredBlocksOf(BasicBlock block);
        
        /** 按照当前方向获取排序好的基本块 */
        public abstract List<BasicBlock> getOrderedBlocks(BasicBlock entry);
        
        /** 按照当前方向获取排序好的指令列表 */
        public abstract List<Instruction> getOrderedInstrs(BasicBlock block);

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
        if (b.loopDepth > 0) {
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
        var blocks = direction.getOrderedBlocks(function.entry);
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
        module.getFunctions().forEach(this::printResult);
    }

    public void printResult(Function function) {
        for (var block : function.getTopoSortedBlocks()) {
            System.out.printf("bb%s:%n", block.order);
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
