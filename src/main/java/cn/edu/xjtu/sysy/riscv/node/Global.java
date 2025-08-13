package cn.edu.xjtu.sysy.riscv.node;

import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.sparseToDense;

import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public final class Global {
    public String name;
    public Type varType;
    public ImmediateValue initVal;

    public Global(String name, Type type, ImmediateValue initVal) {
        this.name = name;
        this.varType = type;
        this.initVal = initVal;
    }

    public String shortName() {
        return "$" + name;
    }

    /*
     * 
     *   .globl $a
        .bss
        .align 3
        .type $a, @object
        .size $a, 400
        $a:
        .zero 400
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(shortName()).append(":\n");
        switch (initVal) {
            case ImmediateValue.Undefined _, ImmediateValue.ZeroInit _-> {
                builder.append("    .zero ").append(Types.sizeOf(varType)).append("\n");
            }
            case ImmediateValue.IntConst it -> {
                builder.append("    .word ").append(it.value).append("\n");
            }
            case ImmediateValue.FloatConst it -> {
                builder.append("    .word ").append(Float.floatToRawIntBits(it.value)).append("\n");
            }
            case ImmediateValue.DenseArray it -> {
                int zeros = 0;
                for (var val : it.values) {
                    if (val instanceof ImmediateValue.ZeroInit) {
                        zeros += 4;
                        continue;
                    }
                    if (zeros > 0) {
                        builder.append("    .zero ").append(zeros).append("\n");
                    }
                    builder.append("    .word ").append(val).append("\n");
                }
            }
            case ImmediateValue.SparseArray it -> {
                int zeros = 0;
                for (var val : sparseToDense(it).values) {
                    if (val instanceof ImmediateValue.ZeroInit) {
                        zeros += 4;
                        continue;
                    }
                    if (zeros > 0) {
                        builder.append("    .zero ").append(zeros).append("\n");
                    }
                    builder.append("    .word ").append(val).append("\n");
                }
            }
        }
        return builder.toString();
    }
}
