package com.geekhalo.ddd.lite.domain.support.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.geekhalo.ddd.lite.domain.support.AbstractEntity;
import com.geekhalo.ddd.lite.domain.support.jpa.converters.InstantLongConverter;
import com.querydsl.core.annotations.QueryTransient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

/**
 * Created by taoli on 17/11/16.
 */
@Data
@MappedSuperclass
public abstract class JpaEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "id")
    private Long id;

    @Version
    @Setter(AccessLevel.PRIVATE)
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Setter(AccessLevel.PRIVATE)
    @Convert(converter = InstantLongConverter.class)
    @JsonIgnore
    private Instant createTime;

    @Column(name = "update_time", nullable = false)
    @Setter(AccessLevel.PRIVATE)
    @Convert(converter = InstantLongConverter.class)
    @JsonIgnore
    private Instant updateTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @PrePersist
    public void prePersist(){
        this.setCreateTime(Instant.now());
        this.setUpdateTime(Instant.now());
    }

    @PreUpdate
    public void preUpdate(){
        this.setUpdateTime(Instant.now());
    }

    @QueryTransient
    public Long getCreateTimeAsMS(){
        return  toMs(getCreateTime());
    }

    @QueryTransient
    public Long getUpdateTimeAsMS(){
        return toMs(getUpdateTime());
    }

    protected Long toMs(Instant instant){
        return instant == null ? null : instant.toEpochMilli();
    }
}
