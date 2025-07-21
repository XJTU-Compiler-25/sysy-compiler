package cn.edu.xjtu.sysy.mir.node;

public record Loop(Loop parent, BasicBlock preheader, BasicBlock header, BasicBlock exit) {
    public static Loop of(Loop parent, BasicBlock preheader, BasicBlock header, BasicBlock exit) {
        return new Loop(parent, preheader, header, exit);
    }

    public static Loop of(BasicBlock preheader, BasicBlock header, BasicBlock exit) {
        return new Loop(null, preheader, header, exit);
    }

}
