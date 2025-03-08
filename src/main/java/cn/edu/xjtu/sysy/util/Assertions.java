package cn.edu.xjtu.sysy.util;

public final class Assertions {

    private Assertions() {}

    public static <T> T requires(boolean condition) {
        return requires(condition, "Assertion failed");
    }

    public static <T> T requires(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        return null;
    }

    public static <T> T unreachable() {
        throw new AssertionError("Unreachable code");
    }

}
