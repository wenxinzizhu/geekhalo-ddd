package com.geekhalo.ddd.lite.domain;

public interface AggregateEvent<ID, A extends Aggregate<ID>> extends DomainEvent{
    A source();

    default A getSource(){
        return source();
    }
}
