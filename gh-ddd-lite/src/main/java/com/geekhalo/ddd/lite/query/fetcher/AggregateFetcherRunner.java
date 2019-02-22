package com.geekhalo.ddd.lite.query.fetcher;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.Repository;

public class AggregateFetcherRunner<ID, A extends Aggregate<ID>> extends FetcherRunner<ID, A>{
    public AggregateFetcherRunner(Repository<ID, A> repository) {
        super(ids -> repository.getByIds(ids), a -> a.getId());
    }
}
