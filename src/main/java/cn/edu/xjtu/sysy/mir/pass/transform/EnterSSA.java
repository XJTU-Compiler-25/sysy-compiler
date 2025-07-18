package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.pass.ModuleVisitor;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.*;

/*
 * EnterSSA 假设 DominanceAnalysis 已经完成
 * 按照 roife 老师的博客写的
 * ref: https://roife.github.io/posts/mem2reg-pass/#%E7%AE%97%E6%B3%95%E7%BB%86%E8%8A%82
 *
 * 由于我们直接从前端拿到了 local var 的信息，不用去分析 promotable alloca 了
 * var 的 def 就是 store，use 就是 load
 */
@SuppressWarnings("unchecked")
public final class EnterSSA extends ModuleVisitor {
    public EnterSSA(ErrManager errManager) {
        super(errManager);
    }

    @Override
    public void visit(Function function) {
        optimizeVars(function);
        insertPhi(function);
        renaming(function);
    }

    /*
     * 1. removeUnusedVar
     * 移除没有 users 的 var（函数参数会被函数 Use，所以不会被删除）
     * 2. rewriteSingleStoreVar
     * 如果一个 var 只有一个 store，且 load 能被 store 支配，则 load 取得的值可以直接替换为 store 的值
     * 3. rewriteLocalAssigns
     * 如果一个 var 的所有 store 和 load 都在同一个基本块内，就将 load 的值替换为前面最近的 store 的值
     */
    private void optimizeVars(Function function) {
        var vars = function.localVars;
        for (var iterator = vars.iterator(); iterator.hasNext(); ) {
            var var = iterator.next();
            // 没有被使用的变量直接删除
            if (var.notUsed()) iterator.remove();
        }
    }

    /*
     * 实际上是插 block argument，也就是在控制流汇合处更新 var 对应的 value
     *
     * 对于每个 var，在支配边界放置 BlockArgument:
     * 计算 DefBlocks 的迭代支配边界（支配边界闭包），得到要插入的基本块
     * 然后对基本块根据序号进行排序，使得插入 BlockArgument 的顺序确定化
     * 在得到的所有支配边界中放置 BlockArgument
     * 对于同一个 var，一个基本块内只会插入一个 BlockArgument
     * 注意更新 pred 中 jmp 指令的操作数
     */
    private void insertPhi(Function function) {
        for (var var : function.localVars) {
            insertPhiForVar(var);
        }
    }

    private void insertPhiForVar(Var var) {
        var defs = new HashSet<BasicBlock>();
        for (var use : var.usedBy) if (use.user instanceof Instruction.Store store) defs.add(store.getBlock());

        var blocksToInsert = new HashSet<BasicBlock>();
        var worklist = new Worklist<>(defs);

        while (!worklist.isEmpty()) {
            var currentBlock = worklist.poll();

            for (var dfBlock : currentBlock.df) {
                if (!blocksToInsert.contains(dfBlock)) {
                    blocksToInsert.add(dfBlock);
                    // phi 也是一个 def
                    worklist.add(dfBlock);
                }
            }
        }

        for (var block : blocksToInsert) {
            var arg = block.addBlockArgument(var);
            for (var predTerm : block.getPredTerminators())
                predTerm.putParam(block, arg, ImmediateValues.undefined());
        }
    }

    /*
     * 变量重命名
     * 整个过程是一个 DFS，为了减小内存开销所以用迭代的方式做
     * 建立一个 map 记录每个 alloca 当前对应的值。所有 alloca 在函数入口的值都初始化为 UndefValue
     * 用迭代 DFS 的方式遍历基本块，基本块信息存在结构体 RenamePassData 中，内部包含了一个数组 Values[]（即 IncomingVals[]）记录当前基本块末尾某个 alloca 对应的 Value（一次迭代只填入一个前驱流过来的值）
     * While (worklist!=NULL)
     * 标记当前基本块已经处理过，防止重复处理
     * 遍历当前块中第 4 步添加的 phi（程序里原来可能也有 phi，不能在这里处理）：
     * 找到 phi 对应的 var L，添加前驱块 Pred 到当前块的边（有几条边就要添加几次）：Phi.add(IncomingVals[L], Pred)
     * 设置 IncomingVals[L]=Phi
     * 如果当前基本块没有重复访问过，则对于基本块内的每条指令
     * 如果当前指令是 load，找到对应的 alloca L，将用到 load 结果的地方都替换成 IncomingVals[L]
     * 如果当前指令是 store，找到对应的 alloca L，更新版本 IncomingVals[L]=V 并删除这条 store
     * 将没有访问过的后继基本块加入 worklist
     */
    private void renaming(Function function) {
        var entryIncomingVals = new HashMap<Var, Value>();
        for (var var : function.localVars) entryIncomingVals.put(var, ImmediateValues.undefined());
        for (var var : function.params) entryIncomingVals.put(var, var);
        // 从 entry 向后继方向 DFS
        renamingRecursive(function.entry, new HashSet<>(), entryIncomingVals);
    }

    private void renamingRecursive(BasicBlock block, HashSet<BasicBlock> visited, HashMap<Var, Value> incomingVals) {
        if (visited.contains(block)) return;
        visited.add(block);

        // 块开头时值改为 block argument
        incomingVals.putAll(block.args);

        for (var iterator = block.instructions.iterator(); iterator.hasNext(); ) {
            var instr = iterator.next();
            switch (instr) {
                case Instruction.Store store -> {
                    if (store.address.value instanceof Var var && !var.isGlobal) {
                        incomingVals.put(var, store.storeVal.value);
                        store.dispose();
                        iterator.remove();
                    }
                }
                case Instruction.Load load -> {
                    if (load.address.value instanceof Var var && !var.isGlobal) {
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
                jmp.params.forEach((arg, use) -> use.replaceValue(incomingVals.get(arg.var)));
                renamingRecursive(jmp.getTarget(), visited, new HashMap<>(incomingVals));
            }
            case Instruction.Br br -> {
                br.trueParams.forEach((arg, use) -> use.replaceValue(incomingVals.get(arg.var)));
                br.falseParams.forEach((arg, use) -> use.replaceValue(incomingVals.get(arg.var)));
                renamingRecursive(br.getTrueTarget(), visited, new HashMap<>(incomingVals));
                renamingRecursive(br.getFalseTarget(), visited, new HashMap<>(incomingVals));
            }
            default -> {}
        }
    }

}
