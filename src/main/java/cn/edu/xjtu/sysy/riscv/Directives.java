package cn.edu.xjtu.sysy.riscv;

public sealed interface Directives {
    public record Equiv(String name, int value) implements Directives {
        @Override
        public String toString() {
            return String.format(".equiv %s, %d", name, value);
        }
    }

    public record Global(Label label) implements Directives {
        @Override
        public String toString() {
            return String.format(".globl %s", label);
        }
    }

    public record Type(Label label, SymType type) implements Directives {
        public enum SymType {
            Object("@object"),
            Function("@function");

            protected final String type;

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

    public record VarSize(Label label, int size) implements Directives {
        @Override
        public String toString() {
            return String.format(".size %s, %d", label, size);
        }
    }

    public record FuncSize(Label label) implements Directives {
        @Override
        public String toString() {
            return String.format(".size %s, .-%s", label, label);
        }
    }

    public record Word(int val) implements Directives {
        @Override
        public String toString() {
            return String.format(".word %d", val);
        }
    }

    public record WordAddr(Label label) implements Directives {
        @Override
        public String toString() {
            return String.format(".word %s", label);
        }
    }

    public record Zero(int size) implements Directives {
        @Override
        public String toString() {
            return String.format(".zero %d", size);
        }
    }

    public record Bss() implements Directives {
        @Override
        public String toString() {
            return ".bss";
        }
    }

    public record Data() implements Directives {
        @Override
        public String toString() {
            return ".data";
        }
    }

    public record Text() implements Directives {
        @Override
        public String toString() {
            return ".text";
        }
    }

    public record Align(int pow) implements Directives {
        @Override
        public String toString() {
            return String.format(".align %d", pow);
        }
    }

    public record Set(Label label, int offset) implements Directives {
        @Override
        public String toString() {
            return String.format(".set %s, . %s %d", label, offset >= 0 ? "+" : "-", Math.abs(offset));
        }
    }
}
