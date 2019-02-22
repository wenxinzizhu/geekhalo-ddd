package com.geekhalo.ddd.lite.domain;

import java.util.List;

/**
 * Created by taoli on 17/11/16.
 */
public interface Entity<ID> {
    ID getId();

    int getVersion();

    List<DomainEvent> getEvents();

    void cleanEvents();
}
