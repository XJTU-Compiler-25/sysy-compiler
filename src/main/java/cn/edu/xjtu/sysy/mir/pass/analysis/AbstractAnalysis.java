/*
package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.*;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Pair;
import cn.edu.xjtu.sysy.util.Worklist;

public abstract class AbstractAnalysis<Result> extends ModuleVisitor<Result> {

    public static final boolean FORWARD = true;
    public static final boolean BACKWARD = false;

    protected final boolean isForward;
    protected final Map<Instruction, Result> flowBeforeInstr;
    protected final Map<Instruction, Result> flowAfterInstr;
    protected final Map<BasicBlock, Result> flowBeforeBlock;
    protected final Map<BasicBlock, Result> flowAfterBlock;

    public AbstractAnalysis(Pipeline<Module> pipeline, boolean isForward) {
        super(pipeline);
        this.isForward = isForward;
        this.flowBeforeInstr = new HashMap<>();
        this.flowAfterInstr = new HashMap<>();
        this.flowBeforeBlock = new HashMap<>();
        this.flowAfterBlock = new HashMap<>();
    }

    public Set<BasicBlock> getPredBlocksOf(BasicBlock block) {
        return isForward ? getCFG().getPredBlocksOf(block) : getCFG().getSuccBlocksOf(block);
    }

    public Set<BasicBlock> getSuccBlocksOf(BasicBlock block) {
        return isForward ? getCFG().getSuccBlocksOf(block) : getCFG().getPredBlocksOf(block);
    }

    public List<BasicBlock> getOrderedBlocksOf(Function function) {
        return isForward ? getCFG().getRPOBlocks(function) : getCFG().getPOBlocks(function);
    }

    public ArrayList<Instruction> getOrderedInstrs(BasicBlock block) {
        var instrs = new ArrayList<>(block.instructions);
        instrs.add(block.terminator);
        if (!isForward) Collections.reverse(instrs);
        return instrs;
    }

    public Result getFlowBefore(BasicBlock block) {
        return flowBeforeBlock.get(block);
    }

    public Result getFlowBefore(Instruction instr) {
        return flowBeforeInstr.get(instr);
    }

    public Result getFlowAfter(BasicBlock block) {
        return flowAfterBlock.get(block);
    }

    public Result getFlowAfter(Instruction instr) {
        return flowAfterInstr.get(instr);
    }

    // 初始情况，也就是格的下界
    protected abstract Result initial();

    // 拷贝，将src的值拷贝到dst
    protected abstract void copy(Result dst, Result src);

    // 合并，在控制流交汇的地方选择取交或者取并
    protected abstract void merge(Result dst, Result src1, Result src2);

    // 单条语句的转换函数
    protected abstract void flowThrough(Instruction instr, Result in, Result out);

    /** 处理控制流交汇（TODO: 基本块参数相关的部分还没写） */
    protected boolean meet(BasicBlock block, Result in) {
        /* 如果b在一个size > 1的强连通分量或者在自环中
         * i.e. b可能在一个循环结构中
         * 执行是否已经到达不动点的检测
         */
        if (!flowBeforeBlock.containsKey(block)) {
            flowBeforeBlock.put(block, in);
            return true;
        }
        var inout = flowBeforeBlock.get(block);
        Result tmp = initial();
        merge(tmp, inout, in);
        if (block.loopDepth == 0) {
            flowBeforeBlock.put(block, tmp);
            return true;
        }
        if (tmp.equals(inout)) {
            return false;
        }
        flowBeforeBlock.put(block, tmp);
        return true;
    }

    /** 处理基本块 */
    protected void flowThrough(BasicBlock b) {
        Result in = flowBeforeBlock.get(b);
        flowThrough(b, in);
    }

    /** 基本块的转换函数 */
    protected void flowThrough(BasicBlock block, Result in) {
        var instrs = getOrderedInstrs(block);
        var inFlow = in;
        var outFlow = initial();
        // 按顺序遍历每条指令，进行分析
        for (var instr : instrs) {
            // System.out.println(instr);
            flowBeforeInstr.put(instr, inFlow);
            flowThrough(instr, inFlow, outFlow);
            flowAfterInstr.put(instr, outFlow);

            inFlow = outFlow;
            outFlow = initial();
        }
        flowAfterBlock.put(block, inFlow);
    }

    @Override
    public void visit(Function function) {
        var blocks = getOrderedBlocksOf(function);
        var worklist =
                new Worklist<>(Collections.singleton(new Pair<>(blocks.getFirst(), initial())));
        while (!worklist.isEmpty()) {
            var e = worklist.poll();
            var cur = e.first();
            var in = e.second();
            boolean changed = meet(cur, in);
            if (changed) {
                flowThrough(cur);
                for (var succ : getSuccBlocksOf(cur))
                    worklist.add(new Pair<>(succ, flowAfterBlock.get(cur)));
            }
        }
    }

    public void printResult(Module module) {
        module.getFunctions().forEach(this::printResult);
    }

    public void printResult(Function function) {
        for (var block : getCFG().getRPOBlocks(function)) {
            System.out.printf("%s:%n", block.shortName());
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
*/
