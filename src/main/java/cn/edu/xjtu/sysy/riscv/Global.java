package cn.edu.xjtu.sysy.riscv;

import java.util.List;

import cn.edu.xjtu.sysy.riscv.node.Instr;

public abstract sealed class Global {

    public static final class Obj extends Global {
        public Label label;
        public List<Directive> directives;
        public List<Directive> value;

        public Obj(Label label, List<Directive> directives, List<Directive> value) {
            this.directives = directives;
            this.label = label;
            this.value = value;
        }
    }

    public static final class Func extends Global {
        public Label label;
        public List<Directive> directives;
        public List<Instr> instrs;
        public Directive.FuncSize size;

        public Func(Label label, List<Directive> directives, List<Instr> instrs, Directive.FuncSize size) {
            this.directives = directives;
            this.instrs = instrs;
            this.label = label;
            this.size = size;
        }
    }
}
