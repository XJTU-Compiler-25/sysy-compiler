package cn.edu.xjtu.sysy.mir.node;

public final class Constants {

    private Constants() {}

    public static final Constant.IntConst INT_ZERO = new Constant.IntConst(0);
    public static final Constant.IntConst INT_ONE = new Constant.IntConst(1);
    public static final Constant.IntConst INT_NEG_ONE = new Constant.IntConst(-1);

    public static final Constant.FloatConst FLOAT_ZERO = new Constant.FloatConst(0.0f);
    public static final Constant.FloatConst FLOAT_ONE = new Constant.FloatConst(1.0f);

    public static Constant.IntConst intConst(int value) {
        if (value == 0) return INT_ZERO;
        else if (value == 1) return INT_ONE;
        else if (value == -1) return INT_NEG_ONE;
        else return new Constant.IntConst(value);
    }

    public static Constant.FloatConst floatConst(float value) {
        if (value == 0.0f) return FLOAT_ZERO;
        else if (value == 1.0f) return FLOAT_ONE;
        else return new Constant.FloatConst(value);
    }

}
