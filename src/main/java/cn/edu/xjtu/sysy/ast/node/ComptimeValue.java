package cn.edu.xjtu.sysy.ast.node;

import java.util.Arrays;

/**
 * 编译期求得的常量值
 */
public sealed abstract class ComptimeValue {
    public static final class Int extends ComptimeValue {
        public int value;

        public Int(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Int val) return val.value == value;
            else return false;
        }
    }

    public static final class Float extends ComptimeValue {
        public float value;

        public Float(float value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Float val) return val.value == value;
            else return false;
        }
    }

    public static final class Array extends ComptimeValue {
        public ComptimeValue[] values;

        public Array(ComptimeValue[] values) {
            this.values = values;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Array val) return Arrays.equals(values, val.values);
            else return false;
        }
    }
}
