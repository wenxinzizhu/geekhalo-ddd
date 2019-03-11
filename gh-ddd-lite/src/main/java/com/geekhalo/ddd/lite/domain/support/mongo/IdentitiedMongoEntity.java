package com.geekhalo.ddd.lite.domain.support.mongo;

import com.geekhalo.ddd.lite.domain.EntityId;
import com.geekhalo.ddd.lite.domain.support.AbstractEntity;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntityId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

public abstract class IdentitiedMongoEntity<ID extends MongoEntityId> extends AbstractEntity<ID> {

    @Id
    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PRIVATE)
    private BigInteger _id;

    @Setter(AccessLevel.PROTECTED)
    private ID id;

    @Override
    public ID getId() {
        return id;
    }
}
