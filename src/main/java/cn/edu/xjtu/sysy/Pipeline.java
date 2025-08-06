package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManager;

import java.util.function.Supplier;

@SuppressWarnings({"unchecked"})
public final class Pipeline<T> {

    public final ErrManager errManager;
    private final Pass.Transformer<T>[] passes;

    @SafeVarargs
    public Pipeline(Supplier<? extends Pass.Transformer<T>>... passes) {
        this(ErrManager.GLOBAL, passes);
    }

    @SafeVarargs
    public Pipeline(ErrManager errManager, Supplier<? extends Pass.Transformer<T>>... passes) {
        this.errManager = errManager;
        var count = passes.length;
        this.passes = new Pass.Transformer[count];
        for (int i = 0; i < count; i++) this.passes[i] = passes[i].get();
    }

    public void process(T input) {
        for (var pass : passes) {
            pass.process(input);
        }
    }

}
