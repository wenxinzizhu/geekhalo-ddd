package com.geekhalo.ddd.lite.domain.support.bitopt;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by taoli on 17/6/20.
 */
public class IntBitOrOp implements IntBitOpSQLSupport{
    private final List<IntBitOp> intBitOps = Lists.newArrayList();

    @Override
    public boolean match(int value) {
        if (CollectionUtils.isEmpty(this.intBitOps)){
            return true;
        }
        return this.intBitOps.stream().anyMatch(intBitOp -> intBitOp.match(value));
    }

    public IntBitOrOp or(IntBitOp intBitOp){
        this.intBitOps.add(intBitOp);
        return this;
    }

    @Override
    public String toSqlFilter(String fieldName) {
        if (CollectionUtils.isEmpty(this.intBitOps)){
            return "";
        }
        return this.intBitOps.stream()
                .map(intBitOp -> toFilter(intBitOp, fieldName))
                .collect(Collectors.joining(" or ","(",")"));
    }
}
