package com.geekhalo.ddd.lite.domain;

/**
 * Created by taoli on 17/11/17.
 */
public class AggregateNotFountException extends BusinessException{
    private final Object id;

    public AggregateNotFountException(Object id) {
        super(404, "Aggregate "+ id +" Not Found");
        this.id = id;
    }
}
