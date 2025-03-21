package cn.edu.xjtu.sysy.util;

public final class Arithmetics {

    private Arithmetics() { }

    public boolean toBoolean(int val) {
        return val != 0;
    }

    public boolean toBoolean(float val) {
        return val != 0f;
    }

    public int toInt(boolean val) {
        return val ? 1 : 0;
    }

}
