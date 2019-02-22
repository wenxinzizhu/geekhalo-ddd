package com.geekhalo.ddd.lite.domain.support.bitopt;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by taoli on 17/6/20.
 */
public class IntBitNotOp implements IntBitOpSQLSupport{
    private final IntBitOp intBitOp;

    public IntBitNotOp(IntBitOp intBitOp) {
        this.intBitOp = intBitOp;
    }

    @Override
    public boolean match(int value) {
        return !this.intBitOp.match(value);
    }

    @Override
    public String toSqlFilter(String fieldName) {
        if (intBitOp instanceof IntMaskOp){
            IntMaskOp intMaskOp = (IntMaskOp) intBitOp;
            return new StringBuilder()
                    .append("(")
                    .append(fieldName)
                    .append(" & ")
                    .append(intMaskOp.getMask())
                    .append(")")
                    .append("<>")
                    .append(intMaskOp.getMask())
                    .toString();

        }
        throw new NotImplementedException("no case");
    }
}
