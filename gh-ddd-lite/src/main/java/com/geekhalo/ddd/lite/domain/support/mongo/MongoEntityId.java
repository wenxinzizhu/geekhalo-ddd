package com.geekhalo.ddd.lite.domain.support.mongo;

import com.geekhalo.ddd.lite.domain.EntityId;

public abstract class MongoEntityId implements EntityId {
    private String id;
    @Override
    public String getValue() {
        return id;
    }
}
