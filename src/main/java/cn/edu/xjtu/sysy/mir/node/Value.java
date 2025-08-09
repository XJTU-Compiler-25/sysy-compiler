package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import cn.edu.xjtu.sysy.riscv.ValuePosition;
import cn.edu.xjtu.sysy.symbol.Type;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract sealed class Value permits BasicBlock, BlockArgument, Function, GlobalVar, ImmediateValue, User {

    private static int counter = 0;
    public final int id = counter++;

    public Type type;
    public final ArrayList<Use> usedBy = new ArrayList<>();

    public ValuePosition constraint = null;

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
        for (var use : (ArrayList<Use>) usedBy.clone()) {
            use.replaceValue(newValue);
        }
    }

    public final void replaceAllUsesWithIf(Value newValue, Predicate<Use> predicate) {
        for (var use : (ArrayList<Use>) usedBy.clone()) {
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
