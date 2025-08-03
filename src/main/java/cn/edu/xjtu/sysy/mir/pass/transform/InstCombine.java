package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.util.Worklist;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.*;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

/*
 * https://understanding-llvm-transformation-passes.readthedocs.io/en/latest/LLVM-transformation-passes/18-combine-redundant-instructions.html
 * -instcombine 做的 transformation 是将原有的 code 的 instructions 给 combine 成 fewer and simpler instructions，降低 code size 和 computation 的同时提升 runtime performance。 这个 pass 不修改任何的 program control-flow，所以 basic block 的跳转不会发生改变。 -instcombine focus 的主要是 numerical (specifically algebraic) instructions。 此外， -instcombine 对于 transformated code 有如下的 guarantees：
 * 如果一个 binary operator (including arithmetic and bitwise) 有一个 operand 是 constant，那么这个 constant 一定在右边 (只有 %y= add i32 %x, 3 一定没有 %y = add 3, i32 %32 )。
 * 如果多个 bitwise operator 有 constant operand，那么他们总会被 group 起来，而且顺序分别是：first shift, and or , and and , finally xor .
 * 对于所有 cmp 的 instructions (including < , > , ≤ , or ≥ ) 总是会被转换成 = or ≠ (在语法允许的情况下).
 * 对于所有 cmp 的 instructions，如果其 operand 是 boolean 的，则其会被替换成 logical operations。
 * 对于两个 operand 是相同的 variable (i.e. virtual registers) 的情况，这里可以用 constant operand 来优化。比如 add X X → mul X, 2 或者 shl X, 1.
 * 对于 multiplication/division 的对象是以 2 为底的指数，都可以转化成 shift operation（这个其实我在 CUDA 里其实经常用，比如 div i32 %x, 32 → shl i32 %3, 5 ）。
 * 等等。其实都是 runtime 到 compile-time 的优化。
 */
@SuppressWarnings("unchecked")
public final class InstCombine extends AbstractTransform {
    public InstCombine(Pipeline<Module> pipeline) { super(pipeline); }

    private final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(BasicBlock block) {
        var worklist = new Worklist<>(block.instructions);

        while (!worklist.isEmpty()) {
            var inst = worklist.poll();
            var result = ConstFold.foldConstant(inst);
            // 如果指令被改变了
            if (result != null) {
                for (var use : inst.usedBy) {
                    var user = use.user;
                    // 使用了该指令的所有指令也应该再次检查，但不用检查终结指令
                    if (user instanceof Instruction instr && !(instr instanceof Instruction.Terminator))
                        worklist.add(instr);
                }
                inst.replaceAllUsesWith(result);
            } else combineInst(inst); // 常量折叠没有成功，尝试一下模式识别
        }
    }


    private void combineInst(Instruction inst) {
        switch (inst) {
            case Call _, CallExternal _, Alloca _, Load _, Store _ -> { }
            case Terminator _ -> unreachable();
            // 数学运算
            case IAdd it -> {
                var lUse = it.lhs;
                var rUse = it.rhs;
                var lhs = lUse.value;
                var rhs = rUse.value;

                if (lhs instanceof IntConst lConst) {
                    var lVal = lConst.value;
                    if (lVal == 0) {
                        it.replaceAllUsesWith(rhs);
                        it.dispose();
                    } else if (rhs instanceof IAdd inner && inner.onlyOneUse()) {
                        var innerLhs = inner.lhs.value;
                        var innerRhs = inner.rhs.value;
                        if (innerLhs instanceof IntConst innerLConst) {
                            var innerLVal = innerLConst.value;
                            lUse.replaceValue(intConst(lVal + innerLVal));
                            rUse.replaceValue(innerRhs);
                        } else if (innerRhs instanceof IntConst innerRConst) {
                            var innerRVal = innerRConst.value;
                            lUse.replaceValue(intConst(lVal + innerRVal));
                            rUse.replaceValue(innerLhs);
                        }
                    }
                } else if (rhs instanceof IntConst rConst) {
                    var rVal = rConst.value;
                    if (rVal == 0) {
                        it.replaceAllUsesWith(lhs);
                        it.dispose();
                    } else if (lhs instanceof IAdd inner && inner.onlyOneUse()) {
                        var innerLhs = inner.lhs.value;
                        var innerRhs = inner.rhs.value;
                        if (innerLhs instanceof IntConst innerLConst) {
                            var innerLVal = innerLConst.value;
                            rUse.replaceValue(intConst(innerLVal + rVal));
                            lUse.replaceValue(innerRhs);
                        } else if (innerRhs instanceof IntConst innerRConst) {
                            var innerRVal = innerRConst.value;
                            rUse.replaceValue(intConst(innerRVal + rVal));
                            lUse.replaceValue(innerLhs);
                        }
                    }
                }
            }
            case GetElemPtr it -> {
                var base = it.basePtr.value;
                var indices = it.indices;
                var indexLen = indices.length;
                // 合并 GEP
                if (base instanceof GetElemPtr inner) {
                    var innerBase = inner.basePtr.value;
                    var innerIndices = inner.indices;
                    var innerIndexLen = innerIndices.length;
                    if (indices[0].value.equals(iZero)) {
                        // 外层的第一维是 0，则直接在外层的 indices 前面连接内层的 indices
                        it.basePtr.replaceValue(innerBase);
                        var newIndices = new Use[innerIndexLen + indexLen - 1];
                        for (int i = 0; i < innerIndexLen; i++) {
                            newIndices[i] = it.use(innerIndices[i].value);
                        }
                        indices[0].dispose();
                        if (indexLen > 1) System.arraycopy(indices, 1, newIndices, innerIndexLen, indexLen);
                        it.indices = newIndices;
                    } else if (indices[0].value instanceof IntConst firstIndex
                            && innerIndices[0].value instanceof IntConst innerLastIndex) {
                        // 外层的第一维不是 0，但内层的最后一维也是常数，则累加该维并连接其余 indices
                        it.basePtr.replaceValue(innerBase);
                        var newIndices = new Use[innerIndexLen + indexLen - 1];
                        for (int i = 0; i < innerIndexLen - 1; i++) {
                            newIndices[i] = it.use(innerIndices[i].value);
                        }
                        newIndices[innerIndexLen - 1] = it.use(intConst(firstIndex.value + innerLastIndex.value));
                        indices[0].dispose();
                        if (indexLen > 1) System.arraycopy(indices, 1, newIndices, innerIndexLen, indexLen);
                        it.indices = newIndices;
                    }
                }
            }
            default -> {}
        }
    }
}
