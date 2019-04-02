package com.geekhalo.ddd.lite.domain;

import java.util.Date;

/**
 * Created by taoli on 17/11/16.
 */
public interface DomainEvent {

    String id();

    default String getId(){
        return id();
    }

    Date occurredOn();

    default Date getCreateTime(){
        return occurredOn();
    }

    default String type(){
        return getClass().getSimpleName();
    }

    default String getType(){
        return type();
    }
}
