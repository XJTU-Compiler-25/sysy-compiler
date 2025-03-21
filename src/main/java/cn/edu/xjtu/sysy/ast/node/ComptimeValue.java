package cn.edu.xjtu.sysy.ast.node;

/**
 * 编译期求得的常量值
 */
public sealed abstract class ComptimeValue {
    public static final class Int extends ComptimeValue {
        public int value;

        public Int(int value) {
            this.value = value;
        }
    }

    public static final class Float extends ComptimeValue {
        public float value;

        public Float(float value) {
            this.value = value;
        }
    }

    public static final class Array extends ComptimeValue {
        public ComptimeValue[] values;

        public Array(ComptimeValue[] values) {
            this.values = values;
        }
    }
}
