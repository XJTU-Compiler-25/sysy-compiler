package cn.edu.xjtu.sysy.ast;

public enum Operator {
    ADD("+"), SUB("-"), MUL("*"), DIV("/"), MOD("%"),
    GT(">"), LT("<"), GE(">="), LE("<="),
    EQ("=="), NE("!="),
    AND("&&"), OR("||"), NOT("!"),
    ;

    public final String op;

    Operator(String op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return op;
    }

    public static Operator byString(String op) {
        return switch (op) {
            case "+" -> ADD;
            case "-" -> SUB;
            case "*" -> MUL;
            case "/" -> DIV;
            case "%" -> MOD;
            case ">" -> GT;
            case "<" -> LT;
            case ">=" -> GE;
            case "<=" -> LE;
            case "==" -> EQ;
            case "!=" -> NE;
            case "&&" -> AND;
            case "||" -> OR;
            case "!" -> NOT;
            default -> throw new RuntimeException("unknown operator: " + op);
        };
    }
}
