package cn.edu.xjtu.sysy.util;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;

// 自带去重的 worklist，防止多次处理
public final class Worklist<E> {
    private final ArrayDeque<E> queue;
    private final HashSet<E> set;

    public Worklist() {
        queue = new ArrayDeque<>();
        set = new HashSet<>();
    }

    public Worklist(Collection<? extends E> c) {
        queue = new ArrayDeque<>(c);
        set = new HashSet<>(c);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public E poll() {
        var e = queue.removeFirst();
        set.remove(e);
        return e;
    }

    public E peek() {
        return queue.peekFirst();
    }

    public void add(E e) {
        if (set.add(e)) queue.addLast(e);
    }

    public void addFirst(E e) {
        if (set.add(e)) queue.addFirst(e);
    }

    public void clear() {
        queue.clear();
        set.clear();
    }

    public void addAll(Collection<? extends E> needToSpill) {
        for (var e : needToSpill) if (set.add(e)) queue.addLast(e);
    }
}
