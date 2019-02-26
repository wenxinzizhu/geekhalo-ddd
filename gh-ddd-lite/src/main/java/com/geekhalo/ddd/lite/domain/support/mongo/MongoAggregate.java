package com.geekhalo.ddd.lite.domain.support.mongo;

import com.geekhalo.ddd.lite.domain.support.AbstractAggregate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

public abstract class MongoAggregate  extends AbstractAggregate<BigInteger> {
    @Id
    @Setter(AccessLevel.PROTECTED)
    private BigInteger id;

    @Override
    public BigInteger getId() {
        return id;
    }
}
