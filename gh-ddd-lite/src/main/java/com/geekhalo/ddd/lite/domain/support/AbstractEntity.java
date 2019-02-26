package com.geekhalo.ddd.lite.domain.support;


import com.geekhalo.ddd.lite.domain.Entity;
import com.geekhalo.ddd.lite.domain.ValidationHandler;
import com.geekhalo.ddd.lite.domain.Validator;
import com.querydsl.core.annotations.QueryTransient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Created by taoli on 17/11/16.
 */
@Getter(AccessLevel.PUBLIC)
@MappedSuperclass
public abstract class AbstractEntity<ID> implements Entity<ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntity.class);

    @Version
    @Setter(AccessLevel.PRIVATE)
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Setter(AccessLevel.PROTECTED)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Date updateTime;

    @QueryTransient
    public Long getCreateTimeAsMS(){
        return  toMs(getCreateTime());
    }

    @QueryTransient
    public Long getUpdateTimeAsMS(){
        return toMs(getUpdateTime());
    }

    protected Long toMs(Date date){
        return date == null ? null : date.getTime();
    }

    @Override
    public void validate(ValidationHandler handler) {
        for (Field field : FieldUtils.getAllFieldsList(getClass())){
            try {
                Object value = FieldUtils.readField(field, this, true);
                if (value instanceof Validator){
                    ((Validator) value).validate(handler);
                }
                if (value instanceof Collections){
                    ((Collection) value).forEach(v->{
                        if (v instanceof Validator){
                            ((Validator) v).validate(handler);
                        }
                    });
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("failed to get value of {} from {}.", field, this);
            }
        }
    }
}
