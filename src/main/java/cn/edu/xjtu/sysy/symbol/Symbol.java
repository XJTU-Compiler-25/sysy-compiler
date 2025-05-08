package cn.edu.xjtu.sysy.symbol;

import java.util.List;

import cn.edu.xjtu.sysy.riscv.Label;

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

        public Label label;
        public int index = -1;
        public boolean declared = false;

        public Var(Kind kind, String name, Type type, boolean isConst) {
            this.kind = kind;
            this.name = name;
            this.type = type;
            this.isConst = isConst;
            this.label = new Label(String.format("$%s", name));
        }

        public Var(Kind kind, String name, Type type, boolean isConst, int index) {
            this.kind = kind;
            this.name = name;
            this.type = type;
            this.isConst = isConst;
            this.index = index;
            this.label = new Label(String.format("$%s", name));
        }
    }

    public static final class Func extends Symbol {
        public String name;
        public Type retType;
        public List<Symbol.Var> params;

        public boolean isPure = false;

        public Label label;
        public int size = -1;
        public int paramSize;
        public int argSize = 0;
        public boolean raSave = false;

        public Func(String name, Type retType, List<Symbol.Var> params) {
            this.name = name;
            this.retType = retType;
            this.params = params;
            this.label = new Label(name);
            paramSize = 0;
            for (var sym : params) {
                if (sym.type instanceof Type.Primitive) 
                    paramSize += 4;
                else paramSize = paramSize / 8 * 8 + 16;
                sym.index = paramSize;
            }
            paramSize = paramSize / 8 * 8 + 8;
            for (var param : params) {
                param.index -= paramSize;
            }
        }

        public int getSize() {
            return raSave ? (size + 16 + argSize) / 16 * 16 + 16 : (size + 8 + argSize) / 16 * 16 + 16;
        }

        public int getIndex(Var var) {
            if (var.index <= 0) return var.index;
            else return raSave ? 16 + var.index : 8 + var.index;
        }
    }
}
