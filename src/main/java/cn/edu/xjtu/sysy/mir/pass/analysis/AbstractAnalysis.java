package cn.edu.xjtu.sysy.mir.pass.analysis;

import java.util.*;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
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

    /** 初始情况，也就是格的下界 */
    protected abstract Result initial();

    /** 拷贝，将src的值拷贝到dst */
    protected abstract void copy(Result dst, Result src);

    /** 合并，在控制流交汇的地方选择取交或者取并 */
    protected abstract void merge(Result dst, Result src1, Result src2);

    /** 单条语句的转换函数 */
    protected abstract void flowThrough(Instruction instr, Result in, Result out);

    /** 初始化每个基本块 */
    protected void init(List<BasicBlock> blocks) {
        for (var b : blocks) {
            flowBeforeBlock.put(b, initial());
            flowAfterBlock.put(b, initial());
        }
    }

    /** 处理控制流交汇（TODO: 基本块参数相关的部分还没写） */
    protected void meet(BasicBlock block) {
        var inBlocks = getPredBlocksOf(block);
        if (inBlocks.isEmpty()) return;
        var inout = this.flowBeforeBlock.get(block);

        boolean copy = true;
        for (var e : inBlocks) {
            var in = this.flowAfterBlock.get(e);
            if (copy) {
                copy = false;
                copy(inout, in);
            } else {
                Result tmp = initial();
                merge(tmp, inout, in);
                copy(inout, tmp);
            }
        }
    }

    /** 处理基本块 */
    protected boolean flowThrough(BasicBlock b) {
        Result in = flowBeforeBlock.get(b);
        Result out = flowAfterBlock.get(b);
        /* 如果b在一个size > 1的强连通分量或者在自环中
         * i.e. b可能在一个循环结构中
         * 执行是否已经到达不动点的检测
         */
        if (b.loopDepth > 0) {
            Result newOut = initial();
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
    protected void flowThrough(BasicBlock block, Result in, Result out) {
        var instrs = getOrderedInstrs(block);
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
        var blocks = getOrderedBlocksOf(function);
        init(blocks);
        var worklist = new Worklist<>(blocks);
        while (!worklist.isEmpty()) {
            var e = worklist.poll();
            meet(e);
            boolean changed = flowThrough(e);
            if (changed) {
                for (var succ : getSuccBlocksOf(e)) worklist.add(succ);
            }
        }
    }

    public void printResult(Module module) {
        module.getFunctions().forEach(this::printResult);
    }

    public void printResult(Function function) {
        for (var block : getCFG().getRPOBlocks(function)) {
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
