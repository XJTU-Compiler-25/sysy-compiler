package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.InstructionHelper;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;

import java.util.ArrayList;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.intConst;
import static cn.edu.xjtu.sysy.util.MathUtils.isPowerOf2;

// 整数常数除法
public final class IDivReduce extends ModulePass<Void> {

    private static final InstructionHelper helper = new InstructionHelper();

    @Override
    public void visit(BasicBlock block) {
        helper.changeBlock(block);
        var reducible = new ArrayList<Instruction.IDiv>();
        for (var inst : block.instructions) {
            if (inst instanceof Instruction.IDiv idiv && idiv.getRhs() instanceof ImmediateValue.IntConst)
                reducible.add(idiv);
        }

        // 只做 power of 2 改成移位，任意常数要用到 int64 而且收益不大
        for (var inst : reducible) {
            var dividend = inst.getLhs();
            var divisor = ((ImmediateValue.IntConst) inst.getRhs()).value;

            // 不处理除数为 0 的情况
            if (divisor == 0) continue;

            if (divisor == 1) {
                inst.replaceAllUsesWith(dividend);
                continue;
            }

            helper.moveToBefore(inst);

            if (divisor == -1) {
                inst.replaceAllUsesWith(helper.insertNeg(dividend));
                continue;
            }

            var negate = false;
            if (divisor < 0) {
                negate = true;
                divisor = -divisor;
            }

            // 其他常数，只处理 power of 2 的除法
            if (!isPowerOf2(divisor)) continue;
            // 到这里，divisor 一定是 2^k (k > 0)

            var k = Integer.numberOfTrailingZeros(divisor);

            // 实现公式: (x + (x >> 31 & (d - 1))) >> k
            var t1 = helper.insertAshr(dividend, intConst(31));
            var t2 = helper.insertAnd(t1, intConst(divisor - 1));
            var t3 = helper.insertAdd(dividend, t2);
            var result = helper.insertAshr(t3, intConst(k));

            if (negate) result = helper.insertNeg(result);

            inst.replaceAllUsesWith(result);
        }
        // 没用的 idiv 会被 DCE 删除的
        helper.changeBlockToNull();
    }

}
