package com.geekhalo.ddd.lite.domain.support;


import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.ValidationHandler;

/**
 * Created by taoli on 17/11/17.
 */
public abstract class AbstractAggregate<ID> extends AbstractEntity<ID> implements Aggregate<ID> {

    @Override
    public void validate(ValidationHandler handler) {

    }
}
