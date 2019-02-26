package com.geekhalo.ddd.lite.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by taoli on 17/11/16.
 */
public interface Aggregate<ID> extends Validator, Entity<ID>{

    @JsonIgnore
    List<DomainEvent> getEvents();

    void cleanEvents();
}
