package cn.edu.xjtu.sysy.mir.node;

public final class Use {
    public Value value;
    public Instruction user;

    public Use(Value value, Instruction user) {
        this.value = value;
        this.user = user;
    }
}
