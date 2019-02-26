package com.geekhalo.ddd.lite.domain.support;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.DomainEvent;
import com.geekhalo.ddd.lite.domain.ValidationHandler;
import com.geekhalo.ddd.lite.domain.Validator;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.querydsl.core.annotations.QueryTransient;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by taoli on 17/11/17.
 */
@MappedSuperclass
public abstract class AbstractAggregate<ID> extends AbstractEntity<ID> implements Aggregate<ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAggregate.class);

    @JsonIgnore
    @QueryTransient
    @Transient
    private final transient List<DomainEventItem> events = Lists.newArrayList();

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
