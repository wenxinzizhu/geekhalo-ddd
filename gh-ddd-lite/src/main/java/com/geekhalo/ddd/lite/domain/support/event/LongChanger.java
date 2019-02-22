package com.geekhalo.ddd.lite.domain.support.event;

import com.geekhalo.ddd.lite.domain.ValueObject;
import lombok.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by taoli on 17/11/19.
 */
@Value
public class LongChanger implements ValueObject, Changer {
    private final Long from;
    private final Long to;

    private LongChanger(Long from, Long to){
        this.from = from;
        this.to = to;
    }

    public static LongChanger apply(Long from, Long to){
        if (!Objects.equals(from, to)){
            return new LongChanger(from, to);
        }
        return null;
    }

    public static Optional<LongChanger> applyAsOpt(Long from, Long to){
        return Optional.ofNullable(apply(from, to));
    }
}
