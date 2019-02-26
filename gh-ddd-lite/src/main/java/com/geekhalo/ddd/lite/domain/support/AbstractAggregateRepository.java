package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.AggregateRepository;

/**
 * Created by taoli on 17/11/17.
 */
public abstract class AbstractAggregateRepository<ID, A extends Aggregate<ID>> implements AggregateRepository<ID, A> {
}
