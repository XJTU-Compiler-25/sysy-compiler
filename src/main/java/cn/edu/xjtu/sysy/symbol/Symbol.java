package cn.edu.xjtu.sysy.symbol;

import java.util.List;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.riscv.Label;

public abstract sealed class Symbol {
    public static final class VarSymbol extends Symbol {
        public boolean isGlobal;
        public String name;
        public Type type;
        public boolean isConst;

        public Number comptimeValue;

        public Value address;

        public Label label;

        /** 变量相对FP的相对地址。 函数参数 <= 0, 局部变量 > 0 */
        public int addr;
        /** 变量在当前作用域是否已经被声明。 */
        public boolean declared;

        public VarSymbol(String name, boolean isGlobal, Type type, boolean isConst) {
            this.name = name;
            this.isGlobal = isGlobal;
            this.type = type;
            this.isConst = isConst;
            this.label = new Label(String.format("$%s", name));
        }
    }

    public static final class FuncSymbol extends Symbol {
        public String name;
        public Type.Function funcType;
        public List<VarSymbol> params;

        public boolean isExternal;

        public Function address;

        public final Label label;
        public int localSize;
        public int inSize;
        public int outSize = 0;
        public boolean raSave = false;

        public FuncSymbol(String name, Type.Function funcType, List<VarSymbol> params) {
            this(name, funcType, params, false);
        }

        public FuncSymbol(String name, Type.Function funcType, List<VarSymbol> params, boolean isExternal) {
            this.name = name;
            this.funcType = funcType;
            this.params = params;
            this.isExternal = isExternal;
            this.label = new Label(name);
            inSize = 0;
            for (var param : params) {
                if (param.type instanceof Type.Int || param.type instanceof Type.Float) {
                    inSize += Types.sizeOf(param.type);
                } else {
                    inSize = alignExc(inSize, 8) + 16;
                }
                param.addr = inSize;
            }
            inSize = alignExc(inSize, 8) + 8;
            for (var param : params) {
                param.addr -= inSize;
            }
        }

        public static int align(int num, int alignment) {
            return num / alignment * alignment;
        }

        public static int alignExc(int num, int alignment) {
            return num % alignment == 0 ? num - alignment : num / alignment * alignment;
        }


        public int getSize() {
            return raSave
                    ? alignExc(16 + localSize + outSize, 16) + 16
                    : alignExc(8 + localSize + outSize, 16) + 16;
        }

        public int getIndex(VarSymbol var) {
            /** 参数 */
            if (var.addr <= 0) return var.addr;
            /** 局部变量 */
            else return raSave ? 16 + var.addr : 8 + var.addr;
        }
    }
}
