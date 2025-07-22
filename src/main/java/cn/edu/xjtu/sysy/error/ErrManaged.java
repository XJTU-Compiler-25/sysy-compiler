package cn.edu.xjtu.sysy.error;

public interface ErrManaged {
    ErrManager getErrManager();

    default void err(String msg) {
        getErrManager().err(msg);
    }

    default void err(Err err) {
        getErrManager().err(err);
    }

    default boolean hasErr() {
        return getErrManager().hasErr();
    }

    default Err[] getErrs() {
        return getErrManager().getErrs();
    }
}
