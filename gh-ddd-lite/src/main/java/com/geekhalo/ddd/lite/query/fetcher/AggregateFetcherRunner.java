package com.geekhalo.ddd.lite.query.fetcher;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.AggregateRepository;

public class AggregateFetcherRunner<ID, A extends Aggregate<ID>> extends FetcherRunner<ID, A>{
    public AggregateFetcherRunner(AggregateRepository<ID, A> aggregateRepository) {
        super(ids -> aggregateRepository.getByIdIn(ids), a -> a.getId());
    }
}
