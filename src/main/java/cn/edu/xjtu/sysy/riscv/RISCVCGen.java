package cn.edu.xjtu.sysy.riscv;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.User;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.mir.node.Var;

import static cn.edu.xjtu.sysy.util.Assertions.unreachable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.text.StyledEditorKit;

import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.BlockArgument;
import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.Br;
import cn.edu.xjtu.sysy.mir.node.Instruction.Jmp;
import cn.edu.xjtu.sysy.mir.node.Instruction.Ret;
import cn.edu.xjtu.sysy.mir.node.Instruction.RetV;
import cn.edu.xjtu.sysy.riscv.node.Global;
import cn.edu.xjtu.sysy.riscv.node.MachineBasicBlock;
import cn.edu.xjtu.sysy.riscv.node.MachineFunc;
import cn.edu.xjtu.sysy.riscv.node.Program;
import cn.edu.xjtu.sysy.riscv.node.Instr;
import cn.edu.xjtu.sysy.riscv.node.PhysicalRegisters;
import static cn.edu.xjtu.sysy.riscv.node.PhysicalRegisters.A0;
import static cn.edu.xjtu.sysy.riscv.node.PhysicalRegisters.A1;
import static cn.edu.xjtu.sysy.riscv.node.PhysicalRegisters.getA;
import static cn.edu.xjtu.sysy.riscv.node.PhysicalRegisters.getFA;
import cn.edu.xjtu.sysy.riscv.node.Register;
import cn.edu.xjtu.sysy.riscv.node.Register.Virtual;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;
import cn.edu.xjtu.sysy.util.Placeholder;

public class RISCVCGen {

    private int cnt = 0;
    private MachineBasicBlock newBlock() {
        return new MachineBasicBlock(cnt++);
    }

    private int ireg = 0;
    private Register.Int freshIntRegister() {
        return new Register.Int(Integer.toString(ireg++));
    }
    private int freg = 0;
    private Register.Float freshFloatRegister() {
        return new Register.Float(Integer.toString(freg++));
    }

    private final Map<Value, Register> mapToReg = new HashMap<>();
    private Register getRegisterFrom(Value val) {
        if (mapToReg.containsKey(val)) return mapToReg.get(val);
        Register reg = switch (val.type) {
            case Type.Int _ -> freshIntRegister();
            case Type.Float _ -> freshFloatRegister();
            default -> unreachable();
        };
        mapToReg.put(val, reg);
        return reg;
    }
    private Register.Int getIntRegisterFrom(Value val) {
        Assertions.requires(val.type.equals(Types.Int));
        return (Register.Int) getRegisterFrom(val);
    }
    private Register.Float getFloatRegisterFrom(Value val) {
        Assertions.requires(val.type.equals(Types.Float));
        return (Register.Float) getRegisterFrom(val);
    }

    public Program visit(Module module) {
        Program program = new Program();
        module.globalVars.forEach((name, var) -> {
            var init = module.globalVarInitValues.get(var);
            program.addGlobl(new Global(name, var.type, init));
        });
        module.functions.values().forEach(func -> program.addFunc(visit(func)));
        return program;
    }

    public MachineFunc visit(Function function) {
        var func = new MachineFunc(function);
        func.setEntry(newBlock());
        func.setEpilogue(newBlock());
        function.blocks.forEach(block -> func.addBlock(visit(block)));
        return func;
    }

    public MachineBasicBlock visit(BasicBlock block) {
        MachineBasicBlock bb = newBlock();
        for (var instr : block.instructions) {
            bb.addAll(visit(instr));
        }
        bb.addAll(visit(block.terminator));
        return bb;
    }

    public List<Instr> visit(Instruction instruction) {
        return switch (instruction) {
            case Instruction.Terminator i -> visit(i);
            case Instruction.Call i2 -> visit(i2);
            case Instruction.CallExternal i3 -> visit(i3);
            case Instruction.Alloca i4 -> visit(i4);
            case Instruction.Load i5 -> visit(i5);
            case Instruction.Store i6 -> visit(i6);
            case Instruction.GetElemPtr i7 -> visit(i7);
            case Instruction.I2F i8 -> visit(i8);
            case Instruction.F2I i9 -> visit(i9);
            case Instruction.BitCastI2F i10 -> visit(i10);
            case Instruction.BitCastF2I i11 -> visit(i11);
            case Instruction.IAdd i12 -> visit(i12);
            case Instruction.ISub i13 -> visit(i13);
            case Instruction.IMul i14 -> visit(i14);
            case Instruction.IDiv i15 -> visit(i15);
            case Instruction.IMod i16 -> visit(i16);
            case Instruction.INeg i17 -> visit(i17);
            case Instruction.FAdd i18 -> visit(i18);
            case Instruction.FSub i19 -> visit(i19);
            case Instruction.FMul i20 -> visit(i20);
            case Instruction.FDiv i21 -> visit(i21);
            case Instruction.FMod i22 -> visit(i22);
            case Instruction.FNeg i23 -> visit(i23);
            case Instruction.Shl i24 -> visit(i24);
            case Instruction.Shr i25 -> visit(i25);
            case Instruction.AShr i26 -> visit(i26);
            case Instruction.And i27 -> visit(i27);
            case Instruction.Or i28 -> visit(i28);
            case Instruction.Xor i29 -> visit(i29);
            case Instruction.Not i30 -> visit(i30);
            case Instruction.IEq i31 -> visit(i31);
            case Instruction.INe i32 -> visit(i32);
            case Instruction.IGt i33 -> visit(i33);
            case Instruction.ILt i34 -> visit(i34);
            case Instruction.IGe i35 -> visit(i35);
            case Instruction.ILe i36 -> visit(i36);
            case Instruction.FEq i37 -> visit(i37);
            case Instruction.FNe i38 -> visit(i38);
            case Instruction.FGt i39 -> visit(i39);
            case Instruction.FLt i40 -> visit(i40);
            case Instruction.FGe i41 -> visit(i41);
            case Instruction.FLe i42 -> visit(i42);
            case Instruction.FSqrt i43 -> visit(i43);
            case Instruction.FAbs i44 -> visit(i44);
            case Instruction.FMin i45 -> visit(i45);
            case Instruction.FMax i46 -> visit(i46);
            default -> Assertions.unreachable();  
        };
    }

    public List<Instr> visit(Instruction.Terminator terminator) {
        return switch (terminator) {
            case Instruction.Ret i -> visit(i);
            case Instruction.RetV i2 -> visit(i2);
            case Instruction.Jmp i3 -> visit(i3);
            case Instruction.Br i4 -> visit(i4);
            default -> Assertions.unreachable(); 
        };
    }

    private Register getRegisterFrom(Value val, RiscVWriter asm) {
        return switch (val) {
            case ImmediateValue i -> {
                switch (i) {
                    case ImmediateValue.IntConst it -> { 
                        var reg = freshIntRegister();
                        asm.li(reg, it.value); 
                        yield reg;
                    }
                    case ImmediateValue.FloatConst it -> { 
                        var intRegister = freshIntRegister();
                        asm.li(intRegister, it.value);
                        var floatRegister = freshFloatRegister();
                        asm.fcvt_s_l(floatRegister, intRegister);
                        yield floatRegister;
                    }
                    case ImmediateValue.ZeroInit _ -> {
                        var reg = freshIntRegister();
                        asm.li(reg, 0);
                        yield reg;
                    }
                    default -> { yield unreachable(); }
                }
            }
            default -> getRegisterFrom(val);
        };
    }

    private Register.Int getIntRegisterFrom(Value val, RiscVWriter asm) {
        Assertions.requires(val.type.equals(Types.Int));
        return (Register.Int) getRegisterFrom(val, asm);
    }

    private Register.Float getFloatRegisterFrom(Value val, RiscVWriter asm) {
        Assertions.requires(val.type.equals(Types.Float));
        return (Register.Float) getRegisterFrom(val, asm);
    }

    public List<Instr> visit(Instruction.Call call) {
        RiscVWriter asm = new RiscVWriter();
        int intRegSize = 0;
        int floatRegSize = 0;
        Stack<Register> spill = new Stack<>();
        for (var arg : call.args) {
            switch (arg.value.type) {
                case Type.Int _, Type.Pointer _ -> {
                    var rd = getA(intRegSize++);
                    var rs = getIntRegisterFrom(arg.value, asm);
                    if (rd == null) spill.add(rs);
                    else asm.mv(rd, rs);
                }
                case Type.Float _ -> {
                    var rd = getFA(intRegSize++);
                    var rs = getFloatRegisterFrom(arg.value, asm);
                    if (rd == null) spill.add(rs);
                    else asm.fmv(rd, rs);
                }
                default -> unreachable();
                
            }
        }
        int spilledSize = 0;
        while (!spill.isEmpty()) {
            var spilled = spill.pop();
            
        }

        asm.call(it.function);

        return asm.getInstrs();
    }
}
