package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.util.Assertions;

import java.util.HashSet;
import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public sealed abstract class User extends Value permits Instruction {

    public final HashSet<Use> used = new HashSet<>();
    /** 列表，处理需要有序访问Use的情况 */
    public final ArrayList<Use> usedList = new ArrayList<>();

    public User(Type type) {
        super(type);
    }

    public void addUsed(Use use) {
        used.add(use);
        usedList.add(use);
    }

    public void removeUsed(Use use) {
        int idx = usedList.lastIndexOf(use);
        if (idx == -1) {
            used.remove(use);
            return;
        }
        usedList.remove(idx);
        if (!usedList.contains(use)) used.remove(use);
    }

    public <V extends Value> Use<V> use(V value) {
        var use = new Use<>(this, value);
        value.addUse(use);
        this.addUsed(use);
        return use;
    }

    // 清理 use 关系
    public void dispose() {
        for (var use : used) use.value.removeUse(use);
        used.clear();
    }

}