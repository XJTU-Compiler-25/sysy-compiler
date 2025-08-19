package cn.edu.xjtu.sysy.mir.node;

import cn.edu.xjtu.sysy.symbol.Type;
import static cn.edu.xjtu.sysy.symbol.Types.alignmentOf;
import static cn.edu.xjtu.sysy.symbol.Types.sizeOf;
import cn.edu.xjtu.sysy.util.MathUtils;

public final class StackState {
    StackState() { }

    public int cursor = 16;

    public int allocate(Type type) {
        System.out.println(type);
        return allocate(sizeOf(type), alignmentOf(type));
    }

    public int allocate(int size, int alignment) {
        pad(alignment);
        cursor += size;
        return cursor;
    }

    public void pad(int alignment) {
        cursor = MathUtils.roundTo(cursor, alignment);
    }

    public int getSize() {
        return cursor;
    }

}
