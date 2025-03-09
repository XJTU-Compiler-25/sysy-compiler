package cn.edu.xjtu.sysy.astvisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import cn.edu.xjtu.sysy.astnodes.ArrayExpr;
import cn.edu.xjtu.sysy.astnodes.AssignStmt;
import cn.edu.xjtu.sysy.astnodes.AssignableExpr;
import cn.edu.xjtu.sysy.astnodes.BinaryExpr;
import cn.edu.xjtu.sysy.astnodes.Block;
import cn.edu.xjtu.sysy.astnodes.BlockStmt;
import cn.edu.xjtu.sysy.astnodes.BreakStmt;
import cn.edu.xjtu.sysy.astnodes.CallExpr;
import cn.edu.xjtu.sysy.astnodes.CompUnit;
import cn.edu.xjtu.sysy.astnodes.ContinueStmt;
import cn.edu.xjtu.sysy.astnodes.Decl;
import cn.edu.xjtu.sysy.astnodes.Expr;
import cn.edu.xjtu.sysy.astnodes.ExprStmt;
import cn.edu.xjtu.sysy.astnodes.FloatLiteral;
import cn.edu.xjtu.sysy.astnodes.FuncDef;
import cn.edu.xjtu.sysy.astnodes.Ident;
import cn.edu.xjtu.sysy.astnodes.IfStmt;
import cn.edu.xjtu.sysy.astnodes.IndexExpr;
import cn.edu.xjtu.sysy.astnodes.IntLiteral;
import cn.edu.xjtu.sysy.astnodes.Node;
import cn.edu.xjtu.sysy.astnodes.Param;
import cn.edu.xjtu.sysy.astnodes.ReturnStmt;
import cn.edu.xjtu.sysy.astnodes.Stmt;
import cn.edu.xjtu.sysy.astnodes.TypeAnnotation;
import cn.edu.xjtu.sysy.astnodes.UnaryExpr;
import cn.edu.xjtu.sysy.astnodes.VarDef;
import cn.edu.xjtu.sysy.astnodes.VarDefStmt;
import cn.edu.xjtu.sysy.astnodes.VarDefs;
import cn.edu.xjtu.sysy.astnodes.WhileStmt;
import cn.edu.xjtu.sysy.parse.SysYBaseVisitor;
import cn.edu.xjtu.sysy.parse.SysYParser;
import cn.edu.xjtu.sysy.parse.SysYParser.AddExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.AndCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ArrayVarDefContext;
import cn.edu.xjtu.sysy.parse.SysYParser.AssignmentStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.BlockStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.BreakStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ContinueStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.EmptyStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.EqCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ExpCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ExpStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.FloatConstExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.FuncCallExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.FuncDefContext;
import cn.edu.xjtu.sysy.parse.SysYParser.IfStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.IntConstExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.MulExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.OrCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ParenExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.RelCondContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ReturnStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.ScalarVarDefContext;
import cn.edu.xjtu.sysy.parse.SysYParser.UnaryExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.VarAccessExpContext;
import cn.edu.xjtu.sysy.parse.SysYParser.VarDefStmtContext;
import cn.edu.xjtu.sysy.parse.SysYParser.VarDefsContext;
import cn.edu.xjtu.sysy.parse.SysYParser.WhileStmtContext;
import static cn.edu.xjtu.sysy.util.Assertions.unreachable;
import static cn.edu.xjtu.sysy.util.Todo.todo;

public class BuildAstVisitor extends SysYBaseVisitor<Node> {

    @Override
    public CompUnit visitCompUnit(SysYParser.CompUnitContext ctx) {
        if (ctx == null) {
            return null;
        }

        List<Decl> decls = new ArrayList<>();

        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);

            if (node instanceof VarDefsContext decl) {
                decls.add(visitVarDefs(decl));
            } else if (node instanceof FuncDefContext funcDef) {
                decls.add(visitFuncDef(funcDef));
            }
        }

        CompUnit compUnit = new CompUnit(ctx.getStart(), ctx.getStop(), decls);
        return compUnit;
    }

    @Override
    public VarDefs visitVarDefs(SysYParser.VarDefsContext ctx) {
        if (ctx == null) {
            return null;
        }

        List<VarDef> varDefs =
                ctx.varDef().stream().map(this::visitVarDef).collect(Collectors.toList());
        TypeAnnotation type = visitVarType(ctx.varType());
        return new VarDefs(ctx.getStart(), ctx.getStop(), ctx.Const() != null, type, varDefs);
    }

    public VarDef visitVarDef(SysYParser.VarDefContext ctx) {
        if (ctx == null) {
            return null;
        }

        if (ctx instanceof ScalarVarDefContext scalarVar) {
            return visitScalarVarDef(scalarVar);
        } else if (ctx instanceof ArrayVarDefContext arrayVar) {
            return visitArrayVarDef(arrayVar);
        }

        return unreachable();
    }

    @Override
    public VarDef visitScalarVarDef(SysYParser.ScalarVarDefContext ctx) {
        if (ctx == null) {
            return null;
        }

        Ident id = visitId(ctx.Id());
        Expr exp = visitExp(ctx.exp());
        return new VarDef(ctx.getStart(), ctx.getStop(), id, exp);
    }

    @Override
    public VarDef visitArrayVarDef(SysYParser.ArrayVarDefContext ctx) {
        if (ctx == null) {
            return null;
        }

        Ident id = visitId(ctx.Id());
        List<Expr> dimensions = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        Expr exp = visitArrayLiteralExp(ctx.arrayLiteralExp());
        if (exp == null) {
            exp = visitAssignable(ctx.assignableExp());
        }
        return new VarDef(ctx.getStart(), ctx.getStop(), id, dimensions, exp);
    }

    @Override
    public TypeAnnotation visitVarType(SysYParser.VarTypeContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new TypeAnnotation(ctx.getStart(), ctx.getStop(), ctx.getText());
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
     * ctx}.
     */
    @Override
    public FuncDef visitFuncDef(SysYParser.FuncDefContext ctx) {
        if (ctx == null) {
            return null;
        }

        List<Param> params =
                ctx.param().stream().map(this::visitParam).collect(Collectors.toList());
        Ident name = visitId(ctx.Id());
        TypeAnnotation retType = visitReturnableType(ctx.retType);
        Block body = visitBlock(ctx.body);
        return new FuncDef(ctx.getStart(), ctx.getStop(), params, name, retType, body);
    }

    @Override
    public TypeAnnotation visitReturnableType(SysYParser.ReturnableTypeContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new TypeAnnotation(ctx.getStart(), ctx.getStop(), ctx.getText());
    }

    @Override
    public Param visitParam(SysYParser.ParamContext ctx) {
        if (ctx == null) {
            return null;
        }

        Ident id = visitId(ctx.Id());
        TypeAnnotation type = visitVarType(ctx.varType());
        List<Expr> dimensions = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new Param(ctx.getStart(), ctx.getStop(), id, type, dimensions);
    }

    @Override
    public Block visitBlock(SysYParser.BlockContext ctx) {
        if (ctx == null) {
            return null;
        }

        List<Stmt> stmts = ctx.stmt().stream().map(this::visitStmt).collect(Collectors.toList());
        return new Block(ctx.getStart(), ctx.getStop(), stmts);
    }

    public Stmt visitStmt(SysYParser.StmtContext ctx) {
        if (ctx == null) {
            return null;
        }
        
        if (ctx instanceof EmptyStmtContext it) {
            return visitEmptyStmt(it);
        } else if (ctx instanceof VarDefStmtContext it) {
            return visitVarDefStmt(it);
        } else if (ctx instanceof AssignmentStmtContext it) {
            return visitAssignmentStmt(it);
        } else if (ctx instanceof ExpStmtContext it) {
            return visitExpStmt(it);
        } else if (ctx instanceof BlockStmtContext it) {
            return visitBlockStmt(it);
        } else if (ctx instanceof IfStmtContext it) {
            return visitIfStmt(it);
        } else if (ctx instanceof WhileStmtContext it) {
            return visitWhileStmt(it);
        } else if (ctx instanceof BreakStmtContext it) {
            return visitBreakStmt(it);
        } else if (ctx instanceof ContinueStmtContext it) {
            return visitContinueStmt(it);
        } else if (ctx instanceof ReturnStmtContext it) {
            return visitReturnStmt(it);
        }

        return unreachable();
    }

    @Override
    public Stmt visitEmptyStmt(SysYParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override
    public VarDefStmt visitVarDefStmt(SysYParser.VarDefStmtContext ctx) {
        if (ctx == null) {
            return null;
        }
        
        VarDefs varDefs = visitVarDefs(ctx.varDefs());
        return new VarDefStmt(ctx.getStart(), ctx.getStop(), varDefs);
    }

    @Override
    public AssignStmt visitAssignmentStmt(SysYParser.AssignmentStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        AssignableExpr target = visitAssignable(ctx.assignableExp());
        Expr value = visitExp(ctx.exp());
        return new AssignStmt(ctx.getStart(), ctx.getStop(), target, value);
    }

    @Override
    public ExprStmt visitExpStmt(SysYParser.ExpStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr expr = visitExp(ctx.exp());
        return new ExprStmt(ctx.getStart(), ctx.getStop(), expr);
    }

    @Override
    public BlockStmt visitBlockStmt(SysYParser.BlockStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        Block block = visitBlock(ctx.block());
        return new BlockStmt(ctx.getStart(), ctx.getStop(), block);
    }

    @Override
    public IfStmt visitIfStmt(SysYParser.IfStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr cond = visitCond(ctx.cond());

        Stmt thenStmt = visitStmt(ctx.stmt(0));
        Stmt elseStmt = visitStmt(ctx.stmt(1));
        return new IfStmt(ctx.getStart(), ctx.getStop(), cond, thenStmt, elseStmt);
    }

    @Override
    public WhileStmt visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr cond = visitCond(ctx.cond());
        Stmt bodyStmt = visitStmt(ctx.stmt());
        return new WhileStmt(ctx.getStart(), ctx.getStop(), cond, bodyStmt);
    }

    @Override
    public BreakStmt visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new BreakStmt(ctx.getStart(), ctx.getStop());
    }

    @Override
    public ContinueStmt visitContinueStmt(SysYParser.ContinueStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new ContinueStmt(ctx.getStart(), ctx.getStop());
    }

    @Override
    public ReturnStmt visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr value = visitExp(ctx.exp());
        return new ReturnStmt(ctx.getStart(), ctx.getStop(), value);
    }

    public Expr visitCond(SysYParser.CondContext ctx) {
        if (ctx == null) {
            return null;
        }

        if (ctx instanceof ExpCondContext it) {
            return visitExpCond(it);
        } else if (ctx instanceof OrCondContext it) {
            return visitOrCond(it);
        } else if (ctx instanceof RelCondContext it) {
            return visitRelCond(it);
        } else if (ctx instanceof AndCondContext it) {
            return visitAndCond(it);
        } else if (ctx instanceof EqCondContext it) {
            return visitEqCond(it);
        }

        // java 17 没法用 switch 模式匹配，检查不了穷尽性...
        // 确实（悲
        return unreachable();
    }

    @Override
    public BinaryExpr visitOrCond(SysYParser.OrCondContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr left = visitCond(ctx.cond(0));
        Expr right = visitCond(ctx.cond(1));
        return new BinaryExpr(ctx.getStart(), ctx.getStop(), left, "||", right);
    }

    @Override
    public Expr visitExpCond(SysYParser.ExpCondContext ctx) {
        if (ctx == null) {
            return null;
        }

        return visitExp(ctx.exp());
    }

    @Override
    public BinaryExpr visitRelCond(SysYParser.RelCondContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr left = visitCond(ctx.cond(0));
        Expr right = visitCond(ctx.cond(1));
        return new BinaryExpr(ctx.getStart(), ctx.getStop(), left, ctx.op.getText(), right);
    }

    @Override
    public BinaryExpr visitAndCond(SysYParser.AndCondContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr left = visitCond(ctx.cond(0));
        Expr right = visitCond(ctx.cond(1));
        return new BinaryExpr(ctx.getStart(), ctx.getStop(), left, "&&", right);
    }

    @Override
    public BinaryExpr visitEqCond(SysYParser.EqCondContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr left = visitCond(ctx.cond(0));
        Expr right = visitCond(ctx.cond(1));
        return new BinaryExpr(ctx.getStart(), ctx.getStop(), left, ctx.op.getText(), right);
    }

    public Expr visitExp(SysYParser.ExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        if (ctx instanceof ParenExpContext it) {
            return visitParenExp(it);
        } else if (ctx instanceof IntConstExpContext it) {
            return visitIntConstExp(it);
        } else if (ctx instanceof FloatConstExpContext it) {
            return visitFloatConstExp(it);
        } else if (ctx instanceof VarAccessExpContext it) {
            return visitVarAccessExp(it);
        } else if (ctx instanceof UnaryExpContext it) {
            return visitUnaryExp(it);
        } else if (ctx instanceof FuncCallExpContext it) {
            return visitFuncCallExp(it);
        } else if (ctx instanceof MulExpContext it) {
            return visitMulExp(it);
        } else if (ctx instanceof AddExpContext it) {
            return visitAddExp(it);
        }
        return unreachable();
    }

    @Override
    public FloatLiteral visitFloatConstExp(SysYParser.FloatConstExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new FloatLiteral(
                ctx.getStart(), ctx.getStop(), Float.parseFloat(ctx.FloatLiteral().getText()));
    }

    @Override
    public UnaryExpr visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr operand = visitExp(ctx.exp());
        return new UnaryExpr(ctx.getStart(), ctx.getStop(), ctx.op.getText(), operand);
    }

    @Override
    public Expr visitParenExp(SysYParser.ParenExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        return visitExp(ctx.exp());
    }

    @Override
    public IntLiteral visitIntConstExp(SysYParser.IntConstExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new IntLiteral(
                ctx.getStart(), ctx.getStop(), Integer.parseInt(ctx.IntLiteral().getText()));
    }

    @Override
    public BinaryExpr visitAddExp(SysYParser.AddExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr left = visitExp(ctx.exp(0));
        Expr right = visitExp(ctx.exp(1));
        return new BinaryExpr(ctx.getStart(), ctx.getStop(), left, ctx.op.getText(), right);
    }

    @Override
    public BinaryExpr visitMulExp(SysYParser.MulExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        Expr left = visitExp(ctx.exp(0));
        Expr right = visitExp(ctx.exp(1));
        return new BinaryExpr(ctx.getStart(), ctx.getStop(), left, ctx.op.getText(), right);
    }

    @Override
    public CallExpr visitFuncCallExp(SysYParser.FuncCallExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        Ident function = visitId(ctx.Id());
        List<Expr> args = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new CallExpr(ctx.getStart(), ctx.getStop(), function, args);
    }

    @Override
    public AssignableExpr visitVarAccessExp(SysYParser.VarAccessExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        return visitAssignable(ctx.assignableExp());
    }

    public Ident visitId(TerminalNode ctx) {
        if (ctx == null) {
            return null;
        }

        return new Ident(ctx.getSymbol(), ctx.getSymbol(), ctx.getText());
    }

    public AssignableExpr visitAssignable(SysYParser.AssignableExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        if (ctx instanceof SysYParser.ScalarAssignableContext it) {
            return visitScalarAssignable(it);
        } else if (ctx instanceof SysYParser.ArrayAssignableContext it) {
            return visitArrayAssignable(it);
        }
        return unreachable();
    }

    @Override
    public Ident visitScalarAssignable(SysYParser.ScalarAssignableContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new Ident(ctx.getStart(), ctx.getStop(), ctx.getText());
    }

    @Override
    public IndexExpr visitArrayAssignable(SysYParser.ArrayAssignableContext ctx) {
        if (ctx == null) {
            return null;
        }

        Ident id = visitId(ctx.Id());
        List<Expr> indexes = ctx.exp().stream().map(this::visitExp).collect(Collectors.toList());
        return new IndexExpr(ctx.getStart(), ctx.getStop(), id, indexes);
    }

    public ArrayExpr visitArrayLiteralExp(SysYParser.ArrayLiteralExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        if (ctx instanceof SysYParser.ElementExpContext) {
            // report semantic error: scalar exp can't be assigned to an array.
            return todo();
        } else if (ctx instanceof SysYParser.ArrayExpContext it) {
            return visitArrayExp(it);
        }
        return unreachable();
    }

    public Expr visitArrayLiteralExpRecursive(SysYParser.ArrayLiteralExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        if (ctx instanceof SysYParser.ElementExpContext it) {
            return visitElementExp(it);
        } else if (ctx instanceof SysYParser.ArrayExpContext it) {
            return visitArrayExp(it);
        }
        return unreachable();
    }

    @Override
    public Expr visitElementExp(SysYParser.ElementExpContext ctx) {
        if (ctx == null) {
            return null;
        }

        return visitExp(ctx.exp());
    }

    @Override
    public ArrayExpr visitArrayExp(SysYParser.ArrayExpContext ctx) {
        if (ctx == null) {
            return null;
        }
        
        List<Expr> elements =
                ctx.arrayLiteralExp().stream()
                        .map(this::visitArrayLiteralExpRecursive)
                        .collect(Collectors.toList());
        return new ArrayExpr(ctx.getStart(), ctx.getStop(), elements);
    }
}
