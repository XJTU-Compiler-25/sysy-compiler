package cn.edu.xjtu.sysy.astnodes;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

/** Block */
public final class Block extends Node {
    public final List<Node> blockItems;

    public Block(Token start, Token end) {
        super(start, end);
        this.blockItems = new ArrayList<>();
    }

    public void addBlockItem(Node node) {
        this.blockItems.add(node);
    }

    @Override
    public String toString() {
        return "Block [blockItems=" + blockItems + "]";
    }

}
