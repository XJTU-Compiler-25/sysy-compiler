package cn.edu.xjtu.sysy.ast.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.xjtu.sysy.symbol.Type;

/**
 * 编译期求得的常量值
 */
public sealed abstract class ComptimeValue {
    public abstract ComptimeValue toType(Type toType);

    public static final class Int extends ComptimeValue {
        public int value;

        public Int(int value) {
            this.value = value;
        }

        @Override
        public ComptimeValue toType(Type toType) {
            if (toType.equals(Type.Primitive.FLOAT)) return new Float(value);
            else if (toType.equals(Type.Primitive.INT)) return this;
            else throw new RuntimeException("Unsupported type conversion");
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Int val) return val.value == value;
            else return false;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    public static final class Float extends ComptimeValue {
        public float value;

        public Float(float value) {
            this.value = value;
        }

        @Override
        public ComptimeValue toType(Type toType) {
            if (toType.equals(Type.Primitive.INT)) return new Int((int) value);
            else if (toType.equals(Type.Primitive.FLOAT)) return this;
            else throw new RuntimeException("Unsupported type conversion");
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Float val) return val.value == value;
            else return false;
        }

        @Override
        public String toString() {
            return java.lang.Float.toString(value);
        }
    }

    public static final class Array extends ComptimeValue {
        public ComptimeValue[] values;

        public Array(ComptimeValue[] values) {
            this.values = values;
        }

        public List<ComptimeValue> flatValue() {
            List<ComptimeValue> flatten = new ArrayList<>();
            for (ComptimeValue value : values) {
                if (value instanceof Array it) {
                    flatten.addAll(it.flatValue());
                } else {
                    flatten.add(value);
                }
            } 
            return flatten;
        }

        @Override
        public ComptimeValue toType(Type toType) {
            throw new RuntimeException("Unsupported type conversion");
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Array val) return Arrays.equals(values, val.values);
            else return false;
        }

        @Override
        public String toString() {
            return Arrays.toString(values);
        }
    }
}
