package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.Repository;

import java.util.List;

public interface SpringDataRepositoryAdapter<ID, A extends Aggregate<ID>> extends Repository<ID, A> {
    @Deprecated
    @Override
    default List<A> getByIds(List<ID> ids){
        return getByIdIn(ids);
    }

    @Override
    default void update(A a){
        save(a);
    }

    List<A> getByIdIn(List<ID> ids);

}
