package com.geekhalo.ddd.lite.domain.predicate;

import com.querydsl.core.types.Predicate;

public interface PredicateWrapper<T>{
    boolean accept(T t);

    Predicate getPredicate();
}