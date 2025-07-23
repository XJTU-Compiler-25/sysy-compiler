package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManager;

import java.util.HashMap;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class Pipeline<T> {

    public final ErrManager errManager;
    private final Pass<T, ?>[] passes;
    private final HashMap<Class, Object> cachedResults = new HashMap<>();

    @SafeVarargs
    public Pipeline(Function<Pipeline<T>, ? extends Pass<T, ?>>... passes) {
        this(ErrManager.GLOBAL, passes);
    }

    @SafeVarargs
    public Pipeline(ErrManager errManager, Function<Pipeline<T>, ? extends Pass<T, ?>>... passes) {
        this.errManager = errManager;
        var count = passes.length;
        this.passes = new Pass[count];
        for (int i = 0; i < count; i++) this.passes[i] = passes[i].apply(this);
    }

    public void process(T input) {
        for (var pass : passes) cachedResults.put(pass.getClass(), pass.process(input));
    }

    public <R> R getResult(Class<? extends Pass<T, R>> passClass) {
        return (R) cachedResults.get(passClass);
    }

    public void invalidateResult(Class<? extends Pass<T, ?>> passClass) {
        cachedResults.remove(passClass);
    }

}
