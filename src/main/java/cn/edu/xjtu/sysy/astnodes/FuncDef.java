package cn.edu.xjtu.sysy.astnodes;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;

/** Function Definition
 * FuncDef 表示函数定义。其中的 FuncType 指明返回类型。
a) 当返回类型为 int/float 时，函数内所有分支都应当含有带有 Exp 的 return
语句。不含有 return 语句的分支的返回值未定义。
b) 当返回值类型为 void 时，函数内只能出现不带返回值的 return 语句。
2. FuncDef 中形参列表（FuncFParams）的每个形参声明（FuncFParam）用于声
明 int/float 类型的参数，或者是元素类型为 int/float 的多维数组。FuncFParam
的语义参见前文。
 */
public final class FuncDef extends Decl {

    public List<Param> params;
    public final Ident name;
    public final TypeAnnotation retType;
    public Block body;

    public FuncDef(
            Token start, Token end, List<Param> params, Ident name, TypeAnnotation retType, Block body) {
        super(start, end);
        this.params = params;
        this.name = name;
        this.retType = retType;
        this.body = body;
    }

    @Override
    public String toString() {
        return "FuncDef [params=" + params + ", name=" + name + ", retType=" + retType + ", body=" + body
                + ", getLocation()=" + Arrays.toString(getLocation()) + "]";
    }
}
