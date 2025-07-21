package cn.edu.xjtu.sysy;

import java.util.Arrays;
import java.util.function.Function;

import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;

public final class PassGroup<T> implements ErrManaged {
    private final Pass<T, ?>[] passes;
    private final ErrManager errManager;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public PassGroup(ErrManager errManager, Function<ErrManager, Pass<T, ?>>... passSuppliers) {
        this.errManager = errManager;
        this.passes = (Pass<T, ?>[]) Arrays.stream(passSuppliers)
                .map(it -> it.apply(errManager))
                .toArray(Pass[]::new);
    }

    @Override
    public ErrManager getErrManager() {
        return errManager;
    }

    public void process(T obj) {
        for (var pass : passes) {
            pass.process(obj);
            if(pass.hasErr()) break;
        }
    }
}
