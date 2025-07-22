package cn.edu.xjtu.sysy.ast.pass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.ast.SemanticError;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.node.Decl;
import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Expr.Array;
import cn.edu.xjtu.sysy.ast.node.Node;
import cn.edu.xjtu.sysy.error.ErrManager;
import cn.edu.xjtu.sysy.symbol.Type;

/** 把 Array 正规化为只有 单值 或 数组 元素 */
public final class ArrayNormalizer extends AstVisitor {
    public ArrayNormalizer(Pipeline<CompUnit> pipeline) {
        super(pipeline);
    }

    @Override
    public void visit(Decl.VarDef node) {
        if (node.type instanceof Type.Array type) {
            int[] dimensions =  IntStream.range(0, type.dimensions.length)
                .map(i -> type.dimensions[type.dimensions.length - 1 - i])
                .toArray();
            var initExpr = node.init;
            if (initExpr instanceof Expr.RawArray array) {
                var checker = new ArrayChecker(dimensions);
                node.init = checker.normalize(array);
                for (var err : checker.errors) err(err);
            }
        }
    }

    @Override
    public void visit(Decl.FuncDef node) {
        // 形参也算是VarDef，跳过检查
        visit(node.body);
    }

    static class ArrayChecker {

        /** 数组维度 */
        private final int[] dimensions;

        /** 数组总长度 */
        private final int length;

        /** 当前填写到的元素 */
        private int index = 0;

        /** 显示初始化的元素位置，和elements一一对应 */
        private final List<Integer> indexes = new ArrayList<>();

        /** 显式初始化的元素列表 */
        private final List<Expr> elements = new ArrayList<>();

        private final List<SemanticError> errors = new ArrayList<>();

        public List<SemanticError> getErrors() {
            return errors;
        }

        private void err(Node node, String form, Object... args) {
            errors.add(new SemanticError(node, String.format(form, args)));
        }

        public ArrayChecker(int[] dimensions) {
            this.dimensions = dimensions;
            length = Arrays.stream(dimensions).reduce(1, (a, b) -> a * b);
        }

        /** 获取子维度 如[2][3][4] 在depth = 1的情况下-> [3][4] */
        private int[] getDimFromDepth(int depth) {
            return Arrays.copyOfRange(dimensions, depth, dimensions.length);
        }

        /** 获取当前数组表达式中第一个非数组表达式 */
        private Expr getNonArrayExpr(Expr.RawArray array) {
            if (array.elements.size() > 1) {
                err(array, "excess elements in scalar initializer");
            }
            var el = array.elements.get(0);
            return switch (el) {
                case Expr.RawArray arr -> getNonArrayExpr(arr);
                default -> el;
            };
        }

        /** 获取当前深度的子数组总长度。 */
        private int depToLen(int depth) {
            return Arrays.stream(getDimFromDepth(depth)).reduce(1, (a, b) -> a * b);
        }

        /**
         * 获取当前index对应的深度（1 <= depth <= dimensions.length) eg. [2][3][4] index = 0 -> depth = 0
         * [2][3][4] index = 1 -> depth = 3 [2][3][4] index = 4 -> depth = 2 [2][3][4] index = 12 ->
         * depth = 1
         */
        private int getDepth() {
            int depth = dimensions.length;
            while (depth > 1 && index % depToLen(depth - 1) == 0) {
                depth--;
            }
            return depth;
        }

        public void visit(Expr.RawArray array) {
            for (var el : array.elements) {
                /** 数组已满的情况 */
                if (index == length) {
                    err(array, "excess elements in array initializer");
                    return;
                }
                switch (el) {
                    /** 如果是子数组 */
                    case Expr.RawArray arr -> {
                        int depth = getDepth();
                        /** 如果已经到达最大深度 i.e. 目前位置应该填的是标量 */
                        if (depth == dimensions.length) {
                            err(arr, "braces around scalar initializer");
                            /** 目前C标准似乎只是报警告，将array的第一个标量元素取出，剩下忽略。 */
                            /** 若是空数组，则这个位置当作是隐式初始化的0 */
                            if (!arr.elements.isEmpty()) {
                                var expr = getNonArrayExpr(arr);
                                elements.add(expr);
                                indexes.add(index);
                            }
                            index++;
                            continue;
                        }
                        /** 没到达最大深度，可以填数组。 截取当前深度的子维度构造checker，递归进行检查 */
                        var checker = new ArrayChecker(getDimFromDepth(depth));
                        checker.visit(arr);
                        /** 将检查结果的所有元素插入列表 */
                        elements.addAll(checker.getElements());
                        errors.addAll(checker.errors);
                        /** 将检查结果的元素位置+index = 在大数组里的位置插入indexes */
                        for (var i : checker.getIndexes()) {
                            indexes.add(i + index);
                        }
                        /** 为目前的index += length。 也就是将子数组在展平后的父数组的位置区间填满。 */
                        index += checker.length();
                    }
                    /** 标量元素。 */
                    default -> {
                        elements.add(el);
                        indexes.add(index++);
                    }
                }
            }
        }

        private List<Integer> getIndexes() {
            return indexes;
        }

        private List<Expr> getElements() {
            return elements;
        }

        private int length() {
            return length;
        }

        public Array normalize(Expr.RawArray array) {
            visit(array);
            return new Array(array, elements, indexes);
        }
    }
}
