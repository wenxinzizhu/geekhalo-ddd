package com.geekhalo.ddd.lite.domain;

/**
 * Created by taoli on 17/11/16.
 */
public interface DomainEventHandler<E extends DomainEvent> {
    void handle(E event);
}
