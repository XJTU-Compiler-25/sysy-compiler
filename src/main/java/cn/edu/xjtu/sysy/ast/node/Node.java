package cn.edu.xjtu.sysy.ast.node;

import org.antlr.v4.runtime.Token;

import java.util.Arrays;

import static cn.edu.xjtu.sysy.util.Assertions.requires;

// AST类的基类。
public abstract sealed class Node permits CompUnit, Decl, Expr, Stmt {
    // 节点的类型名称
    public final String kind;
    // 源位置信息
    private final int[] location;

    public Node(int[] location, String kind) {
        requires(location.length == 4);
        this.kind = this.getClass().getSimpleName();
        this.location = Arrays.copyOf(location, 4);
    }

    public Node(int[] location) {
        requires(location.length == 4);
        this.kind = this.getClass().getSimpleName();
        this.location = Arrays.copyOf(location, 4);
    }

    public Node(Token start, Token end) {
        this.kind = this.getClass().getSimpleName();
        if (start == null || end == null) {
            this.location = new int[] { -1, -1, -1, -1 };
        } else {
            this.location = new int[]{start.getLine(), start.getCharPositionInLine(), end.getLine(), end.getCharPositionInLine()};
        }
    }

    @Override
    public String toString() {
        return kind + " At (" + location[0] + ":" + location[1] + ";" + location[2] + ":" + location[3] + ")";
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        System.arraycopy(location, 0, this.location, 0, 4);
    }
}
