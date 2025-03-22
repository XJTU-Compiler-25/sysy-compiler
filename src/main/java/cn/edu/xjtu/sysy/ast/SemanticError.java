package cn.edu.xjtu.sysy.ast;

import cn.edu.xjtu.sysy.ast.node.Node;
import cn.edu.xjtu.sysy.error.Err;

public final class SemanticError extends Err {
    private final String errMsg;
    private final Node errNode;

    public SemanticError(Node errNode, String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
        this.errNode = errNode;
    }

    @Override
    public String toString() {
        int[] location = errNode.getLocation();
        return String.format("%d:%d: error: %s", location[0], location[1], errMsg);
    }
    
}
