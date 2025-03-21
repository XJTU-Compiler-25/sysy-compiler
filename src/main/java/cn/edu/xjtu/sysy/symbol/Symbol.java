package cn.edu.xjtu.sysy.symbol;

import java.util.List;

public abstract sealed class Symbol {
    public static final class Var extends Symbol {
        public enum Kind {
            GLOBAL, LOCAL;
        }

        public Kind kind;
        public String name;
        public Type type;
        public boolean isConst;
        public Number constValue;

        public Var(Kind kind, String name, Type type) {
            this(kind, name, type, false, null);
        }

        public Var(Kind kind, String name, Type type, boolean isConst) {
            this(kind, name, type, isConst, null);
        }

        public Var(Kind kind, String name, Type type, boolean isConst, Number constValue) {
            this.kind = kind;
            this.name = name;
            this.type = type;
            this.isConst = isConst;
            this.constValue = constValue;
        }
    }

    public static final class Func extends Symbol {
        public String name;
        public Type retType;
        public List<Symbol.Var> params;

        public Func(String name, Type retType, List<Symbol.Var> params) {
            this.name = name;
            this.retType = retType;
            this.params = params;
        }
    }
}
