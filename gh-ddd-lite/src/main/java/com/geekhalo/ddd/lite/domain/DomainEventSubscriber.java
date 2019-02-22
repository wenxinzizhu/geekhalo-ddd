package com.geekhalo.ddd.lite.domain;

/**
 * Created by taoli on 17/11/16.
 */
public interface DomainEventSubscriber<E extends DomainEvent> {
    boolean accept(E e);
}
