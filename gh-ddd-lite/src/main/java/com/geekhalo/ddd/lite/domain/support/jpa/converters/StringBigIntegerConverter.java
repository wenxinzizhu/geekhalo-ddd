package com.geekhalo.ddd.lite.domain.support.jpa.converters;

import javax.persistence.AttributeConverter;
import java.math.BigInteger;

/**
 * Created by fw on 2018/4/9
 */
public class StringBigIntegerConverter implements AttributeConverter<String,BigInteger> {

        @Override
        public BigInteger convertToDatabaseColumn(String string) {
            return string == null ? null : BigInteger.valueOf(Long.valueOf(string));
        }

        @Override
        public String convertToEntityAttribute(BigInteger bigInteger) {
            return bigInteger == null ? null : bigInteger.toString();
        }
}
