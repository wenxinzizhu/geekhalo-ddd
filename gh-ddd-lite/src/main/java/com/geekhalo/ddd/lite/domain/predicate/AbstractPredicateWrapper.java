package com.geekhalo.ddd.lite.domain.predicate;

import com.querydsl.collections.CollQueryFactory;
import com.querydsl.core.types.Path;

public abstract class AbstractPredicateWrapper<T> implements PredicateWrapper<T>{
    private final Path<T> path;

    protected AbstractPredicateWrapper(Path<T> path) {
        this.path = path;
    }

    @Override
    public final boolean accept(T t) {
        return CollQueryFactory
                .from(this.path, t)
                .where(getPredicate())
                .fetchCount() > 0;
    }

}