package com.geekhalo.ddd.lite.domain.support.jpa.converters;

import javax.persistence.AttributeConverter;
import java.math.BigInteger;

/**
 * Created by fw on 2018/4/10
 */
public class LongBigIntegerConverter implements AttributeConverter<Long,BigInteger> {
    @Override
    public BigInteger convertToDatabaseColumn(Long aLong) {
        return BigInteger.valueOf(aLong);
    }

    @Override
    public Long convertToEntityAttribute(BigInteger bigInteger) {
        return bigInteger.longValue();
    }
}
