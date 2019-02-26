package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.ValidationHandler;
import com.geekhalo.ddd.lite.domain.support.AbstractAggregate;
import com.querydsl.core.annotations.QueryTransient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by taoli on 17/11/17.
 */
@Data
@MappedSuperclass
public abstract class JpaAggregate extends AbstractAggregate<Long> implements Aggregate<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "id")
    private Long id;


    @Override
    public Long getId() {
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
