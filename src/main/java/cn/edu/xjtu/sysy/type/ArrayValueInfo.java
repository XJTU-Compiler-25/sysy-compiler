package cn.edu.xjtu.sysy.type;

import java.util.List;

import cn.edu.xjtu.sysy.scope.ValueInfo;

public class ArrayValueInfo extends ValueInfo {
    private List<ValueInfo> elementValue;

    public ArrayValueInfo(List<ValueInfo> elementValue) {
        this.elementValue = elementValue;
    }
}
