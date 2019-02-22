package com.geekhalo.ddd.lite.domain;

import java.util.List;

/**
 * Created by taoli on 17/11/16.
 */
public interface DomainEventPublisher {
    <ID, EVENT extends DomainEvent>void publish(EVENT event);

    default <ID, EVENT extends DomainEvent>void publishAll(List<EVENT> events){
        events.forEach(this::publish);
    }
}
