package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

/**
 * 立即的值
 */
public abstract sealed class Constant extends Value {
    Constant(Type type) {
        super(type);
    }

    public static final class IntConst extends Constant {
        public int value;

        IntConst(int value) {
            super(Types.Int);
            this.value = value;
        }

        @Override
        public String shallowToString() {
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
        public String shallowToString() {
            return Float.toString(value);
        }
    }

}
