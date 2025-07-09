package cn.edu.xjtu.sysy.mir.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import cn.edu.xjtu.sysy.symbol.Type;

public abstract sealed class Value permits BlockArgument, ImmediateValue, Instruction, Var {

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

    public void addUse(Use use) {
        usedBy.add(use);
        if (use.user instanceof Instruction it) {
            it.uses.add(this);
        }
    }

    public final void removeUse(Use use) {
        usedBy.remove(use);
        if (use.user instanceof Instruction it) {
            it.uses.remove(this);
        }
    }

    public final void replaceAllUsesWith(Value newValue) {
        new ArrayList<>(usedBy).forEach(use -> {
            var value = use.value;
            use.value = newValue;
            value.removeUse(use);
            newValue.addUse(use);
        });
    }

    public final void replaceAllUsesWithIf(Value newValue, Predicate<Use> predicate) {
        usedBy.stream().filter(predicate).forEach(use -> {
            var value = use.value;
            use.value = newValue;
            value.removeUse(use);
            newValue.addUse(use);
        });
    }

}
