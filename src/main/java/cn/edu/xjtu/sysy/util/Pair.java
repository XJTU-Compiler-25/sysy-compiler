package cn.edu.xjtu.sysy.util;

public record Pair<T1, T2>(T1 first, T2 second) {

    public static <E1, E2> Pair<E1, E2> pair(E1 first, E2 second) {
        return new Pair<>(first, second);
    }

}
