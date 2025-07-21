package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;

// T: to process, R: result
public abstract class Pass<T, R> implements ErrManaged {
    protected final ErrManager errManager;

    public Pass(ErrManager errManager) {
        this.errManager = errManager;
    }

    public Pass() {
        this(ErrManager.GLOBAL);
    }

    @Override
    public ErrManager getErrManager() {
        return errManager;
    }

    public abstract R process(T obj);
}
