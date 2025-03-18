package cn.edu.xjtu.sysy.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** SymbolTable
 * 针对不同的作用域使用层次结构，维护的符号表
 * SysY语言需要对函数和变量各自维护符号表。
 */
public class SymbolTable<T> {

    /** 本作用域符号表 */
    private final Map<String, T> table = new HashMap<>();
    /** 父作用域 若为顶层则为null */
    private final SymbolTable<T> parent;

    public SymbolTable(SymbolTable<T> parent) {
        this.parent = parent;
    }

    public SymbolTable() {
        this.parent = null;
    }

    /** 获取符号信息 */
    public T get(String name) {
        if (table.containsKey(name)) {
            return table.get(name);
        } else if (parent != null) {
            return parent.get(name);
        } else {
            return null;
        }
    }

    /** 增加新符号映射 */
    public SymbolTable<T> put(String name, T value) {
        table.put(name, value);
        return this;
    }

    /** 检查是否是本作用域及其父作用域的变量 */
    public boolean accesses(String name ) {
        if (table.containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.declares(name);
        } else {
            return false;
        }
    }

    /** 检查是否是本作用域的变量（不包含父作用域） */
    public boolean declares(String name) {
        return table.containsKey(name);
    }

    /** 获取本作用域全部符号名 */
    public Set<String> getDeclaredSymbols() {
        return table.keySet();
    }

    /** 获取父亲作用域符号表 */
    public SymbolTable<T> getParent() {
        return this.parent;
    }

}