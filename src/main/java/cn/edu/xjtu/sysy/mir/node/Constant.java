package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.HashMap;

/**
 * 立即的值
 */
public abstract sealed class Constant extends Value {
    Constant(Type type) {
        super(type);
    }

    @Override
    public final String toString() {
        if (stringRepresent == null) stringRepresent = buildStringRepresent();
        return stringRepresent;
    }

    @Override
    public final String shallowToString() {
        if (stringRepresent == null) stringRepresent = buildStringRepresent();
        return stringRepresent;
    }

    private String stringRepresent;
    abstract String buildStringRepresent();

    public static final class IntConst extends Constant {
        public int value;

        IntConst(int value) {
            super(Types.Int);
            this.value = value;
        }

        @Override
        String buildStringRepresent() {
            return Integer.toString(value);
        }
    }

    public static final class FloatConst extends Constant {
        public float value;

        FloatConst(float value) {
            super(Types.Int);
            this.value = value;
        }

        @Override
        String buildStringRepresent() {
            return Float.toString(value);
        }
    }

    public static final class ZeroInit extends Constant {
        ZeroInit() {
            super(Types.Void);
        }

        @Override
        String buildStringRepresent() {
            return "zeroinit";
        }
    }

    public static final class DenseArrayInit extends Constant {
        public Constant[] values;

        DenseArrayInit(Constant[] values, Type type) {
            super(Types.ptrOf(type));
            this.values = values;
        }

        @Override
        String buildStringRepresent() {
            var sb = new StringBuilder();
            sb.append("[ ");
            for (int i = 0, maxLen = values.length; i < maxLen; i++) {
                sb.append(values[i].shallowToString());
                if (i != maxLen - 1) sb.append(", ");
            }
            sb.append(" ]");
            return sb.toString();
        }
    }

    public static final class SparseArrayInit extends Constant {
        public HashMap<Integer, Constant> values = new HashMap<>();

        SparseArrayInit(Constant[] values, int[] indexes, Type type) {
            super(Types.ptrOf(type));
            for (int i = 0, maxLen = indexes.length; i < maxLen; i++) this.values.put(indexes[i], values[i]);
        }

        @Override
        String buildStringRepresent() {
            var sb = new StringBuilder();
            sb.append("[ ");
            var entries = values.entrySet().stream().toList();
            for (int i = 0, maxLen = entries.size(); i < maxLen; i++) {
                var entry = entries.get(i);
                sb.append(entry.getKey().intValue())
                        .append(" : ").append(entry.getValue().shallowToString());
                if (i != maxLen - 1) sb.append(", ");
            }
            sb.append(" ]");
            return sb.toString();
        }
    }

}
