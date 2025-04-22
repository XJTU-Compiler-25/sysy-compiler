package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;

public abstract class Pass<T> implements ErrManaged {
    protected final ErrManager errManager;

    public Pass(ErrManager errManager) {
        this.errManager = errManager;
    }

    @Override
    public ErrManager getErrManager() {
        return errManager;
    }

    public abstract void process(T obj);
}
