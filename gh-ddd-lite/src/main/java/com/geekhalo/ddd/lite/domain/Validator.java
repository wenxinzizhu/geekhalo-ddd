package com.geekhalo.ddd.lite.domain;

/**
 * Created by taoli on 17/11/16.
 */
public interface Validator {
    void validate(ValidationHandler handler);

    default void validateAndCheck(ValidationHandler handler){
        validate(handler);
        handler.check();
    }
}
