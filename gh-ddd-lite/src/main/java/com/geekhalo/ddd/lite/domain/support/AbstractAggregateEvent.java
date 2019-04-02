package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.AggregateEvent;
import com.google.common.base.Preconditions;

public abstract class AbstractAggregateEvent<ID, A extends Aggregate<ID>>
    extends AbstractDomainEvent
    implements AggregateEvent<ID, A> {
    private final A source;

    public AbstractAggregateEvent(A source) {
        Preconditions.checkArgument(source != null);
        this.source = source;
    }

    public AbstractAggregateEvent(String id, A source){
        super(id);
        this.source = source;
    }

    @Override
    public A source() {
        return this.source;
    }
}
