package com.geekhalo.ddd.lite.domain.support.jpa.converters;

import javax.persistence.AttributeConverter;
import java.math.BigInteger;
import java.time.Instant;

/**
 * Created by fw on 2018/4/9
 */
public class InstantBigIntegerConverter implements AttributeConverter<Instant,BigInteger> {

        @Override
        public BigInteger convertToDatabaseColumn(Instant date) {
            return date == null ? null : BigInteger.valueOf(date.toEpochMilli());
        }

        @Override
        public Instant convertToEntityAttribute(BigInteger date) {
            return date == null ? null : Instant.ofEpochMilli(date.longValue());
        }
}
