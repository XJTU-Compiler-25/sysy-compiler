package cn.edu.xjtu.sysy.riscv.node;

import java.util.ArrayList;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.symbol.Type;

public class MachineFunc {

    public String name;
    public Type.Function funcType;
    public ArrayList<MachineBasicBlock> blocks = new ArrayList<>();
    public MachineBasicBlock entry;
    public MachineBasicBlock epilogue;

    // 是否没有副作用
    public boolean isPure;

    public MachineFunc(Function function) {
        this.name = function.name;
        this.funcType = function.funcType;
        this.isPure = function.isPure;
    }

    public void setEntry(MachineBasicBlock block) {
        entry = block;
    }

    public void setEpilogue(MachineBasicBlock block) {
        epilogue = block;
    }

    public void addBlock(MachineBasicBlock block) {
        blocks.add(block);
    } 
}
