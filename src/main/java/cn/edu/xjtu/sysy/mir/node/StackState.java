package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.util.MathUtils;

import static cn.edu.xjtu.sysy.symbol.Types.alignmentOf;
import static cn.edu.xjtu.sysy.symbol.Types.sizeOf;

public final class StackState {
    StackState() { }

    public int cursor = 0;

    public int allocate(Type type) {
        return allocate(sizeOf(type), alignmentOf(type));
    }

    public int allocate(int size, int alignment) {
        pad(alignment);
        var res = cursor;
        cursor += size;
        return res;
    }

    public void pad(int alignment) {
        cursor = MathUtils.roundTo(cursor, alignment);
    }

    public int getSize() {
        return cursor;
    }

}
