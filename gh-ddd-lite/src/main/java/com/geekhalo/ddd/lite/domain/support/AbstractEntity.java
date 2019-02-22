package com.geekhalo.ddd.lite.domain.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.geekhalo.ddd.lite.domain.DomainEvent;
import com.geekhalo.ddd.lite.domain.Entity;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.querydsl.core.annotations.QueryTransient;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by taoli on 17/11/16.
 */
public abstract class AbstractEntity<ID> implements Entity<ID> {

    @JsonIgnore
    @QueryTransient
    private final List<DomainEventItem> events = Lists.newArrayList();

    protected void registerEvent(DomainEvent event) {
        events.add(new DomainEventItem(event));
    }

    protected void registerEvent(Supplier<DomainEvent> eventSupplier) {
        this.events.add(new DomainEventItem(eventSupplier));
    }

    @Override
    @JsonIgnore
    public List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events.stream()
                .map(eventSupplier -> eventSupplier.getEvent())
                .collect(Collectors.toList()));
    }

    @Override
    public void cleanEvents() {
        events.clear();
    }

    private class DomainEventItem {
        DomainEventItem(DomainEvent event) {
            Preconditions.checkArgument(event != null);
            this.domainEvent = event;
        }

        DomainEventItem(Supplier<DomainEvent> supplier) {
            Preconditions.checkArgument(supplier != null);
            this.domainEventSupplier = supplier;
        }

        private DomainEvent domainEvent;
        private Supplier<DomainEvent> domainEventSupplier;

        public DomainEvent getEvent() {
            if (domainEvent != null) {
                return domainEvent;
            }
            DomainEvent event = this.domainEventSupplier != null ? this.domainEventSupplier.get() : null;
            domainEvent = event;
            return domainEvent;
        }
    }
}
