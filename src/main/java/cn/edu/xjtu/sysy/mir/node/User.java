package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashSet;
import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public sealed abstract class User extends Value permits BlockArgument, Function, Instruction {

    public final HashSet<Use> used = new HashSet<>();
    /** 列表，处理需要有序访问Use的情况 */
    public final ArrayList<Use> usedList = new ArrayList<>();

    public User(Type type) {
        super(type);
    }

    public void addUsed(Use use) {
        if (used.add(use))
            usedList.add(use);
    }

    public void removeUsed(Use use) {
        if (used.remove(use))
            usedList.remove(use);
    }

    public <V extends Value> Use<V> use(V value) {
        var use = new Use<>(this, value);
        value.addUse(use);
        this.addUsed(use);
        return use;
    }

    public void dispose() {
        for (var use : used) use.value.removeUse(use);
        used.clear();
    }

}