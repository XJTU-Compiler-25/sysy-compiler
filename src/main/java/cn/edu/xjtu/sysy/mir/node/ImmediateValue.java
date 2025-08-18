package cn.edu.xjtu.sysy.mir.node;

import java.util.Arrays;
import java.util.HashMap;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

/**
 * 立即的值
 */
public abstract sealed class ImmediateValue extends Value {
    ImmediateValue(Type type) {
        super(type);
    }

    @Override
    public final String toString() {
        if (stringRepresent == null) stringRepresent = buildStringRepresent();
        return stringRepresent;
    }

    @Override
    public final String shortName() {
        if (stringRepresent == null) stringRepresent = buildStringRepresent();
        return stringRepresent;
    }

    private String stringRepresent;
    abstract String buildStringRepresent();

    public static final class IntConst extends ImmediateValue {
        public int value;

        IntConst(int value) {
            super(Types.Int);
            this.value = value;
        }

        @Override
        String buildStringRepresent() {
            return Integer.toString(value);
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof IntConst iConst && value == iConst.value;
        }
    }

    public static final class FloatConst extends ImmediateValue {
        public float value;

        FloatConst(float value) {
            super(Types.Float);
            this.value = value;
        }

        @Override
        String buildStringRepresent() {
            return Float.toString(value);
        }
        
        @Override
        public int hashCode() {
            return Float.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof FloatConst fConst && value == fConst.value;
        }
    }

    public static final class Undefined extends ImmediateValue {
        Undefined() {
            super(Types.Void);
        }

        @Override
        String buildStringRepresent() {
            return "undef";
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    public static final class ZeroInit extends ImmediateValue {
        ZeroInit() {
            super(Types.Void);
        }

        @Override
        String buildStringRepresent() {
            return "zeroinit";
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ZeroInit;
        }
    }

    public static final class DenseArray extends ImmediateValue {
        public Value[] values;

        DenseArray(Type type, Value[] values) {
            super(Types.ptrOf(type));
            this.values = Arrays.copyOf(values, values.length);
        }

        @Override
        String buildStringRepresent() {
            var sb = new StringBuilder();
            sb.append("[ ");
            for (int i = 0, maxLen = values.length; i < maxLen; i++) {
                var value = values[i];
                sb.append(value != null ? value.shortName() : "null");
                if (i != maxLen - 1) sb.append(", ");
            }
            sb.append(" ]");
            return sb.toString();
        }
    }

    public static final class SparseArray extends ImmediateValue {
        public HashMap<Integer, Value> values = new HashMap<>();
        public int size;

        SparseArray(Type type, int capacity, int[] indexes, Value[] values) {
            super(Types.ptrOf(type));
            this.size = capacity;
            for (int i = 0, maxLen = indexes.length; i < maxLen; i++) this.values.put(indexes[i], values[i]);
        }

        @Override
        String buildStringRepresent() {
            var sb = new StringBuilder();
            sb.append("[ ");
            var entries = values.entrySet().stream().toList();
            for (int i = 0, maxLen = entries.size(); i < maxLen; i++) {
                var entry = entries.get(i);
                sb.append('[').append(entry.getKey().intValue())
                        .append("] = ").append(entry.getValue().shortName());
                if (i != maxLen - 1) sb.append(", ");
            }
            sb.append(" ]");
            return sb.toString();
        }
    }

}
