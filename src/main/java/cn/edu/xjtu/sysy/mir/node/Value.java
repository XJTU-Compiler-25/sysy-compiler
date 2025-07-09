package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;

import java.util.HashSet;
import java.util.function.Predicate;

@SuppressWarnings("rawtypes")
public abstract sealed class Value permits ImmediateValue, User, Var {

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
        usedBy.forEach(use -> {
            use.value.removeUse(use);
            use.value = newValue;
            newValue.addUse(use);
        });
    }

    public final void replaceAllUsesWithIf(Value newValue, Predicate<Use> predicate) {
        usedBy.stream().filter(predicate).forEach(use -> {
            use.value.removeUse(use);
            use.value = newValue;
            newValue.addUse(use);
        });
    }

}
