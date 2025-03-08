package cn.edu.xjtu.sysy.astnodes;

import org.antlr.v4.runtime.Token;

// AST类的基类。
public abstract class Node {

    // 类型字段
    public final String kind;

    // 源位置信息
    private final int[] location = new int[4];

    public Node(Token start, Token end) {
        if (start != null) {
            location[0] = start.getLine();
            location[1] = start.getStartIndex();
        }
        if (end != null) {
            location[2] = end.getLine();
            location[3] = end.getStartIndex();
        }
        this.kind = getClass().getSimpleName();
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        System.arraycopy(location, 0, this.location, 0, 4);
    }
}
