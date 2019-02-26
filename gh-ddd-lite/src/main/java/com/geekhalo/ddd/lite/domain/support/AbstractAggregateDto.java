package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public abstract class AbstractAggregateDto<ID> extends AbstractEntityDto<ID>{

    protected AbstractAggregateDto(AbstractAggregate<ID> source) {
       super(source);
    }

    protected AbstractAggregateDto() {
    }
}
