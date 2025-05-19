package cn.edu.xjtu.sysy.symbol;

public record SourceLocation(int startLine, int locInStartLine, int endLine, int locInEndLine) {
    @Override
    public String toString() {
        return String.format("%d:%d-%d:%d", startLine, locInStartLine, endLine, locInEndLine);
    }
}
