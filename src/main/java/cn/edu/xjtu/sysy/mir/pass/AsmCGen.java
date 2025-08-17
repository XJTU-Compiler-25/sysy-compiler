package cn.edu.xjtu.sysy.mir.pass;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashMap;

import cn.edu.xjtu.sysy.mir.node.Function;
import cn.edu.xjtu.sysy.mir.node.Instruction;
import cn.edu.xjtu.sysy.mir.node.Instruction.*;
import cn.edu.xjtu.sysy.mir.node.BasicBlock;
import cn.edu.xjtu.sysy.mir.node.GlobalVar;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.DenseArray;
import cn.edu.xjtu.sysy.mir.node.ImmediateValue.FloatConst;
import static cn.edu.xjtu.sysy.mir.node.ImmediateValues.iZero;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.node.Value;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.Register.Int;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.riscv.ValueUtils;
import cn.edu.xjtu.sysy.riscv.node.AsmWriter;
import cn.edu.xjtu.sysy.riscv.node.Global;
import cn.edu.xjtu.sysy.riscv.node.MachineBasicBlock;
import cn.edu.xjtu.sysy.riscv.node.MachineFunc;
import cn.edu.xjtu.sysy.riscv.node.Instr.Call;
import cn.edu.xjtu.sysy.symbol.Type;
import cn.edu.xjtu.sysy.symbol.Types;
import cn.edu.xjtu.sysy.util.Assertions;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import cn.edu.xjtu.sysy.util.Worklist;

public class AsmCGen extends ModulePass<Void> {
    private final HashMap<GlobalVar, Global> globals = new HashMap<>();
    private final HashMap<Function, MachineFunc> functions = new HashMap<>();
    private final AsmWriter asm = new AsmWriter();
    private MachineFunc curFunc;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb  .append(globals.values().stream().map(global -> global.toString()).collect(Collectors.joining()))
            .append(functions.values().stream().map(global -> global.toString()).collect(Collectors.joining()));
        return sb.toString();
    }

    @Override
    public void visit(Module module) {
        globals.clear();
        var initVals = module.globalVarInitValues;
        module.getGlobalVars().forEach(var -> {
            var globl = new Global(var.shortName(), var.varType, initVals.get(var));
            globals.put(var, globl);
        });
        module.getFunctions().forEach(func -> {
            var rvFunc = new MachineFunc(func.shortName(), func.funcType);
            functions.put(func, rvFunc);
            curFunc = rvFunc;
            visit(func);
        });
    }

    @Override
    public void visit(Function function) {
        var worklist = new Stack<BasicBlock>();
        var visited = new HashSet<BasicBlock>();
        worklist.add(function.entry);
        while (!worklist.isEmpty()) {
            var block = worklist.pop();
            if (!visited.add(block)) continue;
            var machineBlock = new MachineBasicBlock(block.shortName());
            curFunc.addBlock(machineBlock);
            asm.changeBlock(machineBlock);
            visit(block);
            switch (block.terminator) {
                case AbstractBr br -> {
                    worklist.push(br.getTrueTarget());
                    worklist.push(br.getFalseTarget());
                }
                case Jmp jmp -> {
                    worklist.push(jmp.getTarget());
                }
                default -> {}
            }
        }
    }

    @Override
    public void visit(BasicBlock block) {
        block.instructions.forEach(this::visit);
        visit(block.terminator);
    }

    private Register.Int getAddr(Value value, Register.Int ret, Register.Int tmp1, Register.Int tmp2) {
        Assertions.requires(value.type instanceof Type.Pointer);
        return switch (value) {
            case GlobalVar it -> {
                asm.la(ret, it.name);
                yield ret;
            }
            case Instruction.GetElemPtr it -> {
                var bv = it.basePtr.value;
                var base = getAddr(bv, ret, tmp1, tmp2);
                var strides = Types.strides(bv.type);
                var indices = it.indices;
                int offset = 0;
                boolean init = true;
                for (int i = 0, len = indices.length; i < len; i++) {
                    if (indices[i].value instanceof ImmediateValue.IntConst ic) {
                        offset += ic.value * strides[i];
                        continue;
                    }
                    if (init) {
                        init = false;
                        asm.addi(ret, ret, offset * 4, tmp1);
                        continue;
                    }
                    var now = getInt(indices[i].value, tmp1);
                    asm.li(tmp2, strides[i] * 4);
                    asm.mul(tmp1, tmp1, tmp2);
                    asm.add(ret, ret, tmp1);
                }
                if (init) {
                    asm.addi(ret, ret, offset * 4, tmp1);
                }
                yield ret;
            }
            default -> {
                yield switch(value.position) {
                    case Register.Int r -> r;
                    case StackPosition(int offset) -> {
                        asm.ld(ret, Int.FP, offset);
                        yield ret;
                    }
                    default -> unreachable();
                };
            }
        };
    }

    private Register.Int getInt(Value value, Register.Int ret) {
        Assertions.requires(value.type.equals(Types.Int));
        return switch (value) {
            case ImmediateValue.IntConst it -> {
                if (it.value == 0) yield Register.Int.ZERO;
                asm.li(ret, it.value);
                yield ret;
            }
            case GlobalVar it -> {
                asm.la(ret, it.shortName());
                asm.lw(ret, Int.FP, 0);
                yield ret;
            }
            default -> {
                yield switch(value.position) {
                    case Register.Int r -> r;
                    case StackPosition(int offset) -> {
                        asm.lw(ret, Int.FP, offset);
                        yield ret;
                    }
                    default -> unreachable();
                };
            }
        };
    }

    private Register.Int getFloatAsCond(Value value, Register.Int ret) {
        Assertions.requires(value.type.equals(Types.Float));
        return switch (value) {
            case ImmediateValue.FloatConst it -> {
                if (it.value == 0) yield Register.Int.ZERO;
                asm.li(ret, it.value);
                yield ret;
            }
            case GlobalVar it -> {
                asm .la(ret, it.shortName())
                    .lw(ret, ret, 0);
                yield ret;
            }
            default -> {
                yield switch(value.position) {
                    case Register.Float r -> {
                        asm.fmv_x_w(ret, r);
                        yield ret;
                    }
                    case StackPosition(int offset) -> {
                        asm.lw(ret, Int.FP, offset);
                        yield ret;
                    }
                    default -> unreachable();
                };
            }
        };
    }

    private Register.Float getFloat(Value value, Register.Float ret, Register.Int tmp) {
        Assertions.requires(value.type.equals(Types.Float));
        return switch (value) {
            case ImmediateValue.FloatConst it -> {
                if (it.value == 0) asm.fcvt_s_l(ret, Register.Int.ZERO);
                else asm.li(tmp, it.value)
                        .fcvt_s_l(ret, tmp);
                yield ret;
            }
            case GlobalVar it -> {
                asm .la(tmp, it.shortName())
                    .flw(ret, tmp, 0);
                yield ret;
            }
            default -> {
                yield switch(value.position) {
                    case Register.Float r -> r;
                    case StackPosition(int offset) -> {
                        asm.flw(ret, Int.FP, offset);
                        yield ret;
                    }
                    default -> unreachable();
                };
            }
        };
    }

    @Override
    public void visit(Instruction instruction) {
        switch (instruction) {
            case AbstractCall it -> {
                asm.call(it.getLabel());
                switch (it.type) {
                    case Type.Float _ -> {
                        asm.fmv(it, ValueUtils.floatRetAddr);
                    }
                    case Type.Void _ -> { /* DO NOTHING */}
                    case Type.Int _ -> {
                        asm.mv(it, ValueUtils.intRetAddr);
                    }
                    default -> unreachable();
                }
            }
            case Ret it -> {
                switch (it.retVal.value.type) {
                    case Type.Float _ -> {
                        var retVal = getFloat(it.retVal.value, ValueUtils.floatRetAddr, ValueUtils.intScratchReg);
                        if (ValueUtils.floatRetAddr != retVal) {
                            asm.fmv(ValueUtils.floatRetAddr, retVal);
                        }
                    }
                    case Type.Int _ -> {
                        var retVal = getInt(it.retVal.value, ValueUtils.intRetAddr);
                        if (ValueUtils.intRetAddr != retVal) {
                            asm.mv(ValueUtils.intRetAddr, retVal);
                        }
                    }
                    default -> unreachable();
                }
                asm.ret();
            }
            case RetV it -> {
                asm.ret();
            }
            case Jmp it -> {
                asm.j(it.getTarget().shortName());
            }
            case Br it -> {
                var condition = it.getCondition();
                if (condition.type instanceof Type.Pointer) {
                    asm.j(it.getTrueTarget().shortName());
                }
                Register.Int condReg = switch (condition.type) {
                    case Type.Int _ -> getInt(condition, ValueUtils.intScratchReg);
                    case Type.Float _ -> getFloatAsCond(condition, ValueUtils.intScratchReg);
                    default -> unreachable();
                };
                asm.bnez(condReg, it.getTrueTarget().shortName());
            }
            case Instruction.BEq it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.beq(lhs, rhs, it.getTrueTarget().shortName());
            }
            case Instruction.BNe it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.bne(lhs, rhs, it.getTrueTarget().shortName());
            }
            case Instruction.BLt it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.blt(lhs, rhs, it.getTrueTarget().shortName());
            }
            case Instruction.BLe it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.ble(lhs, rhs, it.getTrueTarget().shortName());
            }
            case Instruction.BGt it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.bgt(lhs, rhs, it.getTrueTarget().shortName());
            }
            case Instruction.BGe it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.bge(lhs, rhs, it.getTrueTarget().shortName());
            }
            case Alloca it -> {
                var position = (StackPosition) it.position;
                asm.setzero(position.offset(), Types.sizeOf(it.allocatedType), ValueUtils.intScratchReg);
            }
            case Load it -> {
                var addr = getAddr(it.address.value, ValueUtils.spillIntReg, ValueUtils.spillIntReg2, ValueUtils.intScratchReg);
                switch (it.type) {
                    case Type.Float _ -> {
                        asm.flw(it, addr, 0);        
                    }
                    case Type.Int _ -> {
                        asm.lw(it, addr, 0);  
                    }
                    case Type.Pointer _ -> {
                        asm.ld(it, addr, 0);  
                    }
                    default -> unreachable();
                }
            }
            case Store it -> {
                var addr = getAddr(it.address.value, ValueUtils.spillIntReg, ValueUtils.spillIntReg2, ValueUtils.intScratchReg);
                switch (it.storeVal.value.type) {
                    case Type.Float _ -> {
                        var val = getFloat(it.storeVal.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                        asm.fsw(val, addr, 0);        
                    }
                    case Type.Int _ -> {
                        var val = getInt(it.storeVal.value, ValueUtils.spillIntReg2);
                        asm.sw(val, addr, 0);  
                    }
                    default -> unreachable();
                }
            }
            case GetElemPtr it -> {
                // 延迟计算
            }
            // 数学运算
            case IAdd it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.addw(it, lhs, rhs);
            }
            case ISub it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.subw(it, lhs, rhs);
            }
            case IMul it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.mulw(it, lhs, rhs);
            }
            case IDiv it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.divw(it, lhs, rhs);
            }
            case IMod it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.remw(it, lhs, rhs);
            }
            case INeg it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.negw(it, lhs);
            }
            case FAdd it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fadd(it, lhs, rhs);
            }
            case FSub it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fsub(it, lhs, rhs);
            }
            case FMul it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fmul(it, lhs, rhs);
            }
            case FDiv it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fdiv(it, lhs, rhs);
            }
            case FMod it -> {
                // TODO
            }
            case FNeg it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fneg(it, lhs);
            }
            // 相等运算
            case IEq it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm .xor(ValueUtils.intScratchReg, lhs, rhs)
                    .seqz(it, ValueUtils.intScratchReg);
            }
            case INe it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm .xor(ValueUtils.intScratchReg, lhs, rhs)
                    .snez(it, ValueUtils.intScratchReg);
            }
            case FEq it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.feq(it, lhs, rhs);
            }
            case FNe it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .feq(ValueUtils.intScratchReg, lhs, rhs)
                    .snez(it, ValueUtils.intScratchReg);
            }

            // 比较运算
            case IGe it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm .slt(ValueUtils.intScratchReg, lhs, rhs)
                    .snez(it, ValueUtils.intScratchReg);
            }
            case ILe it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm .slt(ValueUtils.intScratchReg, rhs, lhs)
                    .snez(it, ValueUtils.intScratchReg);
            }
            case IGt it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm .slt(it, rhs, lhs);
            }
            case ILt it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm .slt(it, lhs, rhs);
            }
            case FGe it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .fle(it, rhs, lhs);
            }
            case FLe it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .fle(it, lhs, rhs);
            }
            case FGt it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .flt(it, rhs, lhs);
            }
            case FLt it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .flt(it, lhs, rhs);
            }
            // 转型运算
            case I2F it -> {
                var lhs = getInt(it.value.value, ValueUtils.intScratchReg);
                asm .fcvt_s_l(it, lhs);
            }
            case F2I it -> {
                var lhs = getFloat(it.value.value, ValueUtils.floatScratchReg, ValueUtils.intScratchReg);
                asm .fcvt_l_s(it, lhs);
            }
            case BitCastI2F it -> {
                var lhs = getInt(it.value.value, ValueUtils.intScratchReg);
                asm .fmv_w_x(it, lhs);
            }
            case BitCastF2I it -> {
                var lhs = getFloat(it.value.value, ValueUtils.floatScratchReg, ValueUtils.intScratchReg);
                asm .fmv_x_w(it, lhs);
            }

            // 位运算
            case Shl it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.sllw(it, lhs, rhs);
            }
            // 逻辑右移
            case Shr it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.srlw(it, lhs, rhs);
            }
            // 算数右移
            case AShr it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.sraw(it, lhs, rhs);
            }

            // 逻辑运算
            case And it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.and(it, lhs, rhs);
            }
            case Or it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.or(it, lhs, rhs);
            }
            case Xor it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                var rhs = getInt(it.rhs.value, ValueUtils.spillIntReg2);
                asm.xor(it, lhs, rhs);
            }
            case Not it -> {
                var lhs = getInt(it.rhs.value, ValueUtils.spillIntReg);
                asm.snez(it, lhs);
            }

            // intrinsic
            case FAbs it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.floatScratchReg, ValueUtils.intScratchReg);
                asm .fabs(it, lhs);
            }
            case FMax it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .fmax(it, lhs, rhs);
            }
            case FMin it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                var rhs = getFloat(it.rhs.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm .fmin(it, lhs, rhs);
            }
            case FSqrt it -> {
                var lhs = getFloat(it.lhs.value, ValueUtils.floatScratchReg, ValueUtils.intScratchReg);
                asm .fsqrt(it, lhs);
            }
            case ILi it -> {
                asm.li(it, it.imm);
            }
            case FLi it -> {
                asm.li(it, it.imm);
            }
            case IMv it -> {
                var src = getInt(it.src.value, ValueUtils.spillIntReg);
                asm.mv(it.dst, src);
            }
            case FMv it -> {
                var src = getFloat(it.src.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fmv(it.dst, src);
            }
            case Instruction.ICpy it -> {
                var src = getInt(it.src.value, ValueUtils.spillIntReg);
                asm.mv(it, src);
            }
            case Instruction.FCpy it -> {
                var src = getFloat(it.src.value, ValueUtils.spillFloatReg, ValueUtils.intScratchReg);
                asm.fmv(it, src);
            }
            case Instruction.Addi it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.addiw(it, lhs, it.imm);
            }
            case Instruction.Andi it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.andi(it, lhs, it.imm);
            }
            case Instruction.Ori it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.ori(it, lhs, it.imm);
            }
            case Instruction.Xori it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.xori(it, lhs, it.imm);
            }
            case Instruction.Slli it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.slliw(it, lhs, it.imm);
            }
            case Instruction.Srli it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.srliw(it, lhs, it.imm);
            }
            case Instruction.Srai it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.sraiw(it, lhs, it.imm);
            }
            case Instruction.Slti it -> {
                var lhs = getInt(it.lhs.value, ValueUtils.spillIntReg);
                asm.slti(it, lhs, it.imm);
            }
            case Dummy _ -> {
                // Ignore
            }
        }
    }

}
