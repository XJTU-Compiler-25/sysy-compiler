package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModuleAnalysis;

import java.util.*;

// 求出基本块的 pred, succ 列表
public final class CFGAnalysis extends ModuleAnalysis<CFG> {

    public static CFG run(Module module) {
        return new CFGAnalysis().process(module);
    }

    public static CFG run(Function function) {
        return new CFGAnalysis().process(function);
    }

    public static Set<BasicBlock> getPredBlocksOf(BasicBlock block) {
        var preds = new HashSet<BasicBlock>();
        for (var use : block.usedBy) {
            if (use.user instanceof Instruction.Terminator term) {
                preds.add(term.getBlock());
            }
        }
        return preds;
    }

    public static Set<Instruction.Terminator> getPredTermsOf(BasicBlock block) {
        var preds = new HashSet<Instruction.Terminator>();
        for (var use : block.usedBy) {
            if (use.user instanceof Instruction.Terminator term) {
                preds.add(term);
            }
        }
        return preds;
    }

    public static Set<BasicBlock> getSuccBlocksOf(BasicBlock block) {
        var succs = new HashSet<BasicBlock>();
        switch(block.terminator) {
            case Instruction.Jmp jmp -> succs.add(jmp.getTarget());
            case Instruction.Br br -> {
                succs.add(br.getTrueTarget());
                succs.add(br.getFalseTarget());
            }
            default -> { }
        }
        return succs;
    }

    @Override
    public CFG process(Module module) {
        var predTermMap = new HashMap<BasicBlock, Set<Instruction.Terminator>>();
        var predMap = new HashMap<BasicBlock, Set<BasicBlock>>();
        var succMap = new HashMap<BasicBlock, Set<BasicBlock>>();

        for (var function : module.getFunctions()) {
            for (var block : function.blocks) {
                var predTerms = new HashSet<Instruction.Terminator>();
                var preds = new HashSet<BasicBlock>();
                var succs = new HashSet<BasicBlock>();
                predMap.put(block, preds);
                succMap.put(block, succs);
                predTermMap.put(block, predTerms);

                for (var use : block.usedBy) {
                    if (use.user instanceof Instruction.Terminator term) {
                        predTerms.add(term);
                        preds.add(term.getBlock());
                    }
                }

                switch(block.terminator) {
                    case Instruction.Jmp jmp -> succs.add(jmp.getTarget());
                    case Instruction.Br br -> succs.addAll(br.getTargets());
                    default -> { }
                }
            }
        }

        return new CFG(predMap, succMap, predTermMap);
    }

    public CFG process(Function function) {
        var predTermMap = new HashMap<BasicBlock, Set<Instruction.Terminator>>();
        var predMap = new HashMap<BasicBlock, Set<BasicBlock>>();
        var succMap = new HashMap<BasicBlock, Set<BasicBlock>>();

        for (var block : function.blocks) {
            var predTerms = new HashSet<Instruction.Terminator>();
            var preds = new HashSet<BasicBlock>();
            var succs = new HashSet<BasicBlock>();
            predMap.put(block, preds);
            succMap.put(block, succs);
            predTermMap.put(block, predTerms);

            for (var use : block.usedBy) {
                if (use.user instanceof Instruction.Terminator term) {
                    predTerms.add(term);
                    preds.add(term.getBlock());
                }
            }

            switch(block.terminator) {
                case Instruction.Jmp jmp -> succs.add(jmp.getTarget());
                case Instruction.Br br -> {
                    succs.add(br.getTrueTarget());
                    succs.add(br.getFalseTarget());
                }
                default -> { }
            }
        }

        return new CFG(predMap, succMap, predTermMap);
    }

}
