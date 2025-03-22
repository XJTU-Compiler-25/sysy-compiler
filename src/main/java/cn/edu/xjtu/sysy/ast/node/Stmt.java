package cn.edu.xjtu.sysy.ast.node;

import cn.edu.xjtu.sysy.symbol.SymbolTable;
import org.antlr.v4.runtime.Token;

import java.util.List;

/** Stmts */
public abstract sealed class Stmt extends Node {
    private boolean isDead = false;

    public Stmt(Token start, Token end) {
        super(start, end);
    }

    public void dead() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    /** BlockStmt */
    public static final class Block extends Stmt {
        /** 语句块元素 */
        public List<Stmt> stmts;

        /** 符号表*/
        public SymbolTable.Local symbolTable;

        public Block(Token start, Token end, List<Stmt> stmts) {
            super(start, end);
            this.stmts = stmts;
        }
    }

    /** VarDefStmt */
    public static final class LocalVarDef extends Stmt {
        public List<Decl.VarDef> varDefs;

        public LocalVarDef(Token start, Token end, List<Decl.VarDef> varDefs) {
            super(start, end);
            this.varDefs = varDefs;
        }
    }

    /** AssignStmt assignableExp '=' exp ';' */
    public static final class Assign extends Stmt {
        /** 赋值表达式的左值 */
        public Expr.Assignable target;

        /** 右值 */
        public Expr value;

        public Assign(Token start, Token end, Expr.Assignable target, Expr value) {
            super(start, end);
            this.target = target;
            this.value = value;
        }
    }

    /** Return Stmts */
    public static final class Return extends Stmt {
        public Expr value;

        public Return(Token start, Token end, Expr value) {
            super(start, end);
            this.value = value;
        }
    }

    /** ExprStmt */
    public static final class ExprEval extends Stmt {
        public Expr expr;

        public ExprEval(Token start, Token end, Expr expr) {
            super(start, end);
            this.expr = expr;
        }
    }

    /** Stmts */
    public static final class If extends Stmt {
        public Expr cond;
        public Stmt thenStmt;
        public Stmt elseStmt;

        public If(Token start, Token end, Expr cond, Stmt thenStmt, Stmt elseStmt) {
            super(start, end);
            this.cond = cond;
            this.thenStmt = thenStmt;
            this.elseStmt = elseStmt;
        }
    }

    /** WhileStmt */
    public static final class While extends Stmt {
        public Expr cond;
        public Stmt body;

        public While(Token start, Token end, Expr cond, Stmt body) {
            super(start, end);
            this.cond = cond;
            this.body = body;
        }
    }

    /** ContinueStmt */
    public static final class Continue extends Stmt {
        public Continue(Token start, Token end) {
            super(start, end);
        }
    }

    /** BreakStmt */
    public static final class Break extends Stmt {
        public Break(Token start, Token end) {
            super(start, end);
        }
    }

    /** EmptyStmt */
    public static final class Empty extends Stmt {
        public Empty(Token start, Token end) {
            super(start, end);
        }

        public static final Stmt.Empty INSTANCE = new Stmt.Empty(null, null);
    }
}
