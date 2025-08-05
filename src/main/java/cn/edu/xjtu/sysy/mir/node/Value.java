package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashSet;
import java.util.function.Predicate;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract sealed class Value permits BasicBlock, BlockArgument, Function, GlobalVar, ImmediateValue, User {

    private static int counter = 0;
    public final int id = counter++;

    public Type type;
    public final HashSet<Use> usedBy = new HashSet<>();

    public Value(Type type) {
        this.type = type;
    }

    /**
     * 浅的字符串表示，如指令只返回其 label，只有数字字面量完全返回其表示
     */
    public abstract String shortName();

    @Override
    public String toString() {
        return shortName();
    }

    public final void addUse(Use use) {
        usedBy.add(use);
    }

    public final void removeUse(Use use) {
        usedBy.remove(use);
    }

    public final void replaceAllUsesWith(Value newValue) {
        for (var use : (HashSet<Use>) usedBy.clone()) {
            use.replaceValue(newValue);
        }
    }

    public final void replaceAllUsesWithIf(Value newValue, Predicate<Use> predicate) {
        for (var use : (HashSet<Use>) usedBy.clone()) {
            if (predicate.test(use)) use.replaceValue(newValue);
        }
    }

    public final boolean notUsed() {
        return usedBy.isEmpty();
    }

    public final boolean onlyOneUse() {
        return usedBy.size() == 1;
    }

}
