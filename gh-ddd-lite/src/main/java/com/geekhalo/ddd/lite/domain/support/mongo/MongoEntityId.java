package com.geekhalo.ddd.lite.domain.support.mongo;

import com.geekhalo.ddd.lite.domain.EntityId;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter(AccessLevel.PROTECTED)
public abstract class MongoEntityId implements EntityId {
    private String value;
    protected MongoEntityId(){

    }

    protected MongoEntityId(String id){
        setValue(id);
    }

    @Override
    public String getValue() {
        return value;
    }

    protected void setValue(String value){
        Preconditions.checkArgument(StringUtils.isNotEmpty(value));
        this.value = value;
    }
}
