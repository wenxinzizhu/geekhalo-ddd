package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.DomainEvent;
import com.google.common.base.Preconditions;

/**
 * Created by taoli on 17/11/16.
 */
public abstract class AbstractDomainEvent<ID, A extends Aggregate<ID>> implements DomainEvent<ID, A> {
    private final A source;

    public AbstractDomainEvent(A source) {
        Preconditions.checkArgument(source != null);
        this.source = source;
    }

    @Override
    public A getSource() {
        return source;
    }
}
