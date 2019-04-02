package com.geekhalo.ddd.lite.demo.domain.news.category;

import com.geekhalo.ddd.lite.domain.support.AbstractAggregateEvent;
import com.geekhalo.ddd.lite.domain.support.AbstractDomainEvent;
import lombok.Value;


@Value
public class NewsCategoryCreatedEvent extends AbstractAggregateEvent<NewsCategoryId, NewsCategory> {
    private final String name;
    public NewsCategoryCreatedEvent(NewsCategory source, String name) {
        super(source);
        this.name = name;
    }
}
