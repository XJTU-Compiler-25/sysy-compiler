package cn.edu.xjtu.sysy.astvisitor;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import cn.edu.xjtu.sysy.SysYBaseVisitor;
import cn.edu.xjtu.sysy.SysYParser;
import cn.edu.xjtu.sysy.SysYParser.AddExpContext;
import cn.edu.xjtu.sysy.SysYParser.AssignmentStmtContext;
import cn.edu.xjtu.sysy.SysYParser.BlockStmtContext;
import cn.edu.xjtu.sysy.SysYParser.BreakStmtContext;
import cn.edu.xjtu.sysy.SysYParser.ConstExpContext;
import cn.edu.xjtu.sysy.SysYParser.ContinueStmtContext;
import cn.edu.xjtu.sysy.SysYParser.DeclContext;
import cn.edu.xjtu.sysy.SysYParser.EmptyStmtContext;
import cn.edu.xjtu.sysy.SysYParser.EqExpContext;
import cn.edu.xjtu.sysy.SysYParser.ExpContext;
import cn.edu.xjtu.sysy.SysYParser.ExpStmtContext;
import cn.edu.xjtu.sysy.SysYParser.FuncDefContext;
import cn.edu.xjtu.sysy.SysYParser.IfStmtContext;
import cn.edu.xjtu.sysy.SysYParser.LAndExpContext;
import cn.edu.xjtu.sysy.SysYParser.MulExpContext;
import cn.edu.xjtu.sysy.SysYParser.PrimaryExpContext;
import cn.edu.xjtu.sysy.SysYParser.RelExpContext;
import cn.edu.xjtu.sysy.SysYParser.ReturnStmtContext;
import cn.edu.xjtu.sysy.SysYParser.StmtContext;
import cn.edu.xjtu.sysy.SysYParser.UnaryExpContext;
import cn.edu.xjtu.sysy.SysYParser.UnaryOpContext;
import cn.edu.xjtu.sysy.SysYParser.WhileStmtContext;
import cn.edu.xjtu.sysy.astnodes.ArrayInitVal;
import cn.edu.xjtu.sysy.astnodes.AssignStmt;
import cn.edu.xjtu.sysy.astnodes.BType;
import cn.edu.xjtu.sysy.astnodes.BinaryExpr;
import cn.edu.xjtu.sysy.astnodes.Block;
import cn.edu.xjtu.sysy.astnodes.BlockStmt;
import cn.edu.xjtu.sysy.astnodes.BreakStmt;
import cn.edu.xjtu.sysy.astnodes.CallExpr;
import cn.edu.xjtu.sysy.astnodes.CompUnit;
import cn.edu.xjtu.sysy.astnodes.ContinueStmt;
import cn.edu.xjtu.sysy.astnodes.Expr;
import cn.edu.xjtu.sysy.astnodes.ExprInitVal;
import cn.edu.xjtu.sysy.astnodes.ExprStmt;
import cn.edu.xjtu.sysy.astnodes.FuncDef;
import cn.edu.xjtu.sysy.astnodes.FuncParam;
import cn.edu.xjtu.sysy.astnodes.Ident;
import cn.edu.xjtu.sysy.astnodes.IfStmt;
import cn.edu.xjtu.sysy.astnodes.IndexExpr;
import cn.edu.xjtu.sysy.astnodes.InitVal;
import cn.edu.xjtu.sysy.astnodes.Node;
import cn.edu.xjtu.sysy.astnodes.ReturnStmt;
import cn.edu.xjtu.sysy.astnodes.Stmt;
import cn.edu.xjtu.sysy.astnodes.UnaryExpr;
import cn.edu.xjtu.sysy.astnodes.VarDecl;
import cn.edu.xjtu.sysy.astnodes.VarDef;
import cn.edu.xjtu.sysy.astnodes.WhileStmt;
import cn.edu.xjtu.sysy.util.Assertions;

public class BuildAstVisitor extends SysYBaseVisitor<Node> {

    @Override
    public CompUnit visitCompUnit(SysYParser.CompUnitContext ctx) {

        CompUnit compUnit = new CompUnit(ctx.getStart(), ctx.getStop());

        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);

            switch (node) {
                case DeclContext decl -> compUnit.addDeclaration(visitDecl(decl));
                case FuncDefContext funcDef -> compUnit.addDeclaration(visitFuncDef(funcDef));
                default -> {}
            }
        }
        return compUnit;
    }

    @Override
    public VarDecl visitDecl(SysYParser.DeclContext ctx) {
        return (VarDecl) visitChildren(ctx);
    }

    @Override
    public BType visitBType(SysYParser.BTypeContext ctx) {
        Assertions.check(
                ctx.getChildCount() == 1,
                "Parser should ensure BType only contains 'float' or 'int'");

        return new BType(ctx.getStart(), ctx.getStop(), ctx.getChild(0).getText());
    }

    @Override
    public VarDecl visitConstDecl(SysYParser.ConstDeclContext ctx) {
        BType type = visitBType(ctx.bType());
        List<VarDef> defs = new ArrayList<>();
        ctx.constDef()
                .forEach(
                        e -> {
                            defs.add(visitConstDef(e));
                        });
        VarDecl constDecl = new VarDecl(ctx.getStart(), ctx.getStop(), true, type, defs);
        return constDecl;
    }

    @Override
    public VarDef visitConstDef(SysYParser.ConstDefContext ctx) {
        TerminalNode idNode = ctx.Ident();
        Ident id = new Ident(idNode.getSymbol(), idNode.getSymbol(), idNode.getText());
        List<Expr> dimensions = new ArrayList<>();
        ctx.constExp()
                .forEach(
                        e -> {
                            dimensions.add(visitConstExp(e));
                        });
        InitVal initVal = visitConstInitVal(ctx.constInitVal());
        VarDef constDef = new VarDef(ctx.getStart(), ctx.getStop(), id, dimensions, initVal);
        return constDef;
    }

    @Override
    public InitVal visitConstInitVal(SysYParser.ConstInitValContext ctx) {
        ConstExpContext exp = ctx.constExp();
        if (exp != null) {
            return new ExprInitVal(ctx.getStart(), ctx.getStop(), visitConstExp(exp));
        }
        List<InitVal> elements = new ArrayList<>();
        ctx.constInitVal()
                .forEach(
                        e -> {
                            elements.add(visitConstInitVal(e));
                        });
        return new ArrayInitVal(ctx.getStart(), ctx.getStop(), elements);
    }

    @Override
    public VarDecl visitVarDecl(SysYParser.VarDeclContext ctx) {
        BType type = visitBType(ctx.bType());
        List<VarDef> defs = new ArrayList<>();
        ctx.varDef()
                .forEach(
                        e -> {
                            defs.add(visitVarDef(e));
                        });
        VarDecl varDecl = new VarDecl(ctx.getStart(), ctx.getStop(), false, type, defs);
        return varDecl;
    }

    @Override
    public VarDef visitVarDef(SysYParser.VarDefContext ctx) {
        TerminalNode idNode = ctx.Ident();
        Ident id = new Ident(idNode.getSymbol(), idNode.getSymbol(), idNode.getText());
        List<Expr> dimensions = new ArrayList<>();
        ctx.constExp()
                .forEach(
                        e -> {
                            dimensions.add(visitConstExp(e));
                        });
        InitVal initVal = visitInitVal(ctx.initVal());
        VarDef varDef = new VarDef(ctx.getStart(), ctx.getStop(), id, dimensions, initVal);
        return varDef;
    }

    @Override
    public InitVal visitInitVal(SysYParser.InitValContext ctx) {
        if (ctx == null) return null;
        ExpContext exp = ctx.exp();
        if (exp != null) {
            return new ExprInitVal(ctx.getStart(), ctx.getStop(), visitExp(exp));
        }
        List<InitVal> elements = new ArrayList<>();
        ctx.initVal()
                .forEach(
                        e -> {
                            elements.add(visitInitVal(e));
                        });
        return new ArrayInitVal(ctx.getStart(), ctx.getStop(), elements);
    }

    @Override
    public FuncDef visitFuncDef(SysYParser.FuncDefContext ctx) {
        TerminalNode idNode = ctx.Ident();
        Ident id = new Ident(idNode.getSymbol(), idNode.getSymbol(), idNode.getText());
        BType funcType = visitFuncType(ctx.funcType());
        Block block = visitBlock(ctx.block());
        List<FuncParam> params = new ArrayList<>();
        ctx.funcFParam()
                .forEach(
                        e -> {
                            params.add(visitFuncFParam(e));
                        });
        FuncDef funcDef = new FuncDef(ctx.getStart(), ctx.getStop(), params, id, funcType, block);
        return funcDef;
    }

    @Override
    public BType visitFuncType(SysYParser.FuncTypeContext ctx) {
        Assertions.check(
                ctx.getChildCount() == 1,
                "Parser should ensure BType only 'void' 'float' or 'int'");

        return new BType(ctx.getStart(), ctx.getStop(), ctx.getChild(0).getText());
    }

    @Override
    public FuncParam visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        TerminalNode idNode = ctx.Ident();
        Ident id = new Ident(idNode.getSymbol(), idNode.getSymbol(), idNode.getText());
        BType type = visitBType(ctx.bType());
        List<Expr> dimensions = new ArrayList<>();
        ctx.exp()
                .forEach(
                        e -> {
                            dimensions.add(visitExp(e));
                        });
        FuncParam funcParam = new FuncParam(ctx.getStart(), ctx.getStop(), id, type, dimensions);
        return funcParam;
    }

    @Override
    public Block visitBlock(SysYParser.BlockContext ctx) {
        Block block = new Block(ctx.getStart(), ctx.getStop());
        ctx.blockItem()
                .forEach(
                        e -> {
                            Node node = visitBlockItem(e);
                            if (node != null) {
                                block.addBlockItem(node);
                            }
                        });
        return block;
    }

    @Override
    public Node visitBlockItem(SysYParser.BlockItemContext ctx) {
        return visitChildren(ctx);
    }

    public Stmt visitStmt(SysYParser.StmtContext ctx) {
        return switch (ctx) {
            case EmptyStmtContext ectx -> visitEmptyStmt(ectx);
            case AssignmentStmtContext actx -> visitAssignmentStmt(actx);
            case ExpStmtContext exctx -> visitExpStmt(exctx);
            case BlockStmtContext bctx -> visitBlockStmt(bctx);
            case IfStmtContext ictx -> visitIfStmt(ictx);
            case WhileStmtContext wctx -> visitWhileStmt(wctx);
            case BreakStmtContext brctx -> visitBreakStmt(brctx);
            case ContinueStmtContext cctx -> visitContinueStmt(cctx);
            case ReturnStmtContext rctx -> visitReturnStmt(rctx);
            default -> throw new RuntimeException("Not implemented Stmt type");
        };
    }

    @Override
    public Stmt visitEmptyStmt(SysYParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override
    public AssignStmt visitAssignmentStmt(SysYParser.AssignmentStmtContext ctx) {
        Expr lval = visitLVal(ctx.lVal());
        Expr value = visitExp(ctx.exp());
        AssignStmt stmt = new AssignStmt(ctx.getStart(), ctx.getStop(), lval, value);
        return stmt;
    }

    @Override
    public ExprStmt visitExpStmt(SysYParser.ExpStmtContext ctx) {
        Expr expr = visitExp(ctx.exp());
        return new ExprStmt(ctx.getStart(), ctx.getStop(), expr);
    }

    @Override
    public BlockStmt visitBlockStmt(SysYParser.BlockStmtContext ctx) {
        Block block = visitBlock(ctx.block());
        return new BlockStmt(ctx.getStart(), ctx.getStop(), block);
    }

    @Override
    public IfStmt visitIfStmt(SysYParser.IfStmtContext ctx) {
        List<StmtContext> stmts = ctx.stmt();
        Assertions.check(
                stmts.size() <= 2, "Parser should ensure only thenStmt and elseStmt exists");

        Expr cond = visitCond(ctx.cond());

        Stmt thenStmt = visitStmt(stmts.get(0));
        Stmt elseStmt = null;
        if (stmts.size() == 2) {
            elseStmt = visitStmt(stmts.get(1));
        }

        return new IfStmt(ctx.getStart(), ctx.getStop(), cond, thenStmt, elseStmt);
    }

    @Override
    public WhileStmt visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        Expr cond = visitCond(ctx.cond());
        Stmt bodyStmt = visitStmt(ctx.stmt());

        return new WhileStmt(ctx.getStart(), ctx.getStop(), cond, bodyStmt);
    }

    @Override
    public BreakStmt visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        return new BreakStmt(ctx.getStart(), ctx.getStop());
    }

    @Override
    public ContinueStmt visitContinueStmt(SysYParser.ContinueStmtContext ctx) {
        return new ContinueStmt(ctx.getStart(), ctx.getStop());
    }

    @Override
    public ReturnStmt visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        ExpContext valueContext = ctx.exp();
        Expr value = null;
        if (valueContext != null) {
            value = visitExp(valueContext);
        }
        return new ReturnStmt(ctx.getStart(), ctx.getStop(), value);
    }

    @Override
    public Expr visitExp(SysYParser.ExpContext ctx) {
        return (Expr) visitChildren(ctx);
    }

    @Override
    public Expr visitCond(SysYParser.CondContext ctx) {
        return (Expr) visitChildren(ctx);
    }

    public Ident visitIdent(TerminalNode node) {
        return new Ident(node.getSymbol(), node.getSymbol(), node.getText());
    }

    @Override
    public Expr visitLVal(SysYParser.LValContext ctx) {
        Ident id = visitIdent(ctx.Ident());
        List<ExpContext> exps = ctx.exp();
        List<Expr> dimensions = new ArrayList<>();
        exps.forEach(
                e -> {
                    dimensions.add(visitExp(e));
                });
        if (dimensions.isEmpty()) {
            return id;
        }
        return new IndexExpr(ctx.getStart(), ctx.getStop(), id, dimensions);
    }

    @Override
    public Expr visitPrimaryExp(SysYParser.PrimaryExpContext ctx) {
        return (Expr) visitChildren(ctx);
    }

    @Override
    public Expr visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        PrimaryExpContext pexp = ctx.primaryExp();
        if (pexp != null) {
            return visitPrimaryExp(pexp);
        }

        UnaryOpContext op = ctx.unaryOp();
        UnaryExpContext exp = ctx.unaryExp();
        if (op != null && exp != null) {
            return new UnaryExpr(ctx.getStart(), ctx.getStop(), op.getText(), visitUnaryExp(exp));
        }

        Ident id = visitIdent(ctx.Ident());
        List<Expr> args = new ArrayList<>();
        ctx.exp()
                .forEach(
                        e -> {
                            args.add(visitExp(e));
                        });
        return new CallExpr(ctx.getStart(), ctx.getStop(), id, args);
    }

    /* 
    @Override
    public Node visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
        return visitChildren(ctx);
    }
    */

    @Override
    public Expr visitMulExp(SysYParser.MulExpContext ctx) {
        Expr left = null;
        UnaryExpContext leftContext = null;
        String op = null;
        Expr right = null; 
        UnaryExpContext rightContext = null;
        
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);
            switch (node) {
                case UnaryExpContext exp -> {
                    left = right;
                    leftContext = rightContext;
                    right = visitUnaryExp(exp);
                    rightContext = exp;
                    if (leftContext != null && op != null) {
                        right = new BinaryExpr(leftContext.getStart(), rightContext.getStop(), left, op, right);
                    }
                }
                default -> {
                    op = node.getText();
                }
            }
        }
        return right;
    }

    @Override
    public Expr visitAddExp(SysYParser.AddExpContext ctx) {
        Expr left = null;
        MulExpContext leftContext = null;
        String op = null;
        Expr right = null; 
        MulExpContext rightContext = null;
        
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);
            switch (node) {
                case MulExpContext exp -> {
                    left = right;
                    leftContext = rightContext;
                    right = visitMulExp(exp);
                    rightContext = exp;
                    if (leftContext != null && op != null) {
                        right = new BinaryExpr(leftContext.getStart(), rightContext.getStop(), left, op, right);
                    }
                }
                default -> {
                    op = node.getText();
                }
            }
        }
        return right;
    }

    @Override
    public Expr visitRelExp(SysYParser.RelExpContext ctx) {
        Expr left = null;
        AddExpContext leftContext = null;
        String op = null;
        Expr right = null; 
        AddExpContext rightContext = null;
        
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);
            switch (node) {
                case AddExpContext exp -> {
                    left = right;
                    leftContext = rightContext;
                    right = visitAddExp(exp);
                    rightContext = exp;
                    if (leftContext != null && op != null) {
                        right = new BinaryExpr(leftContext.getStart(), rightContext.getStop(), left, op, right);
                    }
                }
                default -> {
                    op = node.getText();
                }
            }
        }
        return right;
    }

    @Override
    public Expr visitEqExp(SysYParser.EqExpContext ctx) {
        Expr left = null;
        RelExpContext leftContext = null;
        String op = null;
        Expr right = null; 
        RelExpContext rightContext = null;
        
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);
            switch (node) {
                case RelExpContext exp -> {
                    left = right;
                    leftContext = rightContext;
                    right = visitRelExp(exp);
                    rightContext = exp;
                    if (leftContext != null && op != null) {
                        right = new BinaryExpr(leftContext.getStart(), rightContext.getStop(), left, op, right);
                    }
                }
                default -> {
                    op = node.getText();
                }
            }
        }
        return right;
    }

    @Override
    public Expr visitLAndExp(SysYParser.LAndExpContext ctx) {
        Expr left = null;
        EqExpContext leftContext = null;
        String op = null;
        Expr right = null; 
        EqExpContext rightContext = null;
        
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);
            switch (node) {
                case EqExpContext exp -> {
                    left = right;
                    leftContext = rightContext;
                    right = visitEqExp(exp);
                    rightContext = exp;
                    if (leftContext != null && op != null) {
                        right = new BinaryExpr(leftContext.getStart(), rightContext.getStop(), left, op, right);
                    }
                }
                default -> {
                    op = node.getText();
                }
            }
        }
        return right;
    }

    @Override
    public Node visitLOrExp(SysYParser.LOrExpContext ctx) {
        Expr left = null;
        LAndExpContext leftContext = null;
        String op = null;
        Expr right = null; 
        LAndExpContext rightContext = null;
        
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.getChild(i);
            switch (node) {
                case LAndExpContext exp -> {
                    left = right;
                    leftContext = rightContext;
                    right = visitLAndExp(exp);
                    rightContext = exp;
                    if (leftContext != null && op != null) {
                        right = new BinaryExpr(leftContext.getStart(), rightContext.getStop(), left, op, right);
                    }
                }
                default -> {
                    op = node.getText();
                }
            }
        }
        return right;
    }

    @Override
    public Expr visitConstExp(SysYParser.ConstExpContext ctx) {
        return (Expr) visitChildren(ctx);
    }
}
