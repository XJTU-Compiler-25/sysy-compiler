package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;

// T: to process, R: result
@SuppressWarnings("unchecked")
public abstract sealed class Pass<T, R> implements ErrManaged {

    public abstract non-sealed static class Analysis<T, R> extends Pass<T, R> { }

    public abstract non-sealed static class Transformer<T> extends Pass<T, Void> { }

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

}
