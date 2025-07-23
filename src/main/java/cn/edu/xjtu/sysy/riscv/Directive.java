package cn.edu.xjtu.sysy.riscv;

public sealed interface Directive {
    record Equiv(String name, int value) implements Directive {
        @Override
        public String toString() {
            return String.format(".equiv %s, %d", name, value);
        }
    }

    record Global(Label label) implements Directive {
        @Override
        public String toString() {
            return String.format(".globl %s", label);
        }
    }

    record Type(Label label, SymType type) implements Directive {
        public enum SymType {
            Object("@object"),
            Function("@function");

            private final String type;

            SymType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return type;
            }
        }
        @Override
        public String toString() {
            return String.format(".type %s, %s", label, type);
        }
    }

    record VarSize(Label label, int size) implements Directive {
        @Override
        public String toString() {
            return String.format(".size %s, %d", label, size);
        }
    }

    record FuncSize(Label label) implements Directive {
        @Override
        public String toString() {
            return String.format(".size %s, .-%s", label, label);
        }
    }

    record Word(int val) implements Directive {
        @Override
        public String toString() {
            return String.format(".word %d", val);
        }
    }

    record WordAddr(Label label) implements Directive {
        @Override
        public String toString() {
            return String.format(".word %s", label);
        }
    }

    record Zero(int size) implements Directive {
        @Override
        public String toString() {
            return String.format(".zero %d", size);
        }
    }

    record Bss() implements Directive {
        @Override
        public String toString() {
            return ".bss";
        }
    }

    record Data() implements Directive {
        @Override
        public String toString() {
            return ".data";
        }
    }

    record Text() implements Directive {
        @Override
        public String toString() {
            return ".text";
        }
    }

    record Align(int pow) implements Directive {
        @Override
        public String toString() {
            return String.format(".align %d", pow);
        }
    }

    record Set(Label label, int offset) implements Directive {
        @Override
        public String toString() {
            return String.format(".set %s, . %s %d", label, offset >= 0 ? "+" : "-", Math.abs(offset));
        }
    }
}
