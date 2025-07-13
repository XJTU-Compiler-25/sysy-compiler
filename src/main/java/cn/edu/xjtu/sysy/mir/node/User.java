package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashSet;

@SuppressWarnings("rawtypes")
public sealed abstract class User extends Value permits Function, Instruction {

    public final HashSet<Use> used = new HashSet<>();

    public User(Type type) {
        super(type);
    }

    public void addUsed(Use use) {
        used.add(use);
    }

    public void removeUsed(Use use) {
        used.remove(use);
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