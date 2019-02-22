package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.ValidationHandler;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by taoli on 17/11/17.
 */
public abstract class AbstractValidationHandler implements ValidationHandler {
    private final List<String> errorMsg = Lists.newArrayList();

    @Override
    public void handleError(String msg) {
        this.errorMsg.add(msg);
    }

    public List<String> getErrorMsg(){
        return Collections.unmodifiableList(this.errorMsg);
    }
}
