package com.geekhalo.ddd.lite.domain.support.mongo;

import com.geekhalo.ddd.lite.domain.support.AbstractEntity;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

public abstract class MongoEntity extends AbstractEntity<BigInteger> {
    @Id
    @Setter(AccessLevel.PROTECTED)
    private BigInteger id;

    @Override
    public BigInteger getId() {
        return id;
    }

}
