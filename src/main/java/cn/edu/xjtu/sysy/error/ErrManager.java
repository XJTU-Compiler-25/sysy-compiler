package cn.edu.xjtu.sysy.error;

import java.util.ArrayList;
import java.util.List;

public final class ErrManager {
    public List<Err> errs = new ArrayList<>();

    public void add(String msg) {
        add(new Err(msg));
    }

    public void add(Err err) {
        errs.add(err);
    }

    public boolean hasErr() {
        return !errs.isEmpty();
    }
}
