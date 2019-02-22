package com.geekhalo.ddd.lite.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by taoli on 17/11/17.
 */
public class ValidationException extends BusinessException{
    private final List<String> errorMsg = Lists.newArrayList();

    public ValidationException(List<String> msgs) {
        super(404, msgs.stream().collect(Collectors.joining(",")));
        this.errorMsg.addAll(msgs);
    }
}
