package com.geekhalo.ddd.lite.domain.support.event;

import com.geekhalo.ddd.lite.domain.ValueObject;
import lombok.Value;

import java.util.Optional;

/**
 * Created by taoli on 17/11/19.
 */
@Value
public class IntChanger implements ValueObject, Changer{
    private final int from;
    private final int to;

    private IntChanger(int from, int to){
        this.from = from;
        this.to =to;
    }

    public static IntChanger apply(int from, int to){
        if (from != to){
            return new IntChanger(from, to);
        }
        return null;
    }

    public static Optional<IntChanger> applyAsOpt(int from, int to){
        return Optional.ofNullable(apply(from, to));
    }
}
