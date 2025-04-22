package cn.edu.xjtu.sysy.symbol;

import java.util.List;

public abstract sealed class Symbol {
    public static final class Var extends Symbol {
        public enum Kind {
            GLOBAL, LOCAL
        }

        public Kind kind;
        public String name;
        public Type type;
        public boolean isConst;
        public Number comptimeValue;

        public Var(Kind kind, String name, Type type, boolean isConst) {
            this.kind = kind;
            this.name = name;
            this.type = type;
            this.isConst = isConst;
        }
    }

    public static final class Func extends Symbol {
        public String name;
        public Type retType;
        public List<Symbol.Var> params;

        public boolean isPure = false;

        public Func(String name, Type retType, List<Symbol.Var> params) {
            this.name = name;
            this.retType = retType;
            this.params = params;
        }
    }
}
