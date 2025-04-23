package cn.edu.xjtu.sysy.error;

import java.util.ArrayList;

public final class ErrManager {
    public static final ErrManager GLOBAL = new ErrManager();

    public ArrayList<Err> errs = new ArrayList<>();

    public void err(String msg) {
        err(new Err(msg));
    }

    public void err(Err err) {
        errs.add(err);
    }

    public boolean hasErr() {
        return !errs.isEmpty();
    }

    public Err[] getErrs() {
        return errs.toArray(Err[]::new);
    }

    public void clear() {
        errs.clear();
    }

    public void printErrs() {
        errs.forEach(System.err::println);
    }

    public void terminate(String msg) {
        err(msg);
        throw new RuntimeException("Terminated by:" + msg);
    }

}
