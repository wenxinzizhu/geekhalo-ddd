package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.EntityId;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class JpaEntityId implements EntityId {
    protected JpaEntityId(){

    }
    protected JpaEntityId(String value){
        setValue(value);
    }
    @Column(name = "id", updatable = false, nullable = false)
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    protected void setValue(String value){
        Preconditions.checkArgument(StringUtils.isNotEmpty(value));
        this.value = value;
    }
}
