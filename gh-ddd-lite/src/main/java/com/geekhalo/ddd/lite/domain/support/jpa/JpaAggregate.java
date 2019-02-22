package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.ValidationHandler;
import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * Created by taoli on 17/11/17.
 */
@Data
@MappedSuperclass
public abstract class JpaAggregate extends JpaEntity implements Aggregate<Long> {

    @Override
    public void validate(ValidationHandler handler) {
    }


}
