package cn.edu.xjtu.sysy.type;

import cn.edu.xjtu.sysy.astnodes.TypeAnnotation;

public final class BaseType extends ValueType {
    private final String name;

    public BaseType(String name) {
        this.name = name;
    }

    public BaseType(TypeAnnotation type) {
        this.name = type.name;
    }

    @Override
    public boolean isBaseType() {
        return true;
    }

    public boolean equals(ValueType type) {
        if (this == type) {
            return true;
        }
        if (type == null) {
            return false;
        }
        if (getClass() != type.getClass()) {
            return false;
        }
        BaseType bType = (BaseType) type;
        return this.name.equals(bType.name);
    }
}
