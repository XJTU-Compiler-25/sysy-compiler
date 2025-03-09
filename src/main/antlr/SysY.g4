grammar SysY;

options {
    language = Java;
}

@header {
package cn.edu.xjtu.sysy.parse;
}

/**
  * 原文 EBNF 文法：
  * 符号[...]表示方括号内包含的为可选项；
  * 符号{...}表示花括号内包含的为可重复0次或多次的项；
  * 终结符或者是单引号括起的串，或者是Ident、InstConst、floatConst这样的记号。

  * 编译单元 CompUnit → [ CompUnit ] ( Decl | FuncDef )

  * 声明 Decl → ConstDecl | VarDecl
  * 基本类型 BType → 'int' | 'float'

  * 常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
  * 常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
  * 常量初值 ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'

  * 变量声明 VarDecl → BType VarDef { ',' VarDef } ';'
  * 变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
  * 变量初值 InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'

  * 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
  * 函数类型 FuncType → 'void' | 'int' | 'float'
  * 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
  * 函数形参 FuncFParam → BType Ident ['[' ']' { '[' Exp ']' }]

  * 语句块 Block → '{' { BlockItem } '}'
  * 语句块项 BlockItem    → Decl | Stmt
  * 语句 Stmt
  *     → LVal '=' Exp ';'
  *      | [Exp] ';'
  *      | Block
  *      | 'if' '( Cond ')' Stmt [ 'else' Stmt ]
  *      | 'while' '(' Cond ')' Stmt
  *      | 'break' ';'
  *      | 'continue' ';'
  *      | 'return' [Exp] ';'
  * 表达式 Exp → AddExp // 注：SysY 表达式是 int/float 型
  * 条件表达式 Cond → LOrExp
  * 左值表达式 LVal → Ident {'[' Exp ']'}
  * 基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
  * 数值 Number → IntConst | floatConst
  * 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
  * 单目运算符 UnaryOp → '+' | '−' | '!' // 注：'!'仅出现在条件表达式中
  * 函数实参表 FuncRParams → Exp { ',' Exp }
  * 乘除模表达式 MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
  * 加减表达式 AddExp → MulExp | AddExp ('+' | '−') MulExp
  * 关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
  * 相等性表达式 EqExp → RelExp | EqExp ('==' | '!=') RelExp
  * 逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp
  * 逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp
  * 常量表达式 ConstExp → AddExp 注：使用的Ident必须是常量

  * 非终结符部分：
  * identifier  → identifier-nondigit
  *              | identifier identifier-nondigit
  *              | identifier digit

  * integer-const  → decimal-const | octal-const | hexadecimal-const
  * decimal-const → nonzero-digit | decimal-const digit
  * octal-const → 0 | octal-const octal-digit
  * hexadecimal-const -> hexadecimal-prefix hexadecimal-digit | hexadecimal-const hexadecimal-digit

  * 单行注释：以序列 ‘//’ 开始，直到换行符结束，不包括换行符。
  * 多行注释：以序列 ‘/*’ 开始，直到第一次出现 ‘* /’ 时结束，包括结束处 ‘* /’。


  */

// Parser rules (非终结符)

compUnit: (varDefs | funcDef)* EOF;

varDefs: Const? varType varDef (',' varDef)* ';';
varDef
    : Id ('=' exp)?                                              # scalarVarDef
    | Id ('[' exp ']')+ ('=' (arrayLiteralExp | assignableExp))? # arrayVarDef
    ;
varType: 'int' | 'float';

funcDef: retType=returnableType name=Id '(' (param (',' param)*)? ')' body=block;
returnableType: 'int' | 'float' | 'void';
param: varType Id ('[' ']' ('[' exp ']')*)?;

block: '{' stmt* '}';

stmt
    : ';'                                  # emptyStmt
    | varDefs                              # varDefStmt
    | assignableExp '=' exp ';'            # assignmentStmt
    | exp ';'                              # expStmt
    | block                                # blockStmt
    | If '(' cond ')' stmt ('else' stmt)?  # ifStmt
    | While '(' cond ')' stmt              # whileStmt
    | Break ';'                            # breakStmt
    | Continue ';'                         # continueStmt
    | Return exp? ';'                      # returnStmt
    ;

// SysY 语言中逻辑运算必须在 Cond 中
cond
    : exp                                    # expCond
    | cond op=('<' | '>' | '<=' | '>=') cond # relCond
    | cond op=('==' | '!=') cond             # eqCond
    | cond '&&' cond                         # andCond
    | cond '||' cond                         # orCond
    ;
exp
    : '(' exp ')'                          # parenExp
    | IntLiteral                           # intConstExp
    | FloatLiteral                         # floatConstExp
    | assignableExp                        # varAccessExp
    | op=('+' | '-' | '!') exp             # unaryExp
    | Id '(' (arg=exp (',' arg=exp)*)? ')' # funcCallExp
    | exp op=('*' | '/' | '%') exp         # mulExp
    | exp op=('+' | '-') exp               # addExp
    ;
assignableExp
    : Id                # scalarAssignable
    | Id ('[' exp ']')+ # arrayAssignable
    ;
arrayLiteralExp
    : exp                                                            # elementExp
    | '{' element=arrayLiteralExp (',' element=arrayLiteralExp)* '}' # arrayExp
    ;

// Lexer rules (终结符)

MultilineComment: '/*' .*? '*/' -> channel(HIDDEN);
LineComment: '//' ~[\r\n]* -> channel(HIDDEN);

// Whitespace
WS: [ \r\n\t]+ -> channel(HIDDEN);

// Keywords
Const: 'const';
Int: 'int';
Float: 'float';
Void: 'void';
If: 'if';
While: 'while';
Break: 'break';
Continue: 'continue';
Return: 'return';

fragment IdHead: [a-zA-Z_];
fragment IdPart: IdHead | [0-9];

Id: IdHead IdPart*;

fragment Digit: [0-9];
fragment DigitNonZero: [1-9];
fragment DecLiteral: '0' | DigitNonZero Digit*;

fragment HexDigit: [0-9a-fA-F];
fragment HexPrefix: '0' [xX];
fragment HexLiteral: HexPrefix HexDigit+;

fragment OctDigit: [0-7];
fragment OctPrefix: '0';
fragment OctLiteral: OctPrefix OctDigit+;

// 仅整数可以用其他进制表示
IntLiteral: DecLiteral | OctLiteral | HexLiteral;

FloatLiteral
    : Digit* '.' Digit+ ([eE] [+-]? Digit+)?
    | Digit+ [eE] [+-]? Digit+
    ;
