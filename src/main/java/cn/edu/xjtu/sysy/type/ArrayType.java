package cn.edu.xjtu.sysy.type;

import java.util.List;

import cn.edu.xjtu.sysy.astnodes.Expr;

public final class ArrayType extends ValueType {
    //TODO
    private BaseType elementType;
    private List<Expr> dimensions;

    public ArrayType(BaseType elementType, List<Expr> dimensions) {
        this.elementType = elementType;
        this.dimensions = dimensions;
    }

    @Override
    public boolean isArrayType() {
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
        ArrayType arrType = (ArrayType) type;

        // TODO: check dimensions equal. Constant Folding of dimension should be done at this moment.
        boolean dimensionEq = false;
        return this.elementType.equals(arrType.elementType) && dimensionEq;

    }

}
