package cn.edu.xjtu.sysy.util;

public final class Exceptions {

    private Exceptions() { }

    @SuppressWarnings("unchecked")
    public static RuntimeException sneakyThrows(Throwable t) {
        if (t == null) throw new NullPointerException("t");
        else return sneakyThrow0(t);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneakyThrow0(Throwable t) throws T {
        throw (T) t;
    }

}
