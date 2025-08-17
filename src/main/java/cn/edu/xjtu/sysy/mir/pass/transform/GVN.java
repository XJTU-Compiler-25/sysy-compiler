package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.mir.pass.analysis.DomInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.DominanceAnalysis;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfo;
import cn.edu.xjtu.sysy.mir.pass.analysis.FuncInfoAnalysis;
import cn.edu.xjtu.sysy.symbol.Type;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

// Global Value Numbering 全局值编号，用于冗余消除，运行之后需要接着运行 GCM 来修复支配性
public final class GVN extends ModulePass<Void> {

    private DomInfo domInfo;
    private FuncInfo funcInfo;

    @Override
    public void visit(Module module) {
        domInfo = getResult(DominanceAnalysis.class);
        funcInfo = getResult(FuncInfoAnalysis.class);

        super.visit(module);
    }

    private record GVNKey(
            Class<? extends Instruction> kind,
            Type type,
            int[] operands
    ) {
        private static GVNKey of(Instruction inst) {
            var operands = inst.used.stream().mapToInt(it -> it.value.id).toArray();
            // 如果可交换，则 sort operand
            switch (inst) {
                case IAdd _, FAdd _, IMul _, FMul _,
                     And _, Or _, Xor _,
                     IEq _, INe _, FMin _, FMax _ -> Arrays.sort(operands);
                default -> {}
            }
            return new GVNKey(inst.getClass(), inst.type, operands);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GVNKey(var kind1, var type1, var operands1))) return false;
            return kind == kind1 && type.equals(type1) && Arrays.equals(operands, operands1);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(kind, type);
            result = 31 * result + Arrays.hashCode(operands);
            return result;
        }
    }

    private HashMap<GVNKey, Value> table;
    @Override
    public void visit(Function function) {
        // gvn 迭代 3 次以确保消除不同遍历顺序的影响
        for (int i = 0; i < 3; i++) {
            table = new HashMap<>();
            visit(function.entry);
        }
    }

    @Override
    public void visit(BasicBlock block) {
        var oldTable = new HashMap<>(table);

        for (var inst : block.instructions) {
            // 不产生值的指令不参与消除
            if (inst.notProducingValue()) continue;
            switch (inst) {
                case Alloca _, Load _, Store _, Dummy _, Imm _, FMulAdd _,
                     ILi _, FLi _, IMv _, FMv _, ICpy _, FCpy _, CallExternal _ -> { continue; }
                case Terminator _ -> unreachable();
                case Call it -> {
                    // 不纯函数的结果不共享
                    if (!funcInfo.isPure(it.getCallee())) continue;
                }
                default -> { }
            }

            var key = GVNKey.of(inst);
            var existing = table.get(key);

            if (existing != null) inst.replaceAllUsesWith(existing);
            else table.put(key, inst);
        }

        for (var child : domInfo.getDomChildren(block)) visit(child);

        table = oldTable; // 恢复之前的表
    }

}
