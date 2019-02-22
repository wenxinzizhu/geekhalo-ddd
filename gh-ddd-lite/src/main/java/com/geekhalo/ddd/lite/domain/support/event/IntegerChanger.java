package com.geekhalo.ddd.lite.domain.support.event;

import com.geekhalo.ddd.lite.domain.ValueObject;
import lombok.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by taoli on 17/11/19.
 */
@Value
public class IntegerChanger implements ValueObject, Changer {
    private final Integer from;
    private final Integer to;

    private IntegerChanger(Integer from, Integer to){
        this.from =from;
        this.to = to;
    }

    public static IntegerChanger apply(Integer from, Integer to){
        if (!Objects.equals(from, to)){
            return new IntegerChanger(from, to);
        }
        return null;
    }

    public static Optional<IntegerChanger> applyAsOpt(Integer from, Integer to){
        return Optional.ofNullable(apply(from, to));
    }
}
