package com.geekhalo.ddd.lite.domain.support.bitopt;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by taoli on 17/6/20.
 */
public interface IntBitOpSQLSupport extends IntBitOp{
    String toSqlFilter(String fieldName);

    default String toFilter(IntBitOp intBitOp, String field){
        if (intBitOp instanceof IntBitOpSQLSupport){
            return ((IntBitOpSQLSupport) intBitOp).toSqlFilter(field);
        }
        if (intBitOp instanceof IntMaskOp){
            IntMaskOp intMaskOp = (IntMaskOp) intBitOp;
            return new StringBuilder()
                    .append("(")
                    .append(field)
                    .append(" & ")
                    .append(intMaskOp.getMask())
                    .append(")")
                    .append("=")
                    .append(intMaskOp.getMask())
                    .toString();
        }
        throw new NotImplementedException("no case");
    }
}
