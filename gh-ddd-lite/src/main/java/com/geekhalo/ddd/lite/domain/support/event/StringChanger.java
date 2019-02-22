package com.geekhalo.ddd.lite.domain.support.event;

import com.geekhalo.ddd.lite.domain.ValueObject;
import lombok.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by taoli on 17/11/19.
 */
@Value
public class StringChanger implements ValueObject, Changer {
    private final String from;
    private final String to;
    private StringChanger(String from, String to){
        this.from = from;
        this.to = to;
    }

    public static StringChanger apply(String from, String to){
        if (!Objects.equals(from, to)){
            return new StringChanger(from, to);
        }
        return null;
    }

    public static Optional<StringChanger> applyAsOpt(String from, String to){
        return Optional.ofNullable(apply(from, to));
    }
}
