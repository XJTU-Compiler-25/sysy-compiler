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
        //TODO: format
        return "errMsg=" + errMsg;
    }
    
}
