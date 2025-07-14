package cn.edu.xjtu.sysy.symbol;

import java.util.HashMap;
import java.util.Map;

public abstract sealed class SymbolTable {
    public abstract SymbolTable getParent();
    public abstract Symbol.VarSymbol resolve(String name);
    public abstract void declare(Symbol.VarSymbol var);

    public static final class Global extends SymbolTable {
        private final Map<String, Symbol> table = new HashMap<>();

        public Global() {
            this(true);
        }

        public Global(boolean declBuiltin) {
            if (declBuiltin) {
                declareFunc(BuiltinFunction.GETINT.symbol);
                declareFunc(BuiltinFunction.GETCH.symbol);
                declareFunc(BuiltinFunction.GETFLOAT.symbol);
                declareFunc(BuiltinFunction.GETARRAY.symbol);
                declareFunc(BuiltinFunction.GETFARRAY.symbol);
                declareFunc(BuiltinFunction.PUTINT.symbol);
                declareFunc(BuiltinFunction.PUTCH.symbol);
                declareFunc(BuiltinFunction.PUTFLOAT.symbol);
                declareFunc(BuiltinFunction.PUTARRAY.symbol);
                declareFunc(BuiltinFunction.PUTFARRAY.symbol);
                declareFunc(BuiltinFunction.STARTTIME.symbol);
                declareFunc(BuiltinFunction.STOPTIME.symbol);
            }
        }

        @Override
        public SymbolTable getParent() {
            return null;
        }

        @Override
        public Symbol.VarSymbol resolve(String name) {
            var var = table.get(name);
            if (var == null) throw new IllegalArgumentException("Undefined variable: " + name);
            else if (!(var instanceof Symbol.VarSymbol it)) throw new IllegalArgumentException("Symbol is not global variable: " + name);
            else return it;
        }

        @Override
        public void declare(Symbol.VarSymbol var) {
            var key = var.name;
            if (table.containsKey(key)) throw new IllegalArgumentException("Global variable redefinition: " + key);
            else table.put(key, var);
        }

        public Symbol.FuncSymbol resolveFunc(String name) {
            var func = table.get(name);
            if (func == null) throw new IllegalArgumentException("Undefined function: " + name);
            else if (!(func instanceof Symbol.FuncSymbol it)) throw new IllegalArgumentException("Symbol is not function: " + name);
            else return it;
        }

        public void declareFunc(Symbol.FuncSymbol func) {
            var key = func.name;
            if (table.containsKey(key)) throw new IllegalArgumentException("Function redefinition: " + key);
            else table.put(key, func);
        }

        public void declareFunc(String key, Symbol.FuncSymbol func) {
            if (table.containsKey(key)) throw new IllegalArgumentException("Function redefinition: " + key);
            else table.put(key, func);
        }
    }

    public static final class Local extends SymbolTable {
        private final Map<String, Symbol.VarSymbol> table = new HashMap<>();
        public final SymbolTable parent;

        public Local(SymbolTable parent) {
            this.parent = parent;
        }

        @Override
        public SymbolTable getParent() {
            return parent;
        }

        @Override
        public Symbol.VarSymbol resolve(String name) {
            var var = table.get(name);
            if (var != null) return var;
            else return parent.resolve(name);
        }

        @Override
        public void declare(Symbol.VarSymbol var) {
            var key = var.name;
            if (table.containsKey(key)) throw new IllegalArgumentException("Local variable redefinition: " + key);
            else table.put(key, var);
        }

        /** 清除当前作用域的符号表的所有变量的声明状态
         *  需要在每次进入作用域的时候调用，否则无法正确处理变量作用域
         */
        public void clear() {
            for (var key : table.keySet()) {
                table.get(key).declared = false;
            }
        }
    }
}