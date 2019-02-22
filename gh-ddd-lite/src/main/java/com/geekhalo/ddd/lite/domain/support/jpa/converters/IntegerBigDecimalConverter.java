package com.geekhalo.ddd.lite.domain.support.jpa.converters;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

/**
 * Created by fw on 2018/4/10
 */
public class IntegerBigDecimalConverter implements AttributeConverter<Integer,BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Integer integer) {
        return BigDecimal.valueOf(integer);
    }

    @Override
    public Integer convertToEntityAttribute(BigDecimal bigDecimal) {
        return bigDecimal.intValue();
    }
}