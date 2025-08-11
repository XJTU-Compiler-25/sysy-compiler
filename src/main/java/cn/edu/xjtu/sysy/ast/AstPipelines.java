package cn.edu.xjtu.sysy.ast;

import cn.edu.xjtu.sysy.Pipeline;
import cn.edu.xjtu.sysy.ast.node.CompUnit;
import cn.edu.xjtu.sysy.ast.pass.ArrayNormalizer;
import cn.edu.xjtu.sysy.ast.pass.AstAnnotator;

public final class AstPipelines {

    private AstPipelines() {}

    public static final Pipeline<CompUnit> DEFAULT = Pipeline.builder(CompUnit.class)
            .addTransformers(AstAnnotator::new, ArrayNormalizer::new)
            .build();

}
