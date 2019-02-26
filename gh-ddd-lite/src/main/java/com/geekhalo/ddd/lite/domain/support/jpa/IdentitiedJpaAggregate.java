package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.EntityId;
import com.geekhalo.ddd.lite.domain.support.AbstractAggregate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class IdentitiedJpaAggregate<ID extends EntityId> extends AbstractAggregate<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private Long _id;


    @Setter(AccessLevel.PROTECTED)
    private ID id;

    @Override
    public ID getId() {
        return id;
    }

    @PrePersist
    public void prePersist(){
        Date now = new Date();
        this.setCreateTime(now);
        this.setUpdateTime(now);
    }

    @PreUpdate
    public void preUpdate(){
        this.setUpdateTime(new Date());
    }
}
