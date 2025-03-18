package cn.edu.xjtu.sysy.astnodes;

public final class SemanticError {

    private String errMsg;
    private Node errNode;

    public SemanticError(Node errNode, String errMsg) {
        this.errMsg = errMsg;
        this.errNode = errNode;
    }

    @Override
    public String toString() {
        int[] location = errNode.getLocation();
        return String.format("%d:%d: error: %s", location[0], location[1], errMsg);
    }
    
}
