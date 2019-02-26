package com.geekhalo.ddd.lite.domain.support;

import lombok.Data;

@Data
public abstract class AbstractAggregateDto<ID> extends AbstractEntityDto<ID>{

    protected AbstractAggregateDto(AbstractAggregate<ID> source) {
       super(source);
    }

    protected AbstractAggregateDto() {
    }
}
