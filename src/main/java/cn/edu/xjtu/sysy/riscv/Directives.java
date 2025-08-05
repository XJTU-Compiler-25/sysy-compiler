package cn.edu.xjtu.sysy.riscv;

public sealed interface Directives {
    record Equiv(String name, int value) implements Directives {
        @Override
        public String toString() {
            return String.format(".equiv %s, %d", name, value);
        }
    }

    record Global(Label label) implements Directives {
        @Override
        public String toString() {
            return String.format(".globl %s", label);
        }
    }

    record Type(Label label, SymType type) implements Directives {
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

    record VarSize(Label label, int size) implements Directives {
        @Override
        public String toString() {
            return String.format(".size %s, %d", label, size);
        }
    }

    record FuncSize(Label label) implements Directives {
        @Override
        public String toString() {
            return String.format(".size %s, .-%s", label, label);
        }
    }

    record Word(int val) implements Directives {
        @Override
        public String toString() {
            return String.format(".word %d", val);
        }
    }

    record WordAddr(Label label) implements Directives {
        @Override
        public String toString() {
            return String.format(".word %s", label);
        }
    }

    record Zero(int size) implements Directives {
        @Override
        public String toString() {
            return String.format(".zero %d", size);
        }
    }

    record Bss() implements Directives {
        @Override
        public String toString() {
            return ".bss";
        }
    }

    record Data() implements Directives {
        @Override
        public String toString() {
            return ".data";
        }
    }

    record Text() implements Directives {
        @Override
        public String toString() {
            return ".text";
        }
    }

    record Align(int pow) implements Directives {
        @Override
        public String toString() {
            return String.format(".align %d", pow);
        }
    }

    record Set(Label label, int offset) implements Directives {
        @Override
        public String toString() {
            return String.format(".set %s, . %s %d", label, offset >= 0 ? "+" : "-", Math.abs(offset));
        }
    }
}
