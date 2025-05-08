package cn.edu.xjtu.sysy.mir.node;

/**
 * 基本块结束指令
 */
public abstract sealed class Terminator {
    @Override
    public abstract String toString();

    /**
     * 带值返回 return
     */
    public static final class Ret extends Terminator {
        public Value value;

        Ret(Value value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ret " + value.shallowToString();
        }
    }

    /**
     * 无值返回 return void
     */
    public static final class RetV extends Terminator {
        @Override
        public String toString() {
            return "ret void";
        }
    }

    // 无条件跳转 jump
    public static final class Jmp extends Terminator {
        public BasicBlock target;

        Jmp(BasicBlock target) {
            this.target = target;
        }

        @Override
        public String toString() {
            return "jmp " + target.label;
        }
    }

    // 分支 branch
    public static final class Br extends Terminator {
        public Value condition;
        public BasicBlock trueTarget;
        public BasicBlock falseTarget;

        Br(Value condition, BasicBlock trueTarget, BasicBlock falseTarget) {
            this.condition = condition;
            this.trueTarget = trueTarget;
            this.falseTarget = falseTarget;
        }

        @Override
        public String toString() {
            return "br " + condition + ", " + trueTarget.label + ", " + falseTarget.label;
        }
    }
}
