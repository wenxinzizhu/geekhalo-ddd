package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.support.AbstractAggregate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by taoli on 17/11/17.
 */
@Data
@MappedSuperclass
public abstract class JpaAggregate extends AbstractAggregate<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "id")
    private Long id;


    @Override
    public Long getId() {
        return id;
    }
}
