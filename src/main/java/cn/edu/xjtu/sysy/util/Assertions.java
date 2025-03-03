package cn.edu.xjtu.sysy.util;

public final class Assertions {

    private Assertions() { }

    public static void check(boolean condition) {
        check(condition, "Assertion failed");
    }

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

}
