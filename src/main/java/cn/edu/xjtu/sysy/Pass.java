package cn.edu.xjtu.sysy;

import cn.edu.xjtu.sysy.error.ErrManaged;
import cn.edu.xjtu.sysy.error.ErrManager;

// T: to process, R: result
public abstract class Pass<T, R> implements ErrManaged {

    protected final Pipeline<T> pipeline;

    public Pass(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public ErrManager getErrManager() {
        return pipeline.errManager;
    }

    public abstract R process(T obj);


    protected final <R> R getResult(Class<? extends Pass<T, R>> passClass) {
        return pipeline.getResult(passClass);
    }

    protected final void invalidateResult(Class<? extends Pass<T, ?>> passClass) {
        pipeline.invalidateResult(passClass);
    }

}
