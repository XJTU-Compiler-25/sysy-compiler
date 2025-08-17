package cn.edu.xjtu.sysy.mir.pass.transform.loop;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.*;

import java.util.*;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

// 循环规范化
@SuppressWarnings({"unchecked", "rawtypes"})
public final class LoopCanonicalize extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();

    private LoopInfo loopInfo;
    private DomInfo domInfo;

    @Override
    public void visit(Module module) {
        loopInfo = getResult(LoopAnalysis.class);

        super.visit(module);

        invalidate(CFGAnalysis.class);
    }

    private Function currentFunction;
    private List<Loop> loops;
    @Override
    public void visit(Function function) {
        currentFunction = function;
        // 先处理内层
        loops = loopInfo.getLoopsInToOut(function);

        insertPreheader();
        insertDedicatedExit();
        LCSSA();
    }

    private void insertPreheader() {
        for (var loop : loops) {
            if (loop.getPreheader() != null) continue;

            var header = loop.header;
            var outsidePreds = CFGAnalysis.getPredBlocksOf(header).stream()
                    .filter(pred -> !loop.contains(pred))
                    .toList();

            if (outsidePreds.isEmpty()) continue; // 没有外部前驱

            // 创建 preheader
            var preheader = new BasicBlock(currentFunction);
            currentFunction.addBlock(preheader);
            helper.changeBlock(preheader);
            var jmp = helper.insertJmp(header);
            helper.changeBlockToNull();

            var argMap = new HashMap<BlockArgument, BlockArgument>();
            for (var arg : header.args) {
                var newArg = preheader.addBlockArgument(arg.type);
                argMap.put(arg, newArg);
                jmp.putParam(arg, newArg);
            }

            // 将所有外部前驱指向 preheader，并将 header 的参数迁移给 preheader
            for (var pred : outsidePreds) {
                var term = pred.terminator;
                argMap.forEach((oldArg, newArg) -> term.replaceParam(header, oldArg, newArg));
                term.replaceTarget(header, preheader);
            }

        }
    }

    private void insertDedicatedExit() {
        for (var loop : loops) {
            var exits = loop.getExits();

            for (var exit : exits) {
                var preds = CFGAnalysis.getPredBlocksOf(exit);
                var insidePreds = new HashSet<BasicBlock>();
                var dedicated = true;
                for (var pred : preds) {
                    if (loop.contains(pred)) insidePreds.add(pred);
                    else dedicated = false;
                }

                if (dedicated) continue;

                var dedicatedExit = new BasicBlock(currentFunction);
                currentFunction.addBlock(dedicatedExit);

                helper.changeBlock(dedicatedExit);
                var jmp = helper.insertJmp(exit);
                helper.changeBlockToNull();

                var argMap = new HashMap<BlockArgument, BlockArgument>();
                // 将 exit 的参数迁移到 dedicated exit
                for (var arg : exit.args) {
                    var newArg = dedicatedExit.addBlockArgument(arg.type);
                    argMap.put(arg, newArg);
                    jmp.putParam(arg, newArg);
                }

                for (var pred : insidePreds) {
                    var term = pred.terminator;
                    argMap.forEach((oldArg, newArg) -> term.replaceParam(exit, oldArg, newArg));
                    term.replaceTarget(exit, dedicatedExit);
                }
            }
        }
    }

    private void LCSSA() {
        domInfo = getRefreshedResult(DominanceAnalysis.class);
        loopInfo = getRefreshedResult(LoopAnalysis.class);

        for (var loop : loops) {
            var liveOutValues = new HashSet<Value>();
            for (var block : loop.blocks) {
                for (var arg : block.args) {
                    for (var use : arg.usedBy) {
                        if (use.user instanceof Instruction userInst && !loop.contains(userInst.getBlock())) {
                            liveOutValues.add(arg);
                            break;
                        }
                    }
                }

                for (var inst : block.instructions) {
                    if (inst.notProducingValue()) continue;

                    for (var use : inst.usedBy) {
                        if (use.user instanceof Instruction userInst && !loop.contains(userInst.getBlock())) {
                            liveOutValues.add(inst);
                            break;
                        }
                    }
                }
            }

            if (liveOutValues.isEmpty()) continue;

            var valueToPhiMap = new HashMap<Value, HashMap<BasicBlock, BlockArgument>>();
            var exits = loop.getExits();

            for (var value : liveOutValues) {
                var phiMap = new HashMap<BasicBlock, BlockArgument>();
                for (var exit : exits) {
                    var lcssaArg = exit.addBlockArgument(value.type);
                    phiMap.put(exit, lcssaArg);
                }
                valueToPhiMap.put(value, phiMap);
            }

            var exitings = loop.getExitings();
            for (var exiting : exitings) {
                var term = exiting.terminator;

                switch (term) {
                    case Instruction.Jmp jmp -> {
                        var exit = jmp.getTarget();
                        for (var value : liveOutValues) {
                            var arg = valueToPhiMap.get(value).get(exit);
                            term.putParam(exit, arg, value);
                        }
                    }
                    case Instruction.AbstractBr br -> {
                        var tp = br.getTrueTarget();
                        if (!loop.contains(tp)) {
                            for (var value : liveOutValues) {
                                var arg = valueToPhiMap.get(value).get(tp);
                                term.putParam(tp, arg, value);
                            }
                        }
                        var fp = br.getFalseTarget();
                        if (!loop.contains(fp)) {
                            for (var value : liveOutValues) {
                                var arg = valueToPhiMap.get(value).get(fp);
                                term.putParam(fp, arg, value);
                            }
                        }
                    }
                    default -> { }
                }
            }

            for (var value : liveOutValues) {
                var usesOutsideLoop = new ArrayList<Use>();
                for (var use : value.usedBy) {
                    if (use.user instanceof Instruction userInst && !loop.contains(userInst.getBlock())) usesOutsideLoop.add(use);
                }

                for (var use : usesOutsideLoop) {
                    var userInst = (Instruction) use.user;
                    var userBlock = userInst.getBlock();
                    var phiMapForValue = valueToPhiMap.get(value);

                    var correctArg = findCorrectLcssaArg(userBlock, phiMapForValue);
                    use.replaceValue(correctArg);
                }
            }
        }
    }

    private BlockArgument findCorrectLcssaArg(BasicBlock userBlock, Map<BasicBlock, BlockArgument> phiMap) {
        if(phiMap.containsKey(userBlock)) return phiMap.get(userBlock);

        var current = domInfo.getIDom(userBlock);
        while (current != null) {
            if (phiMap.containsKey(current)) return phiMap.get(current);
            current = domInfo.getIDom(current);
        }

        return unreachable();
    }

}
