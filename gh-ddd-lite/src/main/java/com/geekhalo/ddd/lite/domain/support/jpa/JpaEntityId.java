package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.EntityId;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class JpaEntityId implements EntityId {
    @Column(name = "id", updatable = false, nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private String value;

    @Override
    public String getValue() {
        return value;
    }
}
