package cn.edu.xjtu.sysy.riscv.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;

public sealed abstract class Register extends Value {
    protected final String name;

    public Register(String name, Type type) {
        super(type);
        this.name = name;
    }

    public static final class Int extends Register {
        public Int(String name) { 
            super(name, Types.Int);
        }
    } 

    public static final class Float extends Register {
        public Float(String name) { 
            super(name, Types.Float);
        }
    } 

    public static final class Virtual extends Register {
        public Virtual(String name, Type type) {
            super(name, type);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
