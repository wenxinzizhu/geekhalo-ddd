package com.geekhalo.ddd.lite.domain;

/**
 * Created by taoli on 17/11/16.
 */
public interface DomainEvent<ID, E extends Entity<ID>> {
    E getSource();

    default String getType(){
        return getClass().getSimpleName();
    }
}
