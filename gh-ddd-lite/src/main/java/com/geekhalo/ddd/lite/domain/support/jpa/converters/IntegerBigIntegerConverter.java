package com.geekhalo.ddd.lite.domain.support.jpa.converters;

import javax.persistence.AttributeConverter;
import java.math.BigInteger;

/**
 * Created by fw on 2018/4/10
 */
public class IntegerBigIntegerConverter implements AttributeConverter<Integer,BigInteger> {
    @Override
    public BigInteger convertToDatabaseColumn(Integer integer) {
        return BigInteger.valueOf(integer);
    }

    @Override
    public Integer convertToEntityAttribute(BigInteger bigInteger) {
        return bigInteger.intValue();
    }
}