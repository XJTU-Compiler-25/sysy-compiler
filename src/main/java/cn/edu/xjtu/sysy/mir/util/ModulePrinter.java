package cn.edu.xjtu.sysy.mir.util;

import cn.edu.xjtu.sysy.mir.node.BlockArgument;
import cn.edu.xjtu.sysy.mir.node.Module;
import cn.edu.xjtu.sysy.mir.pass.analysis.CFGAnalysis;

import java.util.stream.Collectors;

public final class ModulePrinter {

    private ModulePrinter() { }

    public static void printModule(Module module) {
        System.out.println(toString(module));
    }

    public static String toString(Module module) {
        var sb = new StringBuilder();
        sb.append("=== Program Start ===\n");

        var cfg = new CFGAnalysis().process(module);
        for (var function : module.getFunctions()) {
            sb.append("function ").append(function.name)
                    .append('(').append(function.params.stream()
                    .map(p -> p.first() + ": " + p.second().type)
                    .collect(Collectors.joining(", "))).append("):\n");

            for (var block : cfg.getRPOBlocks(function)) {
                sb.append('^').append(block.shortName()).append('(')
                        .append(block.args.stream().map(BlockArgument::shortName)
                                .collect(Collectors.joining(", ")))
                        .append("):\n");
                for (var instruction : block.instructions) sb.append(instruction).append('\n');
                sb.append(block.terminator).append('\n');
            }
        }

        sb.append("=== Program End ===\n");
        return sb.toString();
    }

}
