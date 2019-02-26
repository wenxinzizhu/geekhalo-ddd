package com.geekhalo.ddd.lite.domain.support.mongo;

import com.geekhalo.ddd.lite.domain.EntityId;
import com.geekhalo.ddd.lite.domain.support.AbstractAggregate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.math.BigInteger;

@MappedSuperclass
public abstract class IdentitiedMongoAggregate<ID extends EntityId> extends AbstractAggregate<ID> {
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
