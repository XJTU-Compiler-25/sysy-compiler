package cn.edu.xjtu.sysy.symbol;

import cn.edu.xjtu.sysy.ast.node.ComptimeValue;

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
        public ComptimeValue comptimeValue;

        public Var(Kind kind, String name, Type type, boolean isConst) {
            this(kind, name, type, isConst, null);
        }

        public Var(Kind kind, String name, Type type, boolean isConst, ComptimeValue comptimeValue) {
            this.kind = kind;
            this.name = name;
            this.type = type;
            this.isConst = isConst;
            if(comptimeValue == null) throw new IllegalArgumentException("comptime value is null");
            this.comptimeValue = comptimeValue;
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
