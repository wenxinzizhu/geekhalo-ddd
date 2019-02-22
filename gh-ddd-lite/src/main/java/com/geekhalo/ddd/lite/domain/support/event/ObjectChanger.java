package com.geekhalo.ddd.lite.domain.support.event;

import com.geekhalo.ddd.lite.domain.ValueObject;
import lombok.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by taoli on 17/11/22.
 */
@Value
public class ObjectChanger<T> implements ValueObject, Changer{
    private final T from;
    private final T to;

    private ObjectChanger(T from, T to){
        this.from = from;
        this.to = to;
    }

    public static <T> ObjectChanger<T> accept(T from, T to){
        if (!Objects.equals(from, to)){
            return new ObjectChanger(from, to);
        }
        return null;
    }

    public static <T> Optional<ObjectChanger<T>> acceptAsOpt(T from, T to){
        return Optional.ofNullable(accept(from, to));
    }
}
