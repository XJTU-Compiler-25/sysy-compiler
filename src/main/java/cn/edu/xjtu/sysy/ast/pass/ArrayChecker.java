package cn.edu.xjtu.sysy.ast.pass;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xjtu.sysy.ast.node.Expr;
import cn.edu.xjtu.sysy.ast.node.Node;
import cn.edu.xjtu.sysy.ast.node.SemanticError;

/** 检查数组初始化器合法性，并进行补0.
 *  大致思想：
 *  eg. a[3][3] = {1,2,3, {1,2}, 1,2,3};
 *  enter(depth = 1) -> { -> depth + 1 = 2 -> { -> depth + 1 > maxDep -> error
 *   ^   |                      |(这里已经=maxDep所以不用补‘{’)
 *   |   v                      v
 *   |   Number,进行补{（也就是封装），depth = maxDepth ({1,2,... -> {{1,2,... )
 *   |   |
 *   |   v
 *   |   持续输入Number，凑够dim[depth] = 3个元素 -> { -> depth + 1 > maxDep -> error
 *   |   |
 *   |   v
 *   - 补充‘}’，depth-1 = 1
 */
public class ArrayChecker {
    /** 对于每一个维度，均初始化一个定长数组，用于每一维的初始化 */
    public List<List<Expr>> initList = new ArrayList<>();

    /** 数组维度 */
    int[] dimensions;

    /** 当前深度（状态机标识符） */
    int depth;

    private final List<SemanticError> errors = new ArrayList<>();

    public List<SemanticError> getErrors() {
        return errors;
    }

    private void err(Node node, String form, Object... args) {
        errors.add(new SemanticError(node, String.format(form, args)));
    }

    public ArrayChecker(int[] dimensions) {
        this.dimensions = dimensions;
        for (int i = 0; i < dimensions.length; i++) {
            initList.add(new ArrayList<>());
        }
        depth = 1;
    }

    /** 检查对应深度的数组长度，若超出对应维度的初始化量，则报错 */
    private boolean checkSize(int depth, Node node) {
        if (initList.get(depth - 1).size() == dimensions[depth - 1]) {
            err(node, "excess elements in array initializer");
            return false;
        }
        return true;
    }

    /** 当当前维度的数组已满时，将当前维度的元素合并为一个数组表达式，添加到上一级维度中，
     * 并清空当前维度的列表。同时递归地向上检查和合并，直到达到指定的最小深度或者当前维度未满。 */
    private void down(int minDepth, Node node) {
        while (initList.get(depth - 1).size() == dimensions[depth - 1] && depth > minDepth) {
            depth--;
            List<Expr> ts = new ArrayList<>(initList.get(depth));
            if (!checkSize(1, node)) {
                return;
            }
            initList.get(depth - 1).add(new Expr.Array(null, null, ts));
            initList.get(depth).clear();
        }
    }

    /** 此方法用于补充缺失的元素为0。它会从最内层维度开始，补充足够的0元素直到填满该维度，
     * 然后通过down方法将这些元素合并到上一级维度，并重复这个过程，直到从指定的最小深度开始的所有维度都被填满。*/
    private void paddingZero(int minDepth, Node node) {
        while (initList.get(minDepth - 1).size() < dimensions[minDepth - 1]) {
            depth = dimensions.length;
            while (initList.get(depth - 1).size() < dimensions[depth - 1]) {
                initList.get(depth - 1).add(new Expr.IntLiteral(null, null, 0));
            }
            down(minDepth, node);
        }
    }

    /** 用于检查并添加单个表达式到当前深度的列表中，然后调用down方法尝试合并。 */
    private void check(Expr expr) {
        if (!checkSize(depth, expr)) {
            return;
        }
        initList.get(depth - 1).add(expr);
        down(1, expr);
    }
    
    /** 用于递归检查数组表达式。遍历数组的所有元素，如果元素是子数组，则增加深度并递归检查；
     * 如果是普通元素，则设置深度为最大值并调用单元素的check方法。
     * 如果在处理子数组后发现当前层级还有元素，则调用paddingZero来补充缺失的元素。 */
    private void check(Expr.Array expr) {
        for (Expr t : expr.elements) {
            if (t instanceof Expr.Array aExp) {
                if (depth == dimensions.length) {
                    err(expr, "braces around scalar initializer");
                    if (aExp.elements.size() > 1) {
                        err(aExp, "excess elements in scalar initializer");
                    }
                    return;
                }
                depth++;
                int tmp = depth;
                check(aExp);
                if (!initList.get(tmp - 1).isEmpty()) {
                    paddingZero(tmp, expr);
                }
                down(1, expr);
            } else {
                depth = dimensions.length;
                check(t);
            }
        }
    }

    /** 整个类的主要入口。它首先检查提供的数组表达式，然后补充缺失的元素，
     * 最后检查是否所有内部维度都被正确处理（都应该为空），并返回处理后的数组表达式。 */
    public Expr.Array generateRealValue(Expr.Array expr) {
        check(expr);
        paddingZero(1, expr);
        for (int i = 1; i < dimensions.length; i++) {
            if (!initList.get(i).isEmpty()) {
                err(expr, "excess elements in array initializer");
                break;
            }
        }
        return new Expr.Array(null, null, initList.get(0));
    }
}
