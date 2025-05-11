package cn.edu.xjtu.sysy.riscv;

import java.util.List;

public abstract sealed class Global {

    public static final class Obj extends Global {
        public Label label;
        public List<Directives> directives;
        public List<Directives> value;

        public Obj(Label label, List<Directives> directives, List<Directives> value) {
            this.directives = directives;
            this.label = label;
            this.value = value;
        }
    }

    public static final class Func extends Global {
        public Label label;
        public List<Directives> directives;
        public List<Instr> instrs;
        public Directives.FuncSize size;

        public Func(Label label, List<Directives> directives, List<Instr> instrs, Directives.FuncSize size) {
            this.directives = directives;
            this.instrs = instrs;
            this.label = label;
            this.size = size;
        }
    }
}
