package cn.edu.xjtu.sysy.mir.node;

public final class Constants {

    private Constants() {}

    public static final Constant.IntConst iZero = new Constant.IntConst(0);
    public static final Constant.IntConst iOne = new Constant.IntConst(1);
    public static final Constant.IntConst iNegOne = new Constant.IntConst(-1);

    public static final Constant.FloatConst fZero = new Constant.FloatConst(0.0f);
    public static final Constant.FloatConst fOne = new Constant.FloatConst(1.0f);

    public static final Constant.IntConst iTrue = iOne;
    public static final Constant.IntConst iFalse = iZero;

    public static Constant.IntConst intConst(int value) {
        if (value == 0) return iZero;
        else if (value == 1) return iOne;
        else if (value == -1) return iNegOne;
        else return new Constant.IntConst(value);
    }

    public static Constant.FloatConst floatConst(float value) {
        if (value == 0.0f) return fZero;
        else if (value == 1.0f) return fOne;
        else return new Constant.FloatConst(value);
    }

    public static Constant.ZeroInit ZeroInit = new Constant.ZeroInit();

}
