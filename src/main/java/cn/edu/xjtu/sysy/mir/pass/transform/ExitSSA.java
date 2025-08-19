package cn.edu.xjtu.sysy.mir.pass.transform;

import cn.edu.xjtu.sysy.mir.node.*;
import cn.edu.xjtu.sysy.mir.pass.ModulePass;
import cn.edu.xjtu.sysy.riscv.Register;
import cn.edu.xjtu.sysy.riscv.StackPosition;
import cn.edu.xjtu.sysy.riscv.ValuePosition;
import cn.edu.xjtu.sysy.symbol.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static cn.edu.xjtu.sysy.riscv.ValueUtils.phiElimFloatReg;
import static cn.edu.xjtu.sysy.riscv.ValueUtils.phiElimIntReg;

// phi elimination pass
public final class ExitSSA extends ModulePass<Void> {

    private static final LIRInstrHelper helper = new LIRInstrHelper();

    private Function currentFunction;
    @Override
    public void visit(Function function) {
        currentFunction = function;
        removePhi();
    }


    private void removePhi() {
        // 先实现将 call conv 传递到 entry args
        processEntryArgs();

        // 然后将每个块的 phi 指令转换为对应的 move 指令
        for (var block : currentFunction.blocks) {
            block.args.clear();
            switch (block.terminator) {
                case Instruction.AbstractBr br -> {
                    processBranchPhiFromFirst(br.getTrueTarget(), br.trueParams);
                    processBranchPhiFromFirst(br.getFalseTarget(), br.falseParams);
                }
                case Instruction.Jmp jmp -> {
                    processBranchPhiFromLast(jmp.getBlock(), jmp.params);
                }
                default -> { }
            }
        }
    }

    private void processEntryArgs() {
        var stackState = currentFunction.stackState;
        var entry = currentFunction.entry;
        helper.changeBlock(entry);

        var entryParams = entry.args;
        var intParams = new ArrayList<BlockArgument>();
        var floatParams = new ArrayList<BlockArgument>();
        for (var param : entryParams) {
            if (param.type != Types.Float) intParams.add(param);
            else floatParams.add(param);
        }

        for (int i = 0; i < intParams.size(); i++) {
            var param = intParams.get(i);
            var type = param.type;
            if (i < 8) {
                var reg = Register.A(i);
                var dummy = helper.dummyDef(type);
                dummy.position = reg;
                var mv = helper.imv(param, dummy);
                entry.insertAtFirst(mv);
                currentFunction.paramToDummy.put(param, dummy);
            } else {
                var dummy = helper.dummyDef(type);
                dummy.position = new StackPosition(stackState.allocate(type));
                var load = helper.imv(param, dummy);
                entry.insertAtFirst(load);
                currentFunction.paramToDummy.put(param, dummy);
            }
        }
        for (int i = 0; i < floatParams.size(); i++) {
            var param = floatParams.get(i);
            var type = param.type;
            if (i < 8) {
                var reg = Register.FA(i);
                var dummy = helper.dummyDef(type);
                dummy.position = reg;
                var mv = helper.fmv(param, dummy);
                entry.insertAtFirst(mv);
                currentFunction.paramToDummy.put(param, dummy);
            } else {
                var dummy = helper.dummyDef(type);
                dummy.position = new StackPosition(stackState.allocate(type));
                var load = helper.fmv(param, dummy);
                entry.insertAtFirst(load);
                currentFunction.paramToDummy.put(param, dummy);
            }
        }
    }

    private void processBranchPhiFromLast(BasicBlock self, Map<BlockArgument, Use> params) {
        if (params.isEmpty()) return;
        helper.changeBlock(self);

        ArrayList<Instruction> moves = new ArrayList<>();

        // 使用 phi-elim 临时寄存器避免循环依赖
        var intValues = new HashMap<Value, Value>();
        var floatValues = new HashMap<Value, Value>();
        // 如果 value 会导致寄存器循环依赖，分配 phi-elim 临时寄存器
        for (var arg : params.keySet()) {
            var use = params.get(arg);
            var value = use.value;
            var isInt = value.type != Types.Float;
            if (isInt) {
                intValues.put(arg, value);
            } else {
                floatValues.put(arg, value);
            }
        };
        System.out.println(intValues);
        if (!intValues.isEmpty()) moves.addAll(move(intValues, true));
        if (!floatValues.isEmpty()) moves.addAll(move(floatValues, false));
        /*params.forEach((arg, use) -> {
            var value = use.value;
            var isInt = value.type != Types.Float;

            // 用 dummy 占用 phi-elim 寄存器
            var temp = hp2.dummyDef(value.type);
            temp.position = isInt ? phiElimIntReg : phiElimFloatReg;
            tempValues.put(value, temp);

            // 先搬到临时 Value
            var mvToTemp = isInt ? hp2.imv(temp, value) : hp2.fmv(temp, value);
            tempInstrs.add(temp);
            tempInstrs.add(mvToTemp);
        });

        params.forEach((arg, use) -> {
            var value = use.value;
            var isInt = value.type != Types.Float;

            var temp = tempValues.get(value);
            var mvToArg = isInt ? hp2.imv(arg, temp) : hp2.fmv(arg, temp);
            tempInstrs.add(mvToArg);
        });
*/
        moves.forEach(self::insertAtLast);
        params.clear();
    }

    private void processBranchPhiFromFirst(BasicBlock target, Map<BlockArgument, Use> params) {
        if (params.isEmpty()) return;
        helper.changeBlock(target);

        ArrayList<Instruction> moves = new ArrayList<>();

        // 使用 phi-elim 临时寄存器避免循环依赖
        var intValues = new HashMap<Value, Value>();
        var floatValues = new HashMap<Value, Value>();
        // 如果 value 会导致寄存器循环依赖，分配 phi-elim 临时寄存器
        for (var arg : params.keySet()) {
            var use = params.get(arg);
            var value = use.value;
            var isInt = value.type != Types.Float;
            if (isInt) {
                intValues.put(arg, value);
            } else {
                floatValues.put(arg, value);
            }
        }
        if (!intValues.isEmpty()) moves.addAll(move(intValues, true));
        if (!floatValues.isEmpty()) moves.addAll(move(floatValues, false));
        /*params.forEach((arg, use) -> {
            var value = use.value;
            var isInt = value.type != Types.Float;

            // 用 dummy 占用 phi-elim 寄存器
            var temp = hp2.dummyDef(value.type);
            temp.position = isInt ? phiElimIntReg : phiElimFloatReg;
            tempValues.put(value, temp);

            // 先搬到临时 Value
            var mvToTemp = isInt ? hp2.imv(temp, value) : hp2.fmv(temp, value);
            tempInstrs.add(temp);
            tempInstrs.add(mvToTemp);
        });

        params.forEach((arg, use) -> {
            var value = use.value;
            var isInt = value.type != Types.Float;

            var temp = tempValues.get(value);
            var mvToArg = isInt ? hp2.imv(arg, temp) : hp2.fmv(arg, temp);
            tempInstrs.add(mvToArg);
        });
*/
        moves.reversed().forEach(target::insertAtFirst);
        params.clear();
    }

    private ArrayList<Instruction> move(HashMap<Value, Value> map, boolean isInt) {
        var instrs = new ArrayList<Instruction>();
        Instruction tmp = null;
        var visited = new HashSet<Value>();
        for (var dst : map.keySet()) {
            if (visited.contains(dst)) continue;
            var cur = dst;
            var path = new ArrayList<Value>();
            var valuePath = new ArrayList<ValuePosition>();
            while (true) { 
                path.add(cur);
                valuePath.add(cur.position);
                if (!visited.add(cur) || !map.containsKey(cur)) break;
                var prev = map.get(cur);
                var idx = valuePath.indexOf(prev.position);
                if (idx != -1) {
                    var cycle = path.subList(idx, path.size());
                    if (tmp == null) {
                        tmp = helper.dummyDef(cur.type);
                        tmp.position = isInt ? phiElimIntReg : phiElimFloatReg;
                        instrs.addFirst(tmp);
                    }
                    processCycle(cycle, instrs, map, tmp, isInt);
                    path = new ArrayList<>(path.subList(0, idx));
                    break;
                } else {
                    cur = prev;
                }
            }
            processChain(path, instrs, map, isInt);
        }
        return instrs;
    }

    private void processChain(List<Value> route, ArrayList<Instruction> moves, 
                                HashMap<Value, Value> map, boolean isInt) {
        for (int i = 0; i < route.size() - 1; i++) {
            var dst = route.get(i);
            moves.add(isInt ? helper.imv(dst, map.get(dst)) : helper.fmv(dst, map.get(dst)));
        }
    }

    private void processCycle(List<Value> route, ArrayList<Instruction> moves, 
                                HashMap<Value, Value> map, Value tmp, boolean isInt) {
        if (route.size() == 1) {
            // 自环
            moves.add(isInt ? helper.imv(route.getFirst(), map.get(route.getFirst()))
                            : helper.fmv(route.getFirst(), map.get(route.getFirst())));
            return;
        }
        moves.add(isInt ? helper.imv(tmp, map.get(route.getFirst()))
                        : helper.fmv(tmp, map.get(route.getFirst())));
        for (int i = 0; i < route.size() - 1; i++) {
            var dst = route.get(i);
            moves.add(isInt ? helper.imv(dst, map.get(dst)) : helper.fmv(dst, map.get(dst)));
        }
        moves.add(isInt ? helper.imv(route.getLast(), tmp) : helper.fmv(route.getLast(), tmp));
    }
}
