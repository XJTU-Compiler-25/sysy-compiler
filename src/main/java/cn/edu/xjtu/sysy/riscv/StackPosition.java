package cn.edu.xjtu.sysy.riscv;

public record StackPosition(int offset, boolean isArgument) implements ValuePosition {
    
    public StackPosition(int offset) {
        this(offset, false);
    }
    
    @Override
    public String toString() {
        return "(fp + " + offset + ")";
    }
}
