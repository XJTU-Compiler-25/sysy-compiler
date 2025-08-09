package cn.edu.xjtu.sysy.riscv.node;

import java.util.Objects;

public class Label {
    public final String labelName;

    public Label(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Label label = (Label) o;
        return Objects.equals(labelName, label.labelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelName);
    }
    
    @Override
    public String toString() {
        return labelName;
    }
}
