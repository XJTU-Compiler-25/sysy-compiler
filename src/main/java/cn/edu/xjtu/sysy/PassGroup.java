package cn.edu.xjtu.sysy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PassGroup<T> {
    public List<Pass<T>> passes;

    public PassGroup(List<Pass<T>> passes) {
        this.passes = passes;
    }

    public PassGroup(Pass<T>... passes) {
        this.passes = new ArrayList<>(Arrays.asList(passes));
    }

    public void process(T obj) {
        for (var pass : passes) pass.process(obj);
    }
}
