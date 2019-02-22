package com.geekhalo.ddd.lite.domain;

/**
 * Created by taoli on 17/11/16.
 */
public interface ValidationHandler {
    void handleError(String msg);

    void check();
}
