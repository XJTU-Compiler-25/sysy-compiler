package cn.edu.xjtu.sysy.type;

public final class ArrayType extends ValueType {
    private final BaseType elementType;
    // At this point, dimensions of array should be determined. 
    private final int[] dimensions;

    public ArrayType(BaseType elementType, int[] dimensions) {
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
        if (dimensions.length != arrType.dimensions.length
                || this.elementType.equals(arrType.elementType)) {
            return false;
        }
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] != arrType.dimensions[i]) {
                return false;
            }
        }
        return true;
    }
}
