package cn.edu.xjtu.sysy.mir.node;

/*
 * while(cond) body =
 * preheader: eval cond, br cond, preheader, exit
 * header: jmp body (在真正循环体前面，用来存放 LICM 提取出的量）
 * body: eval body, jmp latch
 * latch: eval cond, br cond, header, exit
 * exit: 执行循环后面的代码
 */
public record Loop(Loop parent, BasicBlock preheader, BasicBlock header, BasicBlock body, BasicBlock exit) {
    public static Loop of(Loop parent, BasicBlock preheader, BasicBlock header, BasicBlock body, BasicBlock exit) {
        return new Loop(parent, preheader, header, body, exit);
    }

    public static Loop of(BasicBlock preheader, BasicBlock header, BasicBlock body, BasicBlock exit) {
        return new Loop(null, preheader, header, body, exit);
    }

}
