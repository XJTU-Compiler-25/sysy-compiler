package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pass;
import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.util.Pair;
import cn.edu.xjtu.sysy.util.Worklist;

import java.util.HashMap;
import java.util.HashSet;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.unsupported;
import static cn.edu.xjtu.sysy.util.Pair.pair;

// Sparse Conditional Constant Propagation
// 稀有条件常量传播
@SuppressWarnings("unchecked")
public final class SCCP extends AbstractTransform {
    public SCCP(Pipeline<Module> pipeline) { super(pipeline); }

    @Override
    public Class<? extends Pass<Module, ?>>[] invalidates() {
        // 会 fold const branch 成 jmp 所以
        return new Class[] { CFGAnalysis.class };
    }
    @Override
    public void visit(Module module) {
        for (var function : module.getFunctions()) run(function);
    }

    private static final InstructionHelper helper = new InstructionHelper();

    private record LatticeValue(int type, ImmediateValue value) {
        // constant
        private LatticeValue(ImmediateValue value) { this(0, value); }
        private boolean isConst() { return type == 0; }
        private boolean isTop() { return type == 1; }
        private boolean isBottom() { return type == 2; }
    }

    private static final LatticeValue TOP = new LatticeValue(1, null);
    // bottom
    private static final LatticeValue BOT = new LatticeValue(2, null);
    private final HashMap<Value, LatticeValue> latCells = new HashMap<>();
    private final Worklist<Pair<BasicBlock, BasicBlock>> cfgWL = new Worklist<>();
    private final Worklist<Instruction> ssaWL = new Worklist<>();
    private final HashSet<BasicBlock> executable = new HashSet<>();

    private boolean setLattice(Value value, LatticeValue newLat) {
        var oldLat = getLattice(value);
        if (!oldLat.equals(newLat)) {
            latCells.put(value, newLat);
            for (var use : value.usedBy) {
                if (use.user instanceof Instruction instr) ssaWL.add(instr);
            }
            return true;
        }
        return false;
    }

    private LatticeValue getLattice(Value value) {
        if (value instanceof ImmediateValue imm) return new LatticeValue(imm);
        return latCells.computeIfAbsent(value, _ -> TOP);
    }

    private LatticeValue meet(LatticeValue a, LatticeValue b) {
        // BOT meet X = BOT
        if (a == BOT || b == BOT) return BOT;
        // TOP meet X = X
        if (a == TOP) return b;
        if (b == TOP) return a;
        // C1 meet C2 = C1 == C2 ? C1 : BOT
        if (a.value.equals(b.value)) return a;
        return BOT;
    }

    public boolean run(Function function) {
        var modified = false;
        latCells.clear();
        executable.clear();
        cfgWL.clear();
        ssaWL.clear();

        // global var 和 param 都是默认 BOTTOM，其他默认 TOP
        for (var global : function.getModule().getGlobalVars()) latCells.put(global, BOT);
        for (var arg : function.params) latCells.put(arg.second(), BOT);

        // 添加到入口块的边
        var entry = function.entry;
        cfgWL.add(pair(null, entry));

        while (!cfgWL.isEmpty() || !ssaWL.isEmpty()) {
            if (!cfgWL.isEmpty()) {
                var edge = cfgWL.poll();
                var fromBlock = edge.first();
                var toBlock = edge.second();

                // 先更新其 phi
                if (toBlock != entry) {
                    for (var arg : toBlock.args) modified |= visitBlockArgument(fromBlock, arg);
                }

                // 如果本来不在 exe bb 里面，就进行处理并加到 exe bb 里面
                if (executable.add(toBlock)) {
                    for (var instr : toBlock.instructions) modified |= visitInstruction(instr);
                    modified |= visitTerminator(toBlock.terminator);
                }
            } else { // !ssaWL.isEmpty()
                var instr = ssaWL.poll();
                // 如果指令当前可达，则尝试更新状态
                if (executable.contains(instr.getBlock())) modified |= visitInstruction(instr);
            }
        }

        if (modified) transform(function);
        return modified;
    }

    private boolean visitBlockArgument(BasicBlock fromBlock, BlockArgument arg) {
        var oldLat = getLattice(arg);
        var incoming = getLattice(fromBlock.terminator.getParam(arg.block, arg));
        var newLat = meet(oldLat, incoming);
        return setLattice(arg, newLat);
    }

    private boolean visitInstruction(Instruction inst) {
        if (inst instanceof Terminator term) return visitTerminator(term);
        // 对于 instr，用 fold 代替 meet，因为两个不同常量相交可以仍为常量
        var newLat = fold(inst);
        return setLattice(inst, newLat);
    }

    private LatticeValue fold(Instruction inst) {
        switch (inst) {
            // 不能被折叠的指令
            case Alloca _, Load _, Store _, GetElemPtr _, Call _, CallExternal _ -> {
                return BOT;
            }
            case Terminator _ -> {
                return unsupported(inst);
            }
            // 数学运算
            case IAdd it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(intConst(lV + rV));
            }
            case ISub it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(intConst(lV - rV));
            }
            case IMul it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(intConst(lV * rV));
            }
            case IDiv it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                // 不折叠除数为 0
                if (rV == 0) return BOT;
                return new LatticeValue(intConst(lV / rV));
            }
            case IMod it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                // 不折叠除数为 0
                if (rV == 0) return BOT;
                return new LatticeValue(intConst(lV % rV));
            }
            case INeg it -> {
                var l = getLattice(it.lhs.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((IntConst) l.value).value;

                return new LatticeValue(intConst(-lV));
            }
            case FAdd it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(lV + rV));
            }
            case FSub it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(lV - rV));
            }
            case FMul it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(lV * rV));
            }
            case FDiv it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(lV / rV));
            }
            case FMod it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(lV % rV));
            }
            case FNeg it -> {
                var l = getLattice(it.lhs.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;

                return new LatticeValue(floatConst(-lV));
            }

            // 相等运算
            case IEq it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(lV == rV ? iOne : iZero);
            }
            case INe it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(lV != rV ? iOne : iZero);
            }
            case FEq it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(lV == rV ? iOne : iZero);
            }
            case FNe it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(lV != rV ? iOne : iZero);
            }

            // 比较运算
            case IGe it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(lV >= rV ? iOne : iZero);
            }
            case ILe it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(lV <= rV ? iOne : iZero);
            }
            case IGt it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(lV > rV ? iOne : iZero);
            }
            case ILt it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                return new LatticeValue(lV < rV ? iOne : iZero);
            }
            case FGe it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(lV >= rV ? iOne : iZero);
            }
            case FLe it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(lV <= rV ? iOne : iZero);
            }
            case FGt it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(lV > rV ? iOne : iZero);
            }
            case FLt it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(lV < rV ? iOne : iZero);
            }

            // 转型运算
            case I2F it -> {
                var l = getLattice(it.value.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((IntConst) l.value).value;

                return new LatticeValue(floatConst((float) lV));
            }
            case F2I it -> {
                var l = getLattice(it.value.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;

                return new LatticeValue(intConst((int) lV));
            }
            case BitCastI2F it -> {
                var l = getLattice(it.value.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((IntConst) l.value).value;

                return new LatticeValue(floatConst(Float.intBitsToFloat(lV)));
            }
            case BitCastF2I it -> {
                var l = getLattice(it.value.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;

                return new LatticeValue(intConst(Float.floatToIntBits(lV)));
            }

            // 位运算
            case Shl it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                // 检测一下合法性，若移位数量小于 0 或大于 32 则交给运行时行为
                if (rV >= 0 && rV < 32) return new LatticeValue(intConst(lV << rV));
                return BOT;
            }
            // 逻辑右移
            case Shr it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                // 检测一下合法性，若移位数量小于 0 或大于 32 则交给运行时行为
                if (rV >= 0 && rV < 32) return new LatticeValue(intConst(lV >>> rV));
                return BOT;
            }
            // 算数右移
            case AShr it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value;
                var rV = ((IntConst) r.value).value;

                // 检测一下合法性，若移位数量小于 0 或大于 32 则交给运行时行为
                if (rV >= 0 && rV < 32) return new LatticeValue(intConst(lV >> rV));
                return BOT;
            }

            // 逻辑运算
            case And it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value != 0;
                var rV = ((IntConst) r.value).value != 0;

                return new LatticeValue(lV & rV ? iOne : iZero);
            }
            case Or it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value != 0;
                var rV = ((IntConst) r.value).value != 0;

                return new LatticeValue(lV | rV ? iOne : iZero);
            }
            case Xor it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((IntConst) l.value).value != 0;
                var rV = ((IntConst) r.value).value != 0;

                return new LatticeValue(lV ^ rV ? iOne : iZero);
            }
            case Not it -> {
                var r = getLattice(it.rhs.value);
                if (r == BOT) return BOT;
                if (r == TOP) return TOP;
                var rV = ((IntConst) r.value).value != 0;

                return new LatticeValue(rV ? iZero : iOne);
            }

            // intrinsic
            case FAbs it -> {
                var l = getLattice(it.lhs.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;

                return new LatticeValue(floatConst(Math.abs(lV)));
            }
            case FMax it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(Math.max(lV, rV)));
            }
            case FMin it -> {
                var l = getLattice(it.lhs.value);
                var r = getLattice(it.rhs.value);
                if (l == BOT || r == BOT) return BOT;
                if (l == TOP || r == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;
                var rV = ((FloatConst) r.value).value;

                return new LatticeValue(floatConst(Math.min(lV, rV)));
            }
            case FSqrt it -> {
                var l = getLattice(it.lhs.value);
                if (l == BOT) return BOT;
                if (l == TOP) return TOP;
                var lV = ((FloatConst) l.value).value;

                return new LatticeValue(floatConst((float) Math.sqrt(lV)));
            }
        }
    }

    private boolean visitTerminator(Instruction.Terminator term) {
        var modified = false;
        var block = term.getBlock();
        switch (term) {
            case Jmp jmp -> {
                var target = jmp.getTarget();
                cfgWL.add(pair(block, target));
            }
            case Br it -> {
                var cond = getLattice(it.getCondition());
                var tt = it.getTrueTarget();
                var ft = it.getFalseTarget();
                if (cond.isConst()) {
                    // 如果条件是常量，则只加一边
                    cfgWL.add(pair(block, !cond.value.equals(iZero) ? tt : ft));
                    modified = true;
                } else {
                    // 否则添加两个边
                    cfgWL.add(pair(block, tt));
                    cfgWL.add(pair(block, ft));
                }
            }
            default -> { }
        }

        return modified;
    }

    private void transform(Function function) {
        for (var block : function.blocks) {
            // 不处理死代码块，稍后 DCE 会移除它
            if (!executable.contains(block)) continue;

            for (var arg : block.args) {
                var lat = getLattice(arg);
                if (lat.isConst()) {
                    for (var term : CFGAnalysis.getPredTermsOf(block)) term.removeParam(block, arg);
                    arg.replaceAllUsesWith(lat.value);
                }
            }

            // 如果是常量，则直接替换
            for (var inst : block.instructions) {
                var lat = getLattice(inst);
                if (lat.isConst()) inst.replaceAllUsesWith(lat.value);
            }

            if (block.terminator instanceof Br br) {
                var cond = getLattice(br.getCondition());
                // 如果条件是常量，则将分支折叠成 jmp
                if (cond.isConst()) {
                    if (!cond.value.equals(iZero)) {
                        helper.changeBlock(block);
                        var target = br.getTrueTarget();
                        var jmp = helper.jmp(target);
                        br.trueParams.forEach(pair ->
                                jmp.putParam(target, pair.first().value, pair.second().value));
                        helper.changeBlockToNull();
                        br.dispose();
                    } else {
                        helper.changeBlock(block);
                        var target = br.getFalseTarget();
                        var jmp = helper.jmp(target);
                        br.falseParams.forEach(pair ->
                                jmp.putParam(target, pair.first().value, pair.second().value));
                        helper.changeBlockToNull();
                        br.dispose();
                    }
                }
            }
        }
    }

}
