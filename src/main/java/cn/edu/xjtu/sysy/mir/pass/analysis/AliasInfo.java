package cn.edu.xjtu.sysy.mir.pass.analysis;

import cn.edu.xjtu.sysy.mir.node.Value;

import java.util.Map;
import java.util.Set;

import static cn.edu.xjtu.sysy.mir.pass.analysis.AliasState.*;

public record AliasInfo(
        Map<Value, Set<Value>> mustAlias,
        Map<Value, Set<Value>> noAlias
) {
    public AliasState getAliasState(Value a, Value b) {
        if (mustAlias.getOrDefault(a, Set.of()).contains(b)) return MUST_ALIAS;
        if (noAlias.getOrDefault(a, Set.of()).contains(b)) return NO_ALIAS;
        return MAY_ALIAS;
    }

    public boolean isMustAlias(Value a, Value b) {
        return getAliasState(a, b) == MUST_ALIAS;
    }

    public boolean isNoAlias(Value a, Value b) {
        return getAliasState(a, b) == NO_ALIAS;
    }

    public boolean isMayAlias(Value a, Value b) {
        return isMustAlias(a, b) || getAliasState(a, b) == MAY_ALIAS;
    }

}
