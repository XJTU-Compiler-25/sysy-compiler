package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;

public abstract class Pass<T, R> implements ErrManaged {

    private Pipeline<T> currentPipeline;
    private final ErrManager errManager;

    public Pass() {
        this(ErrManager.GLOBAL);
    }

    public Pass(ErrManager errManager) {
        this.errManager = errManager;
    }

    @Override
    public ErrManager getErrManager() {
        return errManager;
    }

    public abstract R process(T obj);

    public final void setCurrentPipeline(Pipeline<T> currentPipeline) {
        this.currentPipeline = currentPipeline;
    }

    public final <E, P extends Pass<T, E>> E getResult(Class<P> pass) {
        return currentPipeline.getResult(pass);
    }

    public final void invalidate(Class<? extends Pass<T, ?>> pass) {
        currentPipeline.invalidate(pass);
    }

    @SafeVarargs
    public final void invalidate(Class<? extends Pass<T, ?>>... passes) {
        currentPipeline.invalidate(passes);
    }

    public final void invalidateAll() {
        currentPipeline.invalidateAll();
    }

}
